package com.company.graphics.panels;

import com.company.classes.Coordinates;
import com.company.classes.Worker;
import com.company.database.DataBase;
import com.company.enums.Commands;
import com.company.enums.Languages;
import com.company.exceptions.NotConnectedException;
import com.company.graphics.Language;
import com.company.graphics.frames.GeneralFrame;
import com.company.graphics.frames.MainFrame;
import com.company.network.Client;
import com.company.network.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Locale;

public class VisualisationPanel extends GeneralPanel {

    Client client;
    LinkedList<Worker> dataBase;
    Messages input = null;
    JTextArea messageArea;
    JPanel visualisation;
    JPanel info;

    JButton backButton;
    JComboBox langJComboBox;

    public void updateImage(){
        try {
            input = (Messages) client.readCommand(Commands.GET_DATABASE);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
        dataBase = (LinkedList<Worker>) input.getObject(1);

        visualisation.removeAll();
        for (Worker w: dataBase){
            visualisation.add(new VisualWorker(w));
        }
        visualisation.revalidate();
    }

    public void changeLangue(Languages lang){
        changeLanguage(lang);
        backButton.setText(language.getBackButton());

        langJComboBox.setSelectedItem(lang);

        revalidate();
    }

    public VisualisationPanel(GeneralFrame parentFrame, Container c, Client client, Locale locale, Language language, Languages currentLanguage) {
        super(parentFrame, c, locale, language, currentLanguage);

        BoxLayout boxlayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxlayout);

        info = new JPanel();
        visualisation = new JPanel();

        String message = "";
        messageArea = new JTextArea(message);
        messageArea.setMinimumSize(new Dimension(250, 50));
        messageArea.setPreferredSize(new Dimension(250, 50));
        messageArea.setMaximumSize(new Dimension(250, 50));
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        this.client = client;
        container = this;

        langJComboBox = new JComboBox<>(Languages.values());
        langJComboBox.setSelectedItem(language);
        langJComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLangue((Languages) langJComboBox.getSelectedItem());
            }
        });



        backButton = new JButton(language.getBackButton());
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanelInFrame("database");
            }
        });

        info.add(messageArea);
        info.add(backButton);
        info.add(langJComboBox);

        this.add(info);
        this.add(visualisation);

        updateImage();

        this.addComponentListener ( new ComponentAdapter()
        {
            public void componentShown ( ComponentEvent e )
            {
                System.out.println("Visualisation shown");
                updateImage();
                changeLangue(language.getCurrentLang());
            }

            public void componentHidden ( ComponentEvent e )
            {
                System.out.println ( "Visualisation hidden" );
            }
        } );


        this.setVisible(true);
    }

    public class VisualWorker extends JComponent implements MouseListener, ActionListener {

        Timer timer;
        private double angle = 0;
        private double scale = 1;
        private double delta = 0.01;


        String text;
        int x;
        int y;
        int width;
        int height;
        Color color;
        long id;

        public VisualWorker(Worker worker) {
            this.x = (int) worker.getCoordinates().getX();
            this.y = worker.getCoordinates().getY();
            this.width = (int) worker.getSalary();
            this.height = (int) worker.getSalary();
            this.id = worker.getId();

            System.out.println(this.height + " " + this.width);

            this.color = Color.getHSBColor(worker.getUser().getLogin().hashCode(),
                    worker.getUser().getLogin().hashCode() + 120,
                    worker.getUser().getLogin().hashCode() - 90);
            this.color = color;
            this.text = worker.getName();
            setPreferredSize(new Dimension(width, height));

            timer = new Timer(worker.getPerson().getWeight(), this);
            timer.start();

            addMouseListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (scale < 0.01) {
                delta = -delta;
            } else if (scale > 0.99) {
                delta = -delta;
            }

            scale += delta;
            angle += 0.01;

            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            Rectangle.Float r = new Rectangle.Float(this.x, this.y, this.width, this.height);

            int h = getHeight();
            int w = getWidth();

            Graphics2D g2d = (Graphics2D) g;

            g.setColor(color);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            g2d.translate(w / 2, h / 2);
            g2d.rotate(angle);
            g2d.scale(scale, scale);

            g2d.fill(r);

            g.setColor(Color.MAGENTA);
            g.setFont(new Font("default", Font.BOLD, (width+height)/20));
            g.drawString(this.text, this.x + width/2, this.y + height/2);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            long id = this.id;

            client.output.addObject(client.user);
            client.output.addObject(Commands.UPDATE);
            client.output.addObject(id);

            try {
                input = client.sendMessage();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            //aborting if response is string (== error)
            if (input.getObject(1).getClass().equals(String.class)){
                messageArea.setText((String) input.getObject(1));
                return;
            } else {
                MainFrame generalFrame = (MainFrame) parentFrame;
                generalFrame.setWorkerToUpdate((Worker) input.getObject(1));
                changePanelInFrame("updateworker");
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}

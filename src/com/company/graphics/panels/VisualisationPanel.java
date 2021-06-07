package com.company.graphics.panels;

import com.company.classes.Coordinates;
import com.company.classes.Worker;
import com.company.database.DataBase;
import com.company.enums.Commands;
import com.company.exceptions.NotConnectedException;
import com.company.graphics.frames.GeneralFrame;
import com.company.network.Client;
import com.company.network.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class VisualisationPanel extends GeneralPanel {

    Client client;
    LinkedList<Worker> dataBase;
    Messages input = null;

    public void updateImage(){
        try {
            input = (Messages) client.readCommand(Commands.GET_DATABASE);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
        dataBase = (LinkedList<Worker>) input.getObject(1);

        this.removeAll();
        for (Worker w: dataBase){
            this.add(new VisualWorker(w));
        }
        this.revalidate();
    }

    public VisualisationPanel(GeneralFrame parentFrame, Container c, Client client) {
        super(parentFrame, c);

        this.client = client;
        container = this;

        updateImage();

        this.addComponentListener ( new ComponentAdapter()
        {
            public void componentShown ( ComponentEvent e )
            {
                System.out.println("Visualisation shown");
                updateImage();
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

        public VisualWorker(Worker worker) {
            this.x = (int) worker.getCoordinates().getX();
            this.y = worker.getCoordinates().getY();
            this.width = (int) worker.getSalary();
            this.height = (int) worker.getSalary();

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
            changePanelInFrame("updateworker");
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

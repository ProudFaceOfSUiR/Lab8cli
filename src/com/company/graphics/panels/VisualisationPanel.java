package com.company.graphics.panels;

import com.company.classes.Coordinates;
import com.company.graphics.frames.GeneralFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class VisualisationPanel extends GeneralPanel{

    public VisualisationPanel(GeneralFrame parentFrame, Container c) {
        super(parentFrame, c);

        container = this;

        Coordinates[] coord = new Coordinates[5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                coord[i] = new Coordinates(i,j);
            }
        }
        for (int i = 0; i < 5; i++) {
            this.add(new VisualWorker(coord[i]));
        }
    }

    public class VisualWorker extends JComponent implements MouseListener {

        String text = "Text";
        int x;
        int y;

        public VisualWorker(Coordinates coodinates) {
            this.x = (int) coodinates.getX();
            this.y = coodinates.getY();
            setPreferredSize(new Dimension(500, 100));

            addMouseListener(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillRect(this.x, this.y, 500, 100);

            g.setColor(Color.MAGENTA);
            g.setFont(new Font("default", Font.BOLD, 30));
            g.drawString(this.text, this.x + 100, this.y + 30);
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

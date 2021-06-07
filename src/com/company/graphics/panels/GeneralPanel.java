package com.company.graphics.panels;

import com.company.graphics.frames.GeneralFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeneralPanel extends JPanel {
    protected CardLayout cards;
    protected Container container;

    protected GeneralFrame parentFrame;

    protected int screenWidth;
    protected int screenHeigth;

    protected int width;
    protected int heigth;

    public GeneralPanel(GeneralFrame parentFrame,Container c){
        this.parentFrame = parentFrame;
        this.container = c;
        this.width = parentFrame.width;
        this.heigth = parentFrame.heigth;
    }

    public void setScreenSize(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.screenWidth = dimension.width;
        this.screenHeigth = dimension.height;
    }

    public void nextPanelInFrame(){
        parentFrame.cards.next(parentFrame.c);
    }

    public void previousPanelInFrame(){
        parentFrame.cards.previous(parentFrame.c);
    }

    public void changePanelInFrame(String frameCodename){
        parentFrame.cards.show(parentFrame.c, frameCodename);
    }

    public class ChangeCard implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cards.next(container);
        }
    }
}

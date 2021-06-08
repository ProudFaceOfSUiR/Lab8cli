package com.company.graphics.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class GeneralFrame extends JFrame {
    Locale locale;
    //screen size
    protected int screenWidth;
    protected int screenHeigth;
    //window size
    public int width;
    public int heigth;
    //graphics
    public CardLayout cards;
    public Container c;

    public GeneralFrame(CardLayout cards, Container c, Locale locale){
        this.cards = cards;
        this.c = c;
        this.locale = locale;
    }

    public void setWindowSize(int width, int heigth){
        this.width = width;
        this.heigth = heigth;
    }

    public void setScreenSize(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.screenWidth = dimension.width;
        this.screenHeigth = dimension.height;
    }

    public class ChangeCard implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cards.next(c);
        }
    }
}

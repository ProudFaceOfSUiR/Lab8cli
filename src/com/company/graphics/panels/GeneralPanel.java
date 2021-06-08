package com.company.graphics.panels;

import com.company.enums.Languages;
import com.company.graphics.Language;
import com.company.graphics.frames.GeneralFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class GeneralPanel extends JPanel {

    Locale locale;
    ResourceBundle rb;

    Language language;
    Languages currentLang;

    protected CardLayout cards;
    protected Container container;

    protected GeneralFrame parentFrame;

    protected int screenWidth;
    protected int screenHeigth;

    protected int width;
    protected int heigth;

    public GeneralPanel(GeneralFrame parentFrame,Container c, Locale locale, Language language, Languages currentLang){
        this.parentFrame = parentFrame;
        this.container = c;
        this.width = parentFrame.width;
        this.heigth = parentFrame.heigth;
        this.locale = locale;
        this.language = language;
        this.currentLang = currentLang;
    }

    public void changeLocale(Locale locale){
        this.locale = locale;
    }

    public void setScreenSize(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.screenWidth = dimension.width;
        this.screenHeigth = dimension.height;
    }

    public void changeLanguage(Languages language){
        this.language.setLanguage(language);
        System.out.println("New language: " + this.language.getCurrentLang().toString());
        parentFrame.repaint();
        parentFrame.revalidate();
        container.repaint();
        this.repaint();
        revalidate();
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

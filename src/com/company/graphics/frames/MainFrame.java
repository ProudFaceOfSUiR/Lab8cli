package com.company.graphics.frames;


import com.company.classes.Worker;
import com.company.database.DataBase;
import com.company.enums.Commands;
import com.company.enums.Languages;
import com.company.graphics.Language;
import com.company.graphics.panels.*;
import com.company.network.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainFrame extends GeneralFrame {

    Language language = new Language();

    DataBase dataBase;
    Client client;
    Worker workerToUpdate;

    LoginPanel loginPanel;
    DataBasePanel dataBasePanel;
    AddWorkerPanel addWorkerPanel;
    UpdateWorkerPanel updateWorkerPanel;
    VisualisationPanel visualisationPanel;

    public void setWorkerToUpdate(Worker w){
        this.workerToUpdate = w;
    }

    public Worker getWorkerToUpdate() {
        return workerToUpdate;
    }

    public void setOperationOnClose(Object p){
        this.setDefaultCloseOperation((Integer) p);
    }

    public MainFrame(DataBase dataBase, Client client, Locale locale) {
        super(new CardLayout(), new Container(), locale);

        this.dataBase = dataBase;
        this.client = client;

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                int resp = JOptionPane.showConfirmDialog(c, "Are you sure you want to exit?",
                        "Exit?", JOptionPane.YES_NO_OPTION);

                if (resp == JOptionPane.YES_OPTION) {
                    setOperationOnClose(JFrame.EXIT_ON_CLOSE);
                    if (client.user.getLogin() != null){
                        client.output.addObject(client.user);
                        client.output.addObject(Commands.EXIT);
                        client.output.addObject(null);
                        try {
                            client.sendMessage();
                        } catch (Exception ee) {
                            System.out.println(ee.getMessage());
                        }
                    }
                    System.exit(1);
                } else {
                    setOperationOnClose(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        language.setLanguage(Languages.ru_RU);
        this.setWindowSize(800, 600);
        setScreenSize();
        this.setBounds(screenWidth/2 - 350, screenHeigth/2-300, width,heigth);
        this.setTitle("Devil's Database");
        c = this.getContentPane();
        cards = new CardLayout(0, 0);
        c.setLayout(cards);
    }

    public void loading(){
        ImageIcon loading = new ImageIcon("ajax-loader.gif");
        BorderLayout borderLayout = new BorderLayout();
        JPanel loadingPanel = new JPanel(borderLayout);
        loadingPanel.add(new JLabel("connecting... ", loading, JLabel.CENTER),BorderLayout.CENTER);
        c.add(loadingPanel, "loading");
        this.setVisible(true);

        /*
        //connecting
        boolean isConnected = client.connectToServer();
        while (!isConnected){
            isConnected = client.connectToServer();
        }
        run();

         */
    }

    public void run(){
        Language language = new Language();
        Languages currentLanguage = Languages.ru_RU;
        language.setLanguage(currentLanguage);

        loginPanel = new LoginPanel(this,c, client,locale,language, currentLanguage);
        loginPanel.initializeLoginFrame();
        dataBasePanel = new DataBasePanel(this,c,client,locale, language, currentLanguage);
        dataBasePanel.initializeDatabaseFrame();

        addWorkerPanel = new AddWorkerPanel(this,c,client,locale, language, currentLanguage);
        updateWorkerPanel = new UpdateWorkerPanel(this,c,client,locale, language, currentLanguage);
        visualisationPanel = new VisualisationPanel(this, c,client,locale, language,currentLanguage);

        c.add(loginPanel, "login");
        cards.next(c);
        c.add(dataBasePanel, "database");
        c.add(addWorkerPanel, "addworker");
        c.add(updateWorkerPanel, "updateworker");
        c.add(visualisationPanel, "visualisation");

        this.setResizable(false);
        this.setVisible(true);
    }
}

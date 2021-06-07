package com.company.graphics.frames;


import com.company.database.DataBase;
import com.company.enums.Commands;
import com.company.graphics.panels.*;
import com.company.network.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends GeneralFrame {

    DataBase dataBase;
    Client client;

    LoginPanel loginPanel;
    DataBasePanel dataBasePanel;
    AddWorkerPanel addWorkerPanel;
    UpdateWorkerPanel updateWorkerPanel;
    VisualisationPanel visualisationPanel;

    public MainFrame(DataBase dataBase, Client client) {
        super(new CardLayout(), new Container());

        this.dataBase = dataBase;
        this.client = client;

        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                        null, "Are You Sure to Close Devil's Database?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                    client.output.addObject(client.user);
                    client.output.addObject(Commands.EXIT);
                    client.output.addObject(null);
                    try {
                        client.sendMessage();
                    } catch (Exception ee) {
                        System.out.println(ee.getMessage());
                    }
                    System.exit(1);
                }
            }
        };
        this.addWindowListener(exitListener);
    }

    public void run(){
        c = this.getContentPane();
        loginPanel = new LoginPanel(this,c, client);
        loginPanel.initializeLoginFrame();
        dataBasePanel = new DataBasePanel(this,c,client);
        dataBasePanel.initializeDatabaseFrame();

        addWorkerPanel = new AddWorkerPanel(this,c,client);
        updateWorkerPanel = new UpdateWorkerPanel(this,c);
        visualisationPanel = new VisualisationPanel(this, c);

        this.setWindowSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setScreenSize();
        this.setBounds(screenWidth/2 - 250, screenHeigth/2-250, width,heigth);
        this.setTitle("Devil's Database");

        cards = new CardLayout(0, 0);
        c.setLayout(cards);

        c.add(loginPanel, "login");
        c.add(dataBasePanel, "database");
        c.add(addWorkerPanel, "addworker");
        c.add(updateWorkerPanel, "updateworker");
        c.add(visualisationPanel, "visualisation");

        this.setResizable(false);
        this.setVisible(true);
    }
}

package com.company.graphics.frames;


import com.company.database.DataBase;
import com.company.graphics.panels.*;
import com.company.network.Client;

import javax.swing.*;
import java.awt.*;

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

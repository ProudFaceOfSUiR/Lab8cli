package com.company.main;

//variant 5501

import com.company.Login.User;
import com.company.database.DataBase;
import com.company.exceptions.NotConnectedException;
import com.company.graphics.frames.MainFrame;
import com.company.network.Client;
import com.company.network.Messages;
//import com.sun.javaws.IconUtil;


public class Main {

    public static void main(String[] args) throws Exception {
        DataBase dataBase = new DataBase();
        dataBase.initialize();

        //initializing clent
        Client client = new Client(dataBase);

        MainFrame mainFrame = new MainFrame(dataBase, client);


        //connecting and merging databases
        boolean isConnected = client.connectToServer();
        while (!isConnected){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException interruptedException) {
                System.out.println(interruptedException.getMessage());
            }
            System.out.println("Reconnecting...");
            isConnected = client.connectToServer();
        }
        mainFrame.run();

        //connecting and reading commands
        while (true){
            if (!isConnected){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    System.out.println(interruptedException.getMessage());
                }
                System.out.println("Reconnecting...");
                isConnected = client.connectToServer();
                mainFrame.run();
            }

            /*
            boolean hasUser = false;
            Messages messages;
            while (true){
                while (!hasUser){
                    User user = new User();
                    //user.initiate();
                    //todo read from interface
                    client.user = user;
                    client.setUser();

                    messages = client.sendMessage();
                    if (messages.getObject(1).equals(true)){
                        hasUser = true;
                        System.out.println("You have successfully logged in!");
                    } else{
                        System.out.println(messages.getObject(2));
                    }
                }
                try {
                    client.readCommand();
                } catch (NotConnectedException e) {
                    System.out.println(e.getMessage());
                    isConnected = false;
                    break;
                }
            }

             */
        }
    }
}

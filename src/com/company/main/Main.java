package com.company.main;

//variant 312709

import com.company.database.DataBase;
import com.company.exceptions.NotConnectedException;
import com.company.network.Client;

public class Main {

    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        boolean initializedFromFile = false;
        if (args.length == 0){
            dataBase.initialize();
        } else {
            dataBase.initialize(args[0]);
            initializedFromFile = true;
        }

        //initializing clent
        Client client = new Client();
        boolean isInitialized = false;
        while (!isInitialized){
            isInitialized = client.initialize(dataBase);
            if (!isInitialized) {
                try {
                    System.out.println("Reinitializing...");
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    System.out.println(interruptedException.getMessage());
                }
            }
        }

        //connecting and reading commands
        boolean isConnected = client.connectToServer(initializedFromFile);
        while (true){
            if (!isConnected){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    System.out.println(interruptedException.getMessage());
                }
                System.out.println("Reconnecting...");
                isConnected = client.connectToServer(initializedFromFile);
                continue;
            }

            while (true){
                try {
                    client.readCommand();
                } catch (NotConnectedException e) {
                    System.out.println(e.getMessage());
                    isConnected = false;
                    break;
                }
            }
        }
    }
}

package com.company.main;

//variant 312709

import com.company.database.DataBase;
import com.company.database.Terminal;
import com.company.exceptions.NotConnectedException;
import com.company.exceptions.OperationCanceledException;
import com.company.network.Client;
import com.sun.javaws.IconUtil;

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
        Client client = new Client(dataBase);

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
        try {
            if (!dataBase.getDatabase().isEmpty() && initializedFromFile && Terminal.binaryChoice("merge client's database with server's")) {
                client.fillFromFile();
            }
        } catch (OperationCanceledException e) {
            System.out.println(e.getMessage());
        }

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

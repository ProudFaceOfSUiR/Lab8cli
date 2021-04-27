package com.company.main;

//variant 312709

import com.company.database.DataBase;
import com.company.exceptions.NotConnectedException;
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

        //connecting and reading commands
        client.connectToServer(initializedFromFile);
        while (true){
            if (!client.isConnected()){
                System.out.println("Reconnecting...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    System.out.println(interruptedException.getMessage());
                }
                continue;
            }

            while (true){
                try {
                    client.readCommand();
                } catch (NotConnectedException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }
        }
    }
}

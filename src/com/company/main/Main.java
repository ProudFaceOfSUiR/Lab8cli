package com.company.main;

//variant 312709

/*
Main class here is DataBase: it contains the database as LinkedList and operates it.
To start the database you need to initialize it, it will automatically try to load data from the file,
which path was given as an argument of the command line.

!The DataBase will not work without initialization!
 */

import com.company.classes.Worker;
import com.company.database.DataBase;
import com.company.database.FileParser;
import com.company.database.Terminal;
import com.company.enums.Commands;
import com.company.exceptions.InvalidDataException;
import com.company.exceptions.OperationCanceledException;
import com.company.exceptions.UnknownCommandException;
import com.company.network.Messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Scanner;

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

        Socket client = null;
        Scanner terminal = new Scanner(System.in);
        Messages output = new Messages();
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        while (true) {
            try {
                client = new Socket("localhost", 1488);
            } catch (Exception e) {
                System.out.println("Error while creating socket: " + e.getMessage());
                try {
                    System.out.println("Reconnecting...");
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    System.out.println(e.getMessage());
                }
                continue;
            }


            try {
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
            } catch (Exception e) {
                System.out.println("Error while getting streams: " + e.getMessage());
                try {
                    System.out.println("Reconnecting...");
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    System.out.println(e.getMessage());
                }
                continue;
            }
            System.out.println("------------------------------------");
            System.out.println("Connected successfully");
            System.out.println("------------------------------------");

            if (initializedFromFile) {
                try {
                    output.addObject(Commands.FILL_FROM_FILE);
                    output.addObject(dataBase.getDatabase());
                    Objects.requireNonNull(out).writeObject(output);
                    out.flush();
                    System.out.println((String) in.readObject());
                    output.clear();
                    out.reset();
                } catch (Exception e) {
                    System.out.println("Error while working with file: " + e.getMessage());
                }
            }

            System.out.println("Type in commands");

            String input = null;
            //reading from terminal and checking if command exist
            String command;
            while (true) {
                //check when we read from file
                if (!terminal.hasNext()) {
                    return;
                }

                //reading command
                command = terminal.nextLine();
                command = command.toLowerCase();

                //skip empty line
                if (command.matches("\\s")) continue;

                try {
                    //getting command
                    Commands c = Terminal.matchCommand(command);

                    switch (c) {
                        case HELP:
                        case INFO:
                        case SHOW:
                        case CLEAR:
                            output.addObject(c);
                            break;
                        case ADD:
                            output.addObject(c);
                            Worker.WorkerBuilderFromTerminal wb = new Worker.WorkerBuilderFromTerminal();
                            output.addObject(wb.build());
                            break;
                        case UPDATE:
                            //updateById(command);
                            break;
                        case REMOVE_BY_ID:
                            //remove(command);
                            break;
                        case EXECUTE_SCRIPT:
                            //executeScript(command);
                            break;
                        case EXIT:
                            System.exit(1);
                            break;
                        case ADD_IF_MAX:
                            //addIfMax();
                            break;
                        case REMOVE_GREATER:
                            //removeGreater(command);
                            break;
                        case REMOVE_LOWER:
                            //removeLower(command);
                            break;
                        case GROUP_COUNTING_BY_POSITION:
                            //groupCountingByPosition();
                            break;
                        case COUNT_LESS_THAN_START_DATE:
                            //countLessThanStartDate(command);
                            break;
                        case FILTER_GREATER_THAN_START_DATE:
                            //filterGreaterThanStartDate(command);
                            break;
                        default:
                            System.out.println("Unknown command");
                            break;
                    }
                } catch (UnknownCommandException | OperationCanceledException | InvalidDataException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                //sending message to server
                try {
                    out.writeObject(output);
                    out.flush();
                } catch (Exception e) {
                    System.out.println("Error while sending an object: " + e.getMessage());
                    continue;
                }

                //getting feedback
                try {
                    input = (String) in.readObject();
                } catch (Exception e) {
                    System.out.println("Error while getting feedback" + e.getMessage());
                }
                System.out.println("------------------------------------");
                System.out.println(input);
                System.out.println("------------------------------------");

                //resetting input channel
                output.clear();
                try {
                    out.reset();
                } catch (Exception e) {
                    System.out.println("Error while resetting out stream: " + e.getMessage());
                }
            }
        }
    }
}

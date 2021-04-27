package com.company.network;

import com.company.classes.Worker;
import com.company.database.DataBase;
import com.company.database.Terminal;
import com.company.enums.Commands;
import com.company.exceptions.InvalidDataException;
import com.company.exceptions.NotConnectedException;
import com.company.exceptions.OperationCanceledException;
import com.company.exceptions.UnknownCommandException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    private Socket client;
    private Scanner terminal;
    private Messages output;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    DataBase dataBase;
    String command;
    String input;

    public Client (DataBase dataBase){
        this.terminal = new Scanner(System.in);
        this.dataBase = dataBase;
        command = null;
        input = null;
        output = new Messages();
    }

    public boolean isConnected(){
        return this.client.isConnected();
    }

    public boolean connectToServer(boolean initializedFromFile){
        try {
            this.client = new Socket("localhost", 1488);
        } catch (Exception e) {
            System.out.println("Error while creating socket: " + e.getMessage());
            return false;
        }

        try {
            this.out = new ObjectOutputStream(client.getOutputStream());
            this.in = new ObjectInputStream(client.getInputStream());
        } catch (Exception e) {
            System.out.println("Error while getting streams: " + e.getMessage());
            return false;
        }
        System.out.println("------------------------------------");
        System.out.println("Connected successfully");
        System.out.println("------------------------------------");

        if (initializedFromFile){
            fillFromFile();
        }

        return true;
    }

    protected String sendMessage() throws Exception{
        String feedback = null;
        Objects.requireNonNull(out).writeObject(this.output);
        try {
            this.out.flush();
        } catch (Exception e) {
            System.out.println("Error while sending a message: " + e.getMessage());
            throw e;
        }
        try {
            feedback = (String) in.readObject();
        } catch (Exception e) {
            System.out.println("Error while getting feedback: " + e.getMessage());
            throw e;
        }
        out.reset();
        this.output.clear();
        return feedback;
    }

    protected void fillFromFile(){
        try {
            this.output.addObject(Commands.FILL_FROM_FILE);
            this.output.addObject(this.dataBase.getDatabase());
            System.out.println(sendMessage());
        } catch (Exception e) {
            System.out.println("Error while working with file: " + e.getMessage());
        }
    }

    public void readCommand() throws NotConnectedException {
        Commands c;
        //check when we read from file
        if (!terminal.hasNext()) {
            return;
        }

        //reading command
        this.command = terminal.nextLine();
        this.command = command.toLowerCase();

        //skip empty line
        if (command.trim().isEmpty()) return;

        try {
            //getting command
            c = Terminal.matchCommand(command);

            switch (c) {
                case HELP:
                case INFO:
                case SHOW:
                case GROUP_COUNTING_BY_POSITION:
                    this.output.addObject(c);
                    break;
                case CLEAR:
                    if (Terminal.binaryChoice("clear the database")){
                        this.output.addObject(c);
                    } else {
                        System.out.println("Operation cancelled");
                    }
                    break;
                case ADD:
                case ADD_IF_MAX:
                    this.output.addObject(c);
                    Worker.WorkerBuilderFromTerminal wb = new Worker.WorkerBuilderFromTerminal();
                    this.output.addObject(wb.build());
                    break;
                case UPDATE:
                    updateByIdCommand(c, command);
                    return;
                case REMOVE_BY_ID:
                    if (!command.matches("\\s*\\w+\\s+\\d+\\s*")){
                        System.out.println("Invalid id format. Operation cancelled");
                        return;
                    }

                    if (Terminal.binaryChoice("delete worker from database")) {
                        this.output.addObject(c);
                        this.output.addObject(command);
                    } else {
                        System.out.println("Operation cancelled");
                        return;
                    }
                    break;
                case EXECUTE_SCRIPT:
                    //todo
                    //executeScript(command);
                    break;
                case EXIT:
                    //todo
                    break;
                case REMOVE_GREATER:
                case REMOVE_LOWER:
                    if (!command.matches("\\s*\\w+\\s+\\d+\\s*")){
                        System.out.println("Invalid salary format. Operation cancelled");
                        return;
                    }

                    if (Terminal.binaryChoice("remove these workers")) {
                        this.output.addObject(c);
                        this.output.addObject(command);
                    } else {
                        System.out.println("Operation cancelled");
                        return;
                    }
                    break;
                case COUNT_LESS_THAN_START_DATE:
                    command = Terminal.removeString(command, "count_less_than_start_date");
                    if (!command.matches("\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
                        System.out.println("Invalid date format. Operation cancelled");
                        return;
                    }

                    this.output.addObject(c);
                    this.output.addObject(command);

                    break;
                case FILTER_GREATER_THAN_START_DATE:
                    command = Terminal.removeString(command, "filter_greater_than_start_date");
                    if (!command.matches("\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
                        System.out.println("Invalid date format. Operation cancelled");
                        return;
                    }

                    this.output.addObject(c);
                    this.output.addObject(command);

                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        } catch (UnknownCommandException | OperationCanceledException | InvalidDataException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("------------------------------------");
        try {
            System.out.println(sendMessage());
        } catch (Exception e) {
            throw new NotConnectedException();
        }
        System.out.println("------------------------------------");
    }

    protected void updateByIdCommand(Commands command, String commandWithID){

        commandWithID = Terminal.removeString(commandWithID, "update");

        //abort if id is empty
        if (commandWithID.isEmpty() || !commandWithID.matches("\\d+")) {
            System.out.println("Invalid id format. Operation cancelled");
            return;
        }

        long id = Long.parseLong(commandWithID);

        this.output.addObject(command);
        this.output.addObject(id);

        //sending command and id
        try {
            Objects.requireNonNull(out).writeObject(this.output);
            this.out.flush();
            out.reset();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        this.output.clear();

        //updating worker
        try {
            this.output.addObject(
                    this.dataBase.updateElement(
                            (Worker)((Messages) this.in.readObject()).getObject(0)
                    )
            );
        } catch (IOException | ClassNotFoundException | OperationCanceledException e) {
            System.out.println(e.getMessage());
            return;
        }

        //sending worker
        try {
            System.out.println(sendMessage());
        } catch (Exception ignored) {}
    }
}

package com.company.network;

import com.company.Login.User;
import com.company.classes.Worker;
import com.company.database.DataBase;
import com.company.database.FileParser;
import com.company.database.Terminal;
import com.company.enums.Commands;
import com.company.exceptions.InvalidDataException;
import com.company.exceptions.NotConnectedException;
import com.company.exceptions.OperationCanceledException;
import com.company.exceptions.UnknownCommandException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    private Socket client;
    private Scanner terminal;
    private Messages output;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public User user;
    DataBase dataBase;
    String command;
    String input;
    private String scriptName;
    private int recursionCounter;

    public void setUser(){
        if (this.user.getNew()) {
            this.output.addObject(Commands.SIGN_UP);
            this.output.addObject(this.user);
        }
        else{
            this.output.addObject(Commands.SIGN_IN);
            this.output.addObject(this.user);
        }
    }

    public void setOutputUser() {
        this.output = new Messages();
        this.output = output;
    }

    public Scanner getTerminal() {
        return terminal;
    }

    public Client (DataBase dataBase){
        this.terminal = new Scanner(System.in);
        this.dataBase = dataBase;
        this.user = new User();
        command = null;
        input = null;
        output = new Messages();
        this.recursionCounter = 0;
        this.scriptName = "";
    }

    public boolean connectToServer(){
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

        return true;
    }

    public String sendMessage() throws Exception{
        String feedback = null;
        Objects.requireNonNull(out).writeObject(this.output);
        try {
            this.out.flush();
        } catch (Exception e) {
            System.out.println("Error while sending a message: " + e.getMessage());
            throw e;
        }
        try {
            feedback = (String) ((Messages) in.readObject()).getObject(0);
        } catch (Exception e) {
            System.out.println("Error while getting feedback: " + e.getMessage());
            throw e;
        }
        out.reset();
        this.output.clear();
        return feedback;
    }

    public void fillFromFile(){
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
                    executeScriptCommand(command);
                    return;
                case EXIT:
                    this.output.addObject(c);
                    try {
                        sendMessage();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    System.exit(1);
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



    public void readCommand1() throws NotConnectedException {
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
                    this.output.addObject(user);
                    break;
                case CLEAR:
                    if (Terminal.binaryChoice("clear the database")){
                        this.output.addObject(c);
                        this.output.addObject(user);
                    } else {
                        System.out.println("Operation cancelled");
                    }
                    break;
                case ADD:
                case ADD_IF_MAX:
                    this.output.addObject(c);
                    Worker.WorkerBuilderFromTerminal wb = new Worker.WorkerBuilderFromTerminal();
                    this.output.addObject(wb.build());
                    this.output.addObject(user);
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
                        this.output.addObject(user);
                    } else {
                        System.out.println("Operation cancelled");
                        return;
                    }
                    break;
                case EXECUTE_SCRIPT:
                    executeScriptCommand(command);
                    //this.output.addObject(user);
                    return;
                case EXIT:
                    this.output.addObject(c);
                    this.output.addObject(null);
                    this.output.addObject(user);
                    try {
                        sendMessage();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    System.exit(1);
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
                        this.output.addObject(user);
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
                    this.output.addObject(user);

                    break;
                case FILTER_GREATER_THAN_START_DATE:
                    command = Terminal.removeString(command, "filter_greater_than_start_date");
                    if (!command.matches("\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
                        System.out.println("Invalid date format. Operation cancelled");
                        return;
                    }

                    this.output.addObject(c);
                    this.output.addObject(command);
                    this.output.addObject(user);

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
            Messages messages =  new Messages();
            //System.out.println("Message recieved");
            messages = this.sendMessage1();

            //System.out.println(messages.getObject(0));
            if (messages.getObject(0).equals(Commands.NO_FEEDBACK)){
                System.out.println(messages.getObject(1));
            } else {
                //System.out.println("needs feedback");
                if (messages.getObject(0).equals(Commands.SIGN_UP) && messages.getObject(1).equals(true)){
                    System.out.println("You successfully entered database");
                }
            }
        } catch (Exception e) {
            throw new NotConnectedException();
        }
        System.out.println("------------------------------------");
    }

    public Messages sendMessage1() throws Exception{
        Messages feedback = null;
        Objects.requireNonNull(out).writeObject(this.output);
        try {
            this.out.flush();
        } catch (Exception e) {
            System.out.println("Error while sending a message: " + e.getMessage());
            throw e;
        }
        try {
            feedback = (Messages) in.readObject();
        } catch (Exception e) {
            System.out.println("Error while getting feedback: " + e.getMessage());
            throw e;
        }
        out.reset();
        this.output.clear();
        //System.out.println(feedback.getObject(1));
        return feedback;
    }

    protected void updateByIdCommand(Commands command, String commandWithID){

        commandWithID = Terminal.removeString(commandWithID, "update");

        //abort if id is empty
        if (commandWithID.isEmpty() || !commandWithID.matches("\\d+")) {
            System.out.println("Invalid id format. Operation cancelled");
            return;
        }

        int id = Integer.parseInt(commandWithID);

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

        Messages response;
        try {
            response = (Messages) this.in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        //aborting if response is string (== error)
        if (response.getObject(0).getClass().equals(String.class)){
            System.out.println(response.getObject(0));
            return;
        }

        //updating worker
        try {
            this.output.addObject(
                    this.dataBase.updateElement(
                            (Worker)(response.getObject(0)
                        )
                    )
            );
        } catch (OperationCanceledException e) {
            System.out.println(e.getMessage());
            return;
        }

        //sending worker
        try {
            System.out.println(this.out);
            System.out.println(sendMessage1());
        } catch (Exception ignored) {

        }
    }

    public void executeScriptCommand(String commandWithFilename){
        //removing spaces and "remove" word to turn into long
        commandWithFilename = Terminal.removeString(commandWithFilename, "execute_script") + ".txt";

        //catching recursion
        if (this.scriptName.equals(commandWithFilename)){
            this.recursionCounter++;
        } else {
            this.scriptName = commandWithFilename;
            this.recursionCounter = 0;
        }

        //stopping if recursion detected
        if (this.recursionCounter > 10){
            System.out.println("Executing stopped to avoid stack overflow");
            this.scriptName = commandWithFilename;
            this.recursionCounter = 0;
            this.terminal = new Scanner(System.in);
            Terminal.changeScanner(this.terminal);
            return;
        }

        //new file and check if it exist
        File f = new File(commandWithFilename);
        if ( FileParser.alreadyExistCheck(commandWithFilename)){
            try {
                //changing terminal scanner on file's
                //IMPORTANT: THE LINK TO DATABASE'S SCANNER IS GIVEN, NOT NEW
                this.terminal = new Scanner(f);
                Terminal.changeScanner(this.terminal);
                while (this.terminal.hasNext()) {
                    try {
                        readCommand();
                    } catch (NotConnectedException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("File not found. Operation cancelled");
            return;
        }
        //chaging terminal back
        this.terminal = new Scanner(System.in);
        Terminal.changeScanner(this.terminal);
    }
}

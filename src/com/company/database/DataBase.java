package com.company.database;

import com.company.classes.Person;
import com.company.classes.WorkerBuilder;
import com.company.enums.Fields;
import com.company.classes.Worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

import com.company.enums.Commands;
import com.company.exceptions.InvalidDataException;
import com.company.exceptions.OperationCanceled;
import com.company.exceptions.UnknownCommandException;

public class DataBase {
    private LinkedList<Worker> database;
    private Scanner terminal;
    private String scriptName;
    private int recursionCounter;

    //check booleans
    private boolean isInitialized;

    private ZonedDateTime initializationTime;

    public DataBase(){}

    //public methods

    public void initialize(String filePath){
        //initializing variables
        this.database = new LinkedList<>();
        this.terminal = new Scanner(System.in);
        this.initializationTime = ZonedDateTime.now();
        this.recursionCounter = 0;
        this.scriptName = "";
        this.isInitialized = true;

        System.out.println("Database has been initialized");
        System.out.println("------------------------------------");

        //reading from file and then from terminal
        readFromFile(filePath);
        readFromTerminal();
    }

    //the old readFromTerminal
    /*public void readFromTerminal() {
        //cancelling if not initialized
        if(!isInitialized){
            System.out.println("DataBase hasn't been initialized! Cancelling...");
            return;
        }

        //reading from terminal and checking if command exist
        String command;
        while(true) {
            //check when we read from file
            if (!terminal.hasNext()){
                return;
            }
            command = terminal.nextLine();
            command = command.toLowerCase();

            //todo fix regexes
            if (command.matches("\\s*help\\s*\\w*")) {
                help();
            }
            else if (command.matches("\\s*info\\s*\\w*")) {
                info();
            }
            else if (command.matches("\\s*show\\s*\\w*")) {
                show();
            }
            else if (command.matches("\\s*add\\s*\\w*")) {
                try {
                    add();
                } catch (OperationCanceled operationCanceled) {
                    System.out.println(operationCanceled.getMessage());
                }
            }
            else if (command.matches("\\s*update\\s+[0-9]+\\s*")){
                updateById(command);
            }
            else if (command.matches("\\s*remove_by_id\\s+[0-9]+\\s*")){
                remove(command);
            }
            else if (command.matches("\\s*clear\\s*\\w*")){
                clear();
            }
            else if (command.matches("\\s*save\\s*\\w*")){
                save();
            }
            else if (command.matches("\\s*execute_script\\s+\\w+\\s*")){
                executeScript(command);
            }
            else if (command.matches("\\s*exit\\s*\\w*")) {
                System.out.println("Exiting...");
                return;
            }
            else if (command.matches("\\s*")) {
                ;//do nothing if spaces are typed in
            }
            else {
                System.out.println("Invalid command");
            }
        }
    }*/

    public void readFromTerminal(){
        //cancelling if not initialized
        if(!isInitialized){
            System.out.println("DataBase hasn't been initialized! Cancelling...");
            return;
        }

        //reading from terminal and checking if command exist
        String command;
        while(true) {
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

                switch (c){
                    case HELP:
                        help();
                        break;
                    case INFO:
                        info();
                        break;
                    case SHOW:
                        show();
                        break;
                    case ADD:
                        try {
                            add();
                        } catch (OperationCanceled operationCanceled) {
                            System.out.println(operationCanceled.getMessage());
                        }
                        break;
                    case UPDATE:
                        updateById(command);
                        break;
                    case REMOVE_BY_ID:
                        remove(command);
                        break;
                    case CLEAR:
                        clear();
                        break;
                    case SAVE:
                        save();
                        break;
                    case EXECUTE_SCRIPT:
                        executeScript(command);
                        break;
                    case EXIT:
                        System.exit(1);
                    case ADD_IF_MAX:
                        //todo
                    case REMOVE_GREATER:
                        //todo
                    case REMOVE_LOWER:
                        //todo
                    case GROUP_COUNTING_BY_POSITION:
                        //todo
                    case COUNT_LESS_THAN_START_DATE:
                        //todo
                    case FILTER_GREATER_THAN_START_DATE:
                        //todo
                }
            } catch (UnknownCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void readFromFile(String filePath){
        //cancelling if not initialized
        if(!isInitialized){
            System.out.println("DataBase hasn't been initialized! Cancelling...");
            return;
        }

        //parsing
        try {
            this.database = FileParser.xmlToDatabase(filePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //protected methods

    protected int returnIndexById(long id){
        //todo change on exception
        int index = -1;
        for (int i = 0; i < database.size(); i++) {
            if (database.get(i).getId() == id){
                index = i;
                break;
            }
        }
        return index;
    }

    protected void updateFields(int index){
        //checking if element exists
        if (database.get(index) == null || index > database.size()){
            System.out.println("Invalid index!");
            return;
        }

        //todo write loop
        //choosing the field to update
        System.out.println("Which fields would you like to update: " + Arrays.toString(Arrays.stream(Fields.getFields()).toArray()) + " ?");
        String choice;
        while (!Fields.isEnum(choice = terminal.nextLine())){
            System.out.println("Incorrect field. Try again: ");
        }
        Fields field = Fields.findEnum(choice);
        switch (field){
            case NAME:
                System.out.println("Please, type the new name: ");
                try {
                    database.get(index).setName(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("name", "\\s*\\w+\\s*")) );
                } catch (InvalidDataException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case SALARY:
                System.out.println("Please, type the new salary: ");
                try {
                    database.get(index).setSalary(Double.parseDouble(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("salary", "\\s*\\d+\\.*\\d*\\s*"))));
                } catch (InvalidDataException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case POSITION:
                //todo write func to compare and get enum
                System.out.println("Please, type the new position: ");
                break;
            case PERSONALITY:
                //todo
                break;
        }
        System.out.println("Worker was successfully updated!");
    }

    //terminal commands

    protected void updateById(String commandWithID){
        //removing spaces and "update" word to turn into long
        commandWithID = Terminal.removeString(commandWithID, "update");
        long id = Long.parseLong(commandWithID);

        //trying to find element
        if (returnIndexById(id) != -1){
            updateFields(returnIndexById(id));
            } else {
            System.out.println("Element not found");
        }
    }

    protected void help(){
        System.out.println("------------------------------------");
        System.out.println("Commands: ");
        for (int i = 0; i < Commands.values().length; i++) {
            System.out.println(" " + Commands.getCommandsWithDescriptions()[i]);
        }
        System.out.println("------------------------------------");
    }

    protected void add() throws OperationCanceled{
        WorkerBuilder wb = new WorkerBuilder();

        Worker w = null;
        try {
            w = wb.newWorker();
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
            if (Terminal.binaryChoice("try to add worker again")){
                add();
            } else throw new OperationCanceled();
        }

        //adding to database
        this.database.add(w);
        System.out.println("New worker was successfully added!");
    }

    protected void show(){
        //checking if database is empty
        if (database.isEmpty()){
            System.out.println("Database is empty");
            return;
        }
        System.out.println("------------------------------------");
        System.out.println("Name | id | Salary | Position | Personality | Coordinates | Dates");
        String sout;
        StringBuilder sb = new StringBuilder();
        for (Worker worker : database) {
            sb.append(worker.getName()).append(" | ").append(worker.getId()).append(" | ").append(worker.getSalary()).append(" | ");

            if (worker.getPosition() != null){
                sb.append(worker.getPosition().toString()).append(" | ");
            }

            if (worker.getPerson().getHeight() != null){
                sb.append(worker.getPerson().getHeight()).append(" | ");
            }
            if (worker.getPerson().getWeight() != null){
                sb.append(worker.getPerson().getWeight()).append(" | ");
            }

            sb.append(worker.getCoordinates().getX()).append(" | ");

            System.out.println(sb.toString());
            sb.delete(0, sb.length());
        }
        System.out.println("------------------------------------");
    }

    protected void info(){
        System.out.println("------------------------------------");
        System.out.println("Type: Linked List");
        System.out.println("Initialization date: " + initializationTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)));
        System.out.println("Number of Workers: " + this.database.size());
        System.out.println("------------------------------------");
    }

    protected void clear(){
        //asking if user really wants to clear the database
        if ( Terminal.binaryChoice("clear the database") ){
            database.clear();
            System.out.println("The database was successfully cleared");
        } else {
            System.out.println("Operation cancelled");
        }
    }

    protected void save(){
        //input file name
        System.out.print("Please, type the name of a new file: ");
        String newFilename = Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("filename", "\\s*\\w+\\s*")) + ".xml";

        //checking if user wants to overwrite existing file
        //if not - throw exception == canceling
        FileParser.overWriteFile(newFilename);

        FileParser.dataBasetoXML(FileParser.dataBaseToString(this.database), newFilename);
    }

    protected void remove(String commandWithID){
        //removing spaces and "remove" word to turn into long
        commandWithID = Terminal.removeString(commandWithID, "remove_by_id");
        long id = Long.parseLong(commandWithID);

        //trying to find element
        if (returnIndexById(id) != -1){
            if (Terminal.binaryChoice("delete worker")){
                this.database.remove(returnIndexById(id));
                System.out.println("Worker was successfully deleted from the database");
            } else {
                System.out.println("Operation canceled");
            }
        } else {
            System.out.println("Element not found");
        }
    }

    protected void executeScript(String commandWithFilename){
        //removing spaces and "remove" word to turn into long
        commandWithFilename = Terminal.removeString(commandWithFilename, "execute_script") + ".txt";

        if (this.scriptName.equals(commandWithFilename)){
            this.recursionCounter++;
        } else {
            this.scriptName = commandWithFilename;
            this.recursionCounter = 0;
        }

        if (this.recursionCounter > 10){
            System.out.println("Executing stopped to avoid stack overflow");
            this.scriptName = commandWithFilename;
            this.recursionCounter = 0;
            this.terminal = new Scanner(System.in);
            Terminal.changeScanner(this.terminal);
            readFromTerminal();
            return;
        }

        File f = new File(commandWithFilename);
        if ( FileParser.alreadyExistCheck(commandWithFilename)){
            try {
                System.out.println("It reads");
                this.terminal = new Scanner(f);
                Terminal.changeScanner(this.terminal);
                while (this.terminal.hasNext()) {
                    readFromTerminal();
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("It stopped");
        this.terminal = new Scanner(System.in);
        Terminal.changeScanner(this.terminal);
        readFromTerminal();
    }
}

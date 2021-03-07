package com.company.database;

import com.company.enums.Fields;
import com.company.enums.Position;
import com.company.classes.Worker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.w3c.dom.*;

import javax.xml.parsers.*;

public class DataBase {
    private LinkedList<Worker> database;

    private Path filePath;
    private Scanner terminal;

    //check booleans
    private boolean isInitialized;
    private boolean isSaved;

    private ZonedDateTime initializationTime;

    public DataBase(){}

    //public methods

    public void initialize(String filePath){
        //initializing variables
        this.database = new LinkedList<>();
        this.terminal = new Scanner(System.in);
        this.initializationTime = ZonedDateTime.now();
        this.isInitialized = true;

        System.out.println("Database has been initialized");
        System.out.println("------------------------------------");

        //reading from file and then from terminal
        readFromFile(filePath);
        readFromTerminal();
    }

    //todo to terminal
    public void readFromTerminal() {
        //cancelling if not initialized
        if(!isInitialized){
            System.out.println("DataBase hasn't been initialized! Cancelling...");
            return;
        }

        //reading from terminal and checking if command exist
        String command;
        loop: while(true) {
            command = terminal.nextLine();
            command = command.toLowerCase();
            if (command.matches("\\s*help\\s*\\w*")) {
                help();
            }
            else if (command.matches("\\s*info\\s*\\w*")) {
                info();
            }
            else if (command.matches("\\s*show\\s*\\w*")) {
                show();
            }
            else if (command.matches("\\s*exit\\s*\\w*")) {
                System.out.println("Exiting..."); //todo haven't saved changes
                break loop;
            }
            else if (command.matches("\\s*add\\s*\\w*")) {
                addWorker();
            }
            else if (command.matches("\\s*update\\s+\\w+\\s*\\w*")){
                updateById(command);
            }
            else if (command.matches("\\s*clear\\s*\\w*")){
                clear();
            }
            else if (command.matches("\\s*save\\s*\\w*")){
                try {
                    save();
                } catch (OperationCanceled operationCanceled) {
                    System.out.println(operationCanceled.getMessage());
                }
            }
            else if (command.matches("\\s*")) {
                ;//do nothing if spaces are typed in
            }
            else {
                System.out.println("Invalid command");
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

    protected void addWorker(){
        //input block
        System.out.print("PLease, write the name of a new worker: ");
        String name = Terminal.removeSpaces(
                Terminal.repeatInputAndExpectRegex("name", "\\s*\\w+\\s*")
        );

        System.out.print("PLease, input " + name + "'s salary: ");
        double salary = Double.parseDouble(
                Terminal.removeSpaces(
                        Terminal.repeatInputAndExpectRegex("salary", "\\s*\\d+\\.*\\d*\\s*"))
        );

        //todo other fields choice

        //adding to database
        try {
            this.database.add(new Worker(name, salary));
            System.out.println("New worker was successfully added!");
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
            System.out.println("Couldn't add worker");
        }
    }

    protected void show(){
        //checking if database is empty
        if (database.isEmpty()){
            System.out.println("Database is empty");
            return;
        }
        System.out.println("------------------------------------");
        System.out.println("Worker name | Worker's id | Worker's salary");
        for (int i = 0; i < database.size(); i++) {
            System.out.println(database.get(i).getName() + " " + database.get(i).getId() + " " + database.get(i).getSalary());
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
        try {
            //asking if user really wants to clear the database
            if ( Terminal.binaryChoice("clear the database") ){
                database.clear();
                System.out.println("The database was successfully cleared");
            } else {
                System.out.println("Operation cancelled");
            }
        } catch (UnknownCommandException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void save() throws OperationCanceled{
        //input file name
        System.out.print("Please, type the name of a new file: ");
        String newFilename = Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("filename", "\\s*\\w+\\s*")) + ".xml";

        //checking if user wants to overwrite existing file
        //if not - throw exception == canceling
        FileParser.overWriteFile(newFilename);

        FileParser.dataBasetoXML(FileParser.dataBaseToString(this.database), newFilename);
    }
}

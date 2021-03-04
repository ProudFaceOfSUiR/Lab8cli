package com.company.database;

import com.company.enums.Position;
import com.company.classes.Worker;

import java.io.File;
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
        this.database = new LinkedList<>();
        this.terminal = new Scanner(System.in);
        this.initializationTime = ZonedDateTime.now();
        this.isInitialized = true;

        System.out.println("Database has been initialized");

        readFromFile(filePath);
        readFromTerminal();
    }

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
            else if (command.matches("\\s*readfromfile\\s+(\\w|[/:.])+\\s*\\w*")){
                System.out.println("Initial command: " + command);
                command = removeCommand("readfromfile");
                System.out.println("After parsing: " + command);
                readFromFile(command);
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

        //checking if path is correct
        if (!filePath.matches("\\s*[A-Z]:\\/(\\w*\\/)*\\w+.xml")){
            System.out.println(filePath);
            System.out.println("Invalid path. Try to read from file using ReadFromFile command");
            return;
        }
        //check if exist
        Path p = Paths.get(filePath);
        if (Files.notExists(p)){
            System.out.println("File doesn't exist! Try to read from file using ReadFromFile command");
            return;
        } else this.filePath = p;

        //check permission to read
        if (!Files.isReadable(this.filePath)){
            System.out.println("File is restricted from editing. Aborting...");
            return;
        }

        //parsing
        xmlParser(new File(String.valueOf(this.filePath)));
    }

    //protected methods

    protected int returnIndexById(long id){
        int index = -1;
        for (int i = 0; i < database.size(); i++) {
            if (database.get(i).getId() == id){
                index = i;
                break;
            }
        }
        return index;
    }

    protected void xmlParser(File file){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("worker");

            String name;
            double salary;
            String positionString;
            Position position;

            int successfullyAddedWorkers = 0;

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    name = eElement.getElementsByTagName("name").item(0).getTextContent();

                    if (name.matches("\\s*")){
                        System.out.println("Invalid name. Couldn't add worker");
                        continue;
                    }

                    if (eElement.getElementsByTagName("salary").item(0).getTextContent().matches("\\s*[0-9]+.*[0-9]*\\s*")){
                        salary = Double.parseDouble(eElement.getElementsByTagName("salary").item(0).getTextContent() );
                    } else {
                        System.out.println(name + "'s salary is invalid. Couldn't add worker");
                        continue;
                    }

                    positionString = eElement.getElementsByTagName("position").item(0).getTextContent();

                    if (Position.findEnum(positionString) != null){
                        position = Position.findEnum(positionString);
                        this.database.add(new Worker(name, salary, position));
                        successfullyAddedWorkers++;
                    } else {
                        System.out.println(name + "'s position is invalid: " + positionString);
                        System.out.println("Worker was added without position");
                        this.database.add(new Worker(name, salary));
                        successfullyAddedWorkers++;
                    }
                }
            }

            System.out.println("DataBase has been successfully filled with " + successfullyAddedWorkers + " workers");
        } catch (Exception e) {
            System.out.println("Something went wrong :0");
        }
    }

    protected void updateFields(int index){
        if (database.get(index) == null || index > database.size()){
            System.out.println("Invalid index!");
        } else {
            System.out.println("Which fields would you like to update: " + Arrays.stream(Position.getPositions()).toArray() + " ?");
            //todo
        }
    }

    protected boolean binaryChoice(String move) throws UnknownCommandException {
        System.out.println("Do you want to " + move + "? (Yes/No)");
        String command = terminal.nextLine();
        //
        command = command.toUpperCase();
        if (command.matches("\\s*YES\\s*\\w*\\s*")){
            return true;
        } else if (command.matches("\\s*NO\\s*\\w*\\s*")){
            return false;
        } else {
            throw new UnknownCommandException();
        }
    }

    protected String removeCommand(String command){
        for (String s : Arrays.asList(command, " ")) {
            System.out.println(command);
            command = command.replace(s, "");
        }
        return command;
    }

    //terminal commands

    protected void updateById(String commandWithID){
        //removing spaces and "update" word to turn into long
        commandWithID = removeCommand("update");
        long id = Long.parseLong(commandWithID);

        //trying to find element
        if (returnIndexById(id) != -1){
            updateFields(returnIndexById(id));
        } else {
            System.out.println("Element not found");
        }
    }

    protected void help(){
        System.out.println("Commands: ");
        for (int i = 0; i < Commands.values().length; i++) {
            System.out.println(" " + Commands.getCommandsWithDescriptions()[i]);
        }
    }

    protected void addWorker(){
        //input block
        System.out.print("PLease, write the name of a new worker: ");
        String name = terminal.nextLine();
        while (!name.matches("\\s*\\w+\\s*")){
            System.out.print("Invalid name. Please, try again: ");
            name = terminal.nextLine();
        }
        name = name.replaceAll("\\s*",""); //deleting whitespaces

        System.out.print("PLease, input " + name + "'s salary: ");
        double salary;
        String temp = terminal.nextLine();
        while (!temp.matches("\\s*\\d+\\.*\\d*\\s*")){
            System.out.print("Invalid salary. Please, try again: ");
            temp = terminal.nextLine();
        }
        salary = Double.valueOf(temp);

        System.out.println("New worker was successfully added!");

        //adding to database
        this.database.add(new Worker(name, salary));
    }

    protected void show(){
        if (database.isEmpty()){
            System.out.println("Database is empty");
            return;
        }
        System.out.println("Worker name | Worker's id | Worker's salary");
        for (int i = 0; i < database.size(); i++) {
            System.out.println(database.get(i).getName() + " " + database.get(i).getId() + " " + database.get(i).getSalary());
        }
    }

    protected void info(){
        System.out.println("Type: Linked List");
        System.out.println("Initialization date: " + initializationTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)));
        System.out.println("Number of Workers: " + this.database.size());
    }

    protected void clear(){
        try {
            if ( binaryChoice("clear the database") ){
                database.clear();
            }
        } catch (UnknownCommandException e) {
            System.out.println("Unknown command. Operation canceled");
        }
    }

    protected void save(){

    }
}
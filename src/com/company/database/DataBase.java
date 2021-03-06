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
        System.out.println("------------------------------------");

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
            else if (command.matches("\\s*save\\s*\\w*")){
                save();
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
            System.out.println("Invalid path. Couldn't get file");
            return;
        }
        //check if exist
        Path p = Paths.get(filePath);
        if (Files.notExists(p)){
            System.out.println("File doesn't exist!");
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
            System.out.println("------------------------------------");
        } catch (Exception e) {
            System.out.println("Something went wrong :0");
        }
    }

    protected void updateFields(int index){
        if (database.get(index) == null || index > database.size()){
            System.out.println("Invalid index!");
            return;
        }

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
                    database.get(index).setName(removeString(repeatInputAndExpectRegex("name", "\\s*\\w+\\s*"),"\\s*") );
                } catch (InvalidDataException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case SALARY:
                System.out.println("Please, type the new salary: ");
                try {
                    database.get(index).setSalary(Double.parseDouble(removeString(repeatInputAndExpectRegex("salary", "\\s*\\d+\\.*\\d*\\s*"),"\\s*")));
                } catch (InvalidDataException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case POSITION:
                //todo
                System.out.println("Please, type the new position: ");
                break;
            case PERSONALITY:
                //todo
                break;
        }
        System.out.println("Worker was successfully updated!");
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

    protected String removeString(String input, String string){
        String output = input;
        for (String s : Arrays.asList(string, " ", "\t")) {
            output = output.replace(s, "");
        }
        return output;
    }

    protected String repeatInputAndExpectRegex(String waitFor, String expectedRegex){
        String output = terminal.nextLine();
        while (!output.matches(expectedRegex)){
            System.out.println("Incorrect " + waitFor + " Please, try again: ");
            output = terminal.nextLine();
        }
        return output;
    }

    protected String dataBaseToXMLString(){
        StringBuilder sb = new StringBuilder();

        //writing preamble
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
        sb.append("<database>").append("\n");

        for (Worker w: database) {
            sb.append("\t").append("<worker>").append("\n");

            sb.append("\t\t").append("<name>").append(w.getName()).append("</name>").append("\n");
            sb.append("\t\t").append("<salary>").append(w.getSalary()).append("</salary>").append("\n");

            if (w.getPosition() != null){
                sb.append("\t\t").append("<position>").append(w.getPosition().toString()).append("</position>").append("\n");
            }

            sb.append("\t").append("</worker>").append("\n");
        }

        sb.append("</database>");
        return sb.toString();
    }

    //terminal commands

    protected void updateById(String commandWithID){
        //removing spaces and "update" word to turn into long
        commandWithID = removeString(commandWithID, "update");
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
        String name = removeString(
                repeatInputAndExpectRegex("name", "\\s*\\w+\\s*")
                ," "
        );

        System.out.print("PLease, input " + name + "'s salary: ");
        double salary = Double.parseDouble(
                removeString(
                        repeatInputAndExpectRegex("salary", "\\s*\\d+\\.*\\d*\\s*")
                        ," ")
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
            if ( binaryChoice("clear the database") ){
                database.clear();
            }
        } catch (UnknownCommandException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void save(){
        //input file name
        System.out.print("Please, type the name of a new file: ");
        String newFilename = removeString(repeatInputAndExpectRegex("filename", "\\s*\\w+\\s*"), " ");

        //checking if file already exist
        File f = new File(newFilename + ".xml");
        if(f.exists() && !f.isDirectory()) {
            try {
                //asking if user wants to overwrite it
                if (!binaryChoice("overwrite the existing file")){
                    System.out.println("Operation cancelled");
                    return;
                }
            } catch (UnknownCommandException e) {
                System.out.println(e.getMessage());
            }
        }

        try {
            // Creates a FileWriter
            FileWriter file = new FileWriter(newFilename + ".xml");

            // Creates a BufferedWriter
            BufferedWriter buffer = new BufferedWriter(file);

            String output = dataBaseToXMLString();

            buffer.write(output);
            buffer.flush();

            System.out.println("Databse was successfully saved to a new file!");
            buffer.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

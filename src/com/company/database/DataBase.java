package com.company.database;

import com.company.classes.WorkerBuilder;
import com.company.enums.Fields;
import com.company.classes.Worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

import com.company.enums.Commands;
import com.company.enums.Position;
import com.company.exceptions.InvalidDataException;
import com.company.exceptions.OperationCanceledException;
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
                        add();
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
                        removeGreater(command);
                        break;
                    case REMOVE_LOWER:
                        removeLower(command);
                        break;
                    case GROUP_COUNTING_BY_POSITION:
                        groupCountingByPosition();
                        break;
                    case COUNT_LESS_THAN_START_DATE:
                        countLessThanStartDate(command);
                        break;
                    case FILTER_GREATER_THAN_START_DATE:
                        filterGreaterThanStartDate(command);
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            } catch (UnknownCommandException | OperationCanceledException e) {
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
            LinkedList<Worker> databaseFromXML = FileParser.xmlToDatabase(filePath);
            if (databaseFromXML != null){
                this.database = databaseFromXML;
            }
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

    //todo catch EOF
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
                } catch (InvalidDataException | OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case SALARY:
                System.out.println("Please, type the new salary: ");
                try {
                    database.get(index).setSalary(Double.parseDouble(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("salary", "\\s*\\d+\\.*\\d*\\s*"))));
                } catch (InvalidDataException | OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case POSITION:
                System.out.println("Please, type the new position: ");
                Position newPosition;
                try {
                    newPosition = Position.findEnum(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("position", "\\s*\\w+\\s*")));
                } catch (OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                this.database.get(index).setPosition(newPosition);
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

    protected void add() throws OperationCanceledException {
        WorkerBuilder wb = new WorkerBuilder();

        Worker w = null;
        try {
            w = wb.newWorker();
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
            if (Terminal.binaryChoice("try to add worker again")){
                add();
            } else throw new OperationCanceledException();
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
        try {
            if ( Terminal.binaryChoice("clear the database") ){
                database.clear();
                System.out.println("The database was successfully cleared");
            } else {
                System.out.println("Operation cancelled");
            }
        } catch (OperationCanceledException e) {
            //catch EOF
            System.out.println(e.getMessage());
            return;
        }
    }

    protected void save(){
        //input file name
        System.out.print("Please, type the name of a new file: ");
        String newFilename = null;
        try {
            newFilename = Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("filename", "\\s*\\w+\\s*")) + ".xml";
        } catch (OperationCanceledException e) {
            System.out.println(e.getMessage());
            return;
        }

        //checking if user wants to overwrite existing file
        //if not - throw exception == canceling
        try {
            FileParser.overWriteFile(newFilename);
        } catch (OperationCanceledException e) {
            System.out.println(e.getMessage());
            return;
        }

        FileParser.dataBasetoXML(FileParser.dataBaseToString(this.database), newFilename);
    }

    protected void remove(String commandWithID){
        //removing spaces and "remove" word to turn into long
        commandWithID = Terminal.removeString(commandWithID, "remove_by_id");
        long id = Long.parseLong(commandWithID);

        //trying to find element
        if (returnIndexById(id) != -1){
            try {
                if (Terminal.binaryChoice("delete worker")){
                    this.database.remove(returnIndexById(id));
                    System.out.println("Worker was successfully deleted from the database");
                } else {
                    System.out.println("Operation canceled");
                }
            } catch (OperationCanceledException e) {
                //catching EOF
                System.out.println(e.getMessage());
                return;
            }
        } else {
            System.out.println("Element not found");
        }
    }

    protected void executeScript(String commandWithFilename){
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
            readFromTerminal();
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
                    readFromTerminal();
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        //chaging terminal back
        this.terminal = new Scanner(System.in);
        Terminal.changeScanner(this.terminal);
        //continue reading
        readFromTerminal();
    }

    protected void removeGreater(String commandWithSalary){
        //removing spaces and "update" word to turn into long
        commandWithSalary = Terminal.removeString(commandWithSalary, "remove_greater");
        double salary = Double.parseDouble(commandWithSalary);

        for (int i = 0; i < this.database.size(); i++) {
            if (this.database.get(i).getSalary() > salary){
                this.database.remove(i);
            }
        }

        System.out.println("Workers with salary greater " + salary + " were successfully removed!");
    }

    protected void removeLower(String commandWithSalary){
        //removing spaces and "update" word to turn into long
        commandWithSalary = Terminal.removeString(commandWithSalary, "remove_lower");
        double salary = Double.parseDouble(commandWithSalary);

        for (int i = 0; i < this.database.size(); i++) {
            if (this.database.get(i).getSalary() < salary){
                this.database.remove(i);
            }
        }

        System.out.println("Workers with salary lower " + salary + " were successfully removed!");
    }

    protected void groupCountingByPosition(){
        StringBuilder sb = new StringBuilder();
        for (Position p: Position.values()) {
            System.out.println("-----------" + p.toString() + "-----------");
            for (Worker worker : this.database) {
                if (worker.getPosition() != null) {
                    if (worker.getPosition().equals(p)){
                        sb.append(worker.getName()).append(" ").append(worker.getId());
                    }
                }
                System.out.println(sb.toString());
                sb.delete(0, sb.length());
            }
        }
    }

    protected void countLessThanStartDate(String commandWithStartDate){
        //removing spaces and "count_less_than_start_date" word to turn into date
        commandWithStartDate = Terminal.removeString(commandWithStartDate, "count_less_than_start_date");
        if (!commandWithStartDate.matches("\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
            System.out.println("Invalid date!");
            return;
        }

        ZonedDateTime z = ZonedDateTime.parse(commandWithStartDate);

        int counter = 0;
        for (Worker w:this.database) {
            if (w.getStartDate().isBefore(z)){
                counter++;
            }
        }
        System.out.println("There are " + counter + " workers with StartDate less than " + commandWithStartDate);
    }

    protected void filterGreaterThanStartDate(String commandWithStartDate){
        //removing spaces and "count_less_than_start_date" word to turn into date
        commandWithStartDate = Terminal.removeString(commandWithStartDate, "count_less_than_start_date");
        if (!commandWithStartDate.matches("\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
            System.out.println("Invalid date!");
            return;
        }

        ZonedDateTime z = ZonedDateTime.parse(commandWithStartDate);

        System.out.println("-----Workers with date after " + commandWithStartDate + " -----");
        for (Worker w:this.database) {
            if (w.getStartDate().isAfter(z)){
                System.out.println(w.getName() + " " + w.getId());
            }
        }
        System.out.println("-------------------------");
    }
}

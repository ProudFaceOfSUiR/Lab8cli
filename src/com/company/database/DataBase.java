package com.company.database;

import com.company.classes.Coordinates;
import com.company.classes.Person;
import com.company.enums.Fields;
import com.company.classes.Worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import com.company.enums.Commands;
import com.company.enums.Position;
import com.company.exceptions.InvalidDataException;
import com.company.exceptions.OperationCanceledException;
import com.company.exceptions.UnknownCommandException;

public class DataBase implements Serializable {

    private static final long serialVersionUID = 40L;

    private LinkedList<Worker> database;
    private Scanner terminal;
    private String scriptName;
    private int recursionCounter;

    //check booleans
    private boolean isInitialized;

    private ZonedDateTime initializationTime;

    public DataBase(){}

    //public methods

    /**
     * Initializing database (like constructor)
     * @param filePath
     */
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

        //reading from file
        readFromFile(filePath);
    }

    /**
     * Initializing database (like constructor), but without a file (if it's not given)
     */
    public void initialize(){
        //initializing variables
        this.database = new LinkedList<>();
        this.terminal = new Scanner(System.in);
        this.initializationTime = ZonedDateTime.now();
        this.recursionCounter = 0;
        this.scriptName = "";
        this.isInitialized = true;

        System.out.println("Database has been initialized without file");
        System.out.println("------------------------------------");
    }

    /**
     * Reader from given file.
     * @param filePath
     */
    public void readFromFile(String filePath){
        //cancelling if not initialized
        if(!isInitialized){
            System.out.println("DataBase hasn't been initialized! Cancelling...");
            return;
        }

        //parsing
        try {
            LinkedList<Worker> databaseFromXML = FileParser.xmlToDatabase(filePath);

            if (!databaseFromXML.isEmpty()){
                this.database = databaseFromXML;
            } else {
                System.out.println("Database wasn't filled.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public LinkedList<Worker> getDatabase(){
        return this.database;
    }


    /**
     * The whole code of updating feilds
     * @param index
     * @throws OperationCanceledException
     */
    public void updateElement(int index) throws OperationCanceledException{
        //checking if element exists
        if (database.get(index) == null){
            System.out.println("Invalid index!");
            return;
        }

        //choosing the field to update
        System.out.println("Which field would you like to update: " + Arrays.toString(Arrays.stream(Fields.getFields()).toArray()) + " ?");
        String choice;
        try {
            while (!Fields.isEnum(choice = terminal.nextLine())){
                System.out.println("Incorrect field. Try again: ");
            }
        } catch (NoSuchElementException e) {
            throw new OperationCanceledException();
        }

        Fields field = Fields.findEnum(choice);

        //updating chosen field
        switch (field){
            case NAME:
                System.out.println("Please, type the new name: ");
                try {
                    database.get(index).setName(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("name", "\\s*\\w+\\s*")) );
                } catch (InvalidDataException | OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                System.out.println(field.toString() + " has been successfully updated!");
                break;
            case SALARY:
                System.out.println("Please, type the new salary: ");
                try {
                    database.get(index).setSalary(Double.parseDouble(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("salary", "\\s*\\d+\\.*\\d*\\s*"))));
                } catch (InvalidDataException | OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                System.out.println(field.toString() + " has been successfully updated!");
                break;
            case POSITION:
                System.out.println("Please, type the new position " + Arrays.toString(Position.values()) + ": ");
                Position newPosition;
                try {
                    newPosition = Position.findEnum(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegexOrNull("position", "\\s*\\w+\\s*")));
                } catch (OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                this.database.get(index).setPosition(newPosition);
                System.out.println(field.toString() + " has been successfully updated!");
                break;
            case PERSONALITY:
                System.out.println("PLease, type the new height: ");
                Person person = new Person();
                try {
                    person.setHeight(Long.valueOf(Terminal.repeatInputAndExpectRegexOrNull("height", "\\s*[0-9]+\\s*")));
                } catch (Exception e){
                    //pass, because Person is already null
                }

                System.out.println("PLease, type the new weight: ");
                try {
                    person.setWeight((int) Long.parseLong(Terminal.repeatInputAndExpectRegexOrNull("weight", "\\s*[0-9]+\\s*")));
                } catch (Exception e){
                    //pass
                }

                this.database.get(index).setPerson(person);
                System.out.println(field.toString() + " has been successfully updated!");
                break;
            case COORDINATES:
                Coordinates c = new Coordinates(0,0);

                try {
                    System.out.print("X = ");
                    c.setX(
                            Long.parseLong(Terminal.removeSpaces(
                                    Terminal.repeatInputAndExpectRegex("x coordinate", "\\s*\\d+\\s*")
                            ))
                    );
                    System.out.print("Y = ");
                    c.setY((int)
                            Long.parseLong(Terminal.removeSpaces(
                                    Terminal.repeatInputAndExpectRegex("y coordinate", "\\s*\\d+\\s*")
                            ))
                    );
                } catch (InvalidDataException | OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return;
                }

                this.database.get(index).setCoordinates(c);
                System.out.println(field.toString() + " has been successfully updated!");
                break;
            case STARTDATE:
                System.out.println("Please, write the new start day (yyyy-mm-dd): ");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date;
                try {
                    date = LocalDate.parse(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex(
                            "start day", "\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")), formatter);
                } catch (OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return;
                }

                this.database.get(index).setStartDate( date.atStartOfDay(ZoneId.systemDefault()) );
                System.out.println(field.toString() + " has been successfully updated!");
                break;
            case ENDDATE:
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                System.out.println("Please, write the new end day (yyyy-mm-dd): ");
                String enddate;
                try {
                    enddate = Terminal.removeSpaces(
                            Terminal.repeatInputAndExpectRegexOrNull("end day", "\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")
                    );
                } catch (OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                if (enddate == null){
                    this.database.get(index).setEndDate(null);
                } else {
                    date = LocalDate.parse(enddate, formatter);
                    this.database.get(index).setEndDate(date.atStartOfDay(ZoneId.systemDefault()));
                }

                System.out.println(field.toString() + " has been successfully updated!");
                break;
        }
        System.out.println("Worker was successfully updated!");
    }

    public ZonedDateTime getInitializationTime(){
        return this.initializationTime;
    }
}

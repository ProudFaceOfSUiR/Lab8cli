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

    //check booleans
    private boolean isInitialized;

    private ZonedDateTime initializationTime;

    public DataBase(){}

    //public methods



    /**
     * Initializing database (like constructor), but without a file (if it's not given)
     */
    public void initialize(){
        //initializing variables
        this.database = new LinkedList<>();
        this.terminal = new Scanner(System.in);
        this.initializationTime = ZonedDateTime.now();
        this.isInitialized = true;

        System.out.println("Client's database has been initialized.");
        System.out.println("------------------------------------");
    }

    public LinkedList<Worker> getDatabase(){
        return this.database;
    }

    public Worker updateElement(Worker workerToUpdate) throws OperationCanceledException{

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
                    workerToUpdate.setName(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("name", "\\s*\\p{L}+\\s*")) );
                } catch (InvalidDataException | OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return null;
                }
                System.out.println(field.toString() + " has been successfully updated!");
                break;
            case SALARY:
                System.out.println("Please, type the new salary: ");
                try {
                    workerToUpdate.setSalary(Double.parseDouble(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("salary", "\\s*\\d+\\.*\\d*\\s*"))));
                } catch (InvalidDataException | OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return null;
                }
                System.out.println(field.toString() + " has been successfully updated!");
                break;
            case POSITION:
                System.out.println("Please, type the new position " + Arrays.toString(Position.values()) + ": ");
                Position newPosition;
                try {
                    newPosition = Position.findEnum(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex("position", "\\s*\\w+\\s*")));
                } catch (OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return null;
                }
                workerToUpdate.setPosition(newPosition);
                System.out.println(field.toString() + " has been successfully updated!");
                break;
            case PERSONALITY:
                System.out.println("PLease, type the new height: ");
                Person person = new Person();
                try {
                    person.setHeight(Long.valueOf(Terminal.repeatInputAndExpectRegex("height", "\\s*[0-9]+\\s*")));
                } catch (Exception e){
                    //pass, because Person is already null
                }

                System.out.println("PLease, type the new weight: ");
                try {
                    person.setWeight((int) Long.parseLong(Terminal.repeatInputAndExpectRegex("weight", "\\s*[0-9]+\\s*")));
                } catch (Exception e){
                    //pass
                }

                workerToUpdate.setPerson(person);
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
                    return null;
                }

                workerToUpdate.setCoordinates(c);
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
                    return null;
                }

                workerToUpdate.setStartDate( date.atStartOfDay(ZoneId.systemDefault()) );
                System.out.println(field.toString() + " has been successfully updated!");
                break;
            case ENDDATE:
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                System.out.println("Please, write the new end day (yyyy-mm-dd): ");
                String enddate;
                try {
                    enddate = Terminal.removeSpaces(
                            Terminal.repeatInputAndExpectRegex("end day", "\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")
                    );
                } catch (OperationCanceledException e) {
                    System.out.println(e.getMessage());
                    return null;
                }
                if (enddate == null){
                    workerToUpdate.setEndDate(null);
                } else {
                    date = LocalDate.parse(enddate, formatter);
                    workerToUpdate.setEndDate(date.atStartOfDay(ZoneId.systemDefault()));
                }

                System.out.println(field.toString() + " has been successfully updated!");
                break;
        }
        return workerToUpdate;
    }
}

package com.company.classes;

import com.company.database.Terminal;
import com.company.enums.Fields;
import com.company.enums.Position;
import com.company.exceptions.InvalidDataException;
import com.company.exceptions.OperationCanceledException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

public class WorkerBuilder {

    //todo null input


    private Worker worker;

    //public methods

    public WorkerBuilder(){
        try {
            worker = new Worker("Default", 1, null, null, new Coordinates(0, 0), ZonedDateTime.now(), null);
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    public Worker newWorker() throws InvalidDataException, OperationCanceledException{
        setName();
        setSalary();
        setPosition();
        setPersonality();
        setCoordinates();
        setDates();
        return new Worker(worker.getName(),worker.getSalary(), worker.getPosition(), worker.getPerson(), worker.getCoordinates(), worker.getStartDate(), worker.getEndDate());
    }

    //protected methods for terminal input

    protected void setName() throws InvalidDataException, OperationCanceledException{
        System.out.print("Please, write the name of a new worker: ");
        worker.setName(
                Terminal.removeSpaces(
                    Terminal.repeatInputAndExpectRegex("name", "\\s*\\w+\\s*")
                )
            );
    }

    protected void setSalary() throws InvalidDataException, OperationCanceledException{
        System.out.print("PLease, input " + worker.getName() + "'s salary: ");
        worker.setSalary(
                Double.parseDouble(
                    Terminal.removeSpaces(
                        Terminal.repeatInputAndExpectRegex("salary", "\\s*\\d+\\.*\\d*\\s*"))
                )
            );
    }

    //todo invalid position
    protected void setPosition() throws OperationCanceledException{
        System.out.println("Please, write " + worker.getName() + "'s position " + Arrays.toString(Arrays.stream(Position.getPositions()).toArray()) + ": ");
        worker.setPosition(Position.findEnum(Terminal.repeatInputAndExpectRegexOrNull("position","\\s*\\w+\\s*")));
    }

    protected void setPersonality(){
        Person person = new Person();
        System.out.println("PLease, write " + worker.getName() + "'s height:");
        try {
            person.setHeight(Long.valueOf(Terminal.repeatInputAndExpectRegexOrNull("height", "\\s*[0-9]+\\s*")));
        } catch (Exception e){
            //pass, because Person is already null
        }

        System.out.println("PLease, write " + worker.getName() + "'s weight:");
        try {
            person.setWeight((int) Long.parseLong(Terminal.repeatInputAndExpectRegexOrNull("weight", "\\s*[0-9]+\\s*")));
        } catch (Exception e){
            //pass
        }
        this.worker.setPerson(person);
    }

    protected void setCoordinates() throws InvalidDataException, OperationCanceledException{
        System.out.println("PLease, input " + worker.getName() + "'s coordinates");
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
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
            setCoordinates();
        }

        this.worker.setCoordinates(c);
    }

    protected void setDates() throws OperationCanceledException{
        System.out.println("Please, write the start day (yyyy-mm-dd): ");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(Terminal.removeSpaces(Terminal.repeatInputAndExpectRegex(
                "start day", "\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")), formatter);

        worker.setStartDate( date.atStartOfDay(ZoneId.systemDefault()) );

        System.out.println("Please, write the end day (yyyy-mm-dd): ");
        String enddate = null;
        enddate = Terminal.removeSpaces(
                Terminal.repeatInputAndExpectRegexOrNull("end day", "\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")
        );
        if (enddate == null){
            worker.setEndDate(null);
        } else {
            date = LocalDate.parse(enddate, formatter);

            worker.setEndDate(date.atStartOfDay(ZoneId.systemDefault()));
        }
    }
}

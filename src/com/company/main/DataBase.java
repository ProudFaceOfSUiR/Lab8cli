package com.company.main;

import com.company.classes.Person;
import com.company.classes.Position;
import com.company.classes.Worker;
import com.company.main.Commands;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class DataBase {
    private LinkedList<Worker> workers;
    private Scanner terminal;

    ZonedDateTime initializationTime;

    public DataBase(){}

    public void initialize(String filePath){
        readFromFile(filePath);

        this.workers = new LinkedList<>();
        this.terminal = new Scanner(System.in);

        initializationTime = ZonedDateTime.now();
        System.out.println("Database has been initialized");

        readFromTerminal();
    }

    public int returnIndexById(long id){
        int index = -1;
        for (int i = 0; i < workers.size(); i++) {
            if (workers.get(i).getId() == id){
                index = i;
                break;
            }
        }
        return index;
    }

    public void readFromTerminal() {
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
            else if (command.matches("\\s*")) {
                ;//do nothing
            }
            else {
                System.out.println("Invalid command");
            }
        }
    }

    public void readFromFile(String filePath){
        if (!filePath.matches("\\s*[A-Z]:\\/(\\w*\\/)*\\w+.txt")){ //todo change on xml
            System.out.println(filePath);
            System.out.println("Invalid filename. Try to read from file using READFROMFILE command");
        } else {
            File initializationFile = new File(filePath);
            System.out.println("File found!");
        }
    }

    protected void updateById(String commandWithID){
        //removing spaces and "update" word to turn into long
        for (String s : Arrays.asList("update", " ")) {
            commandWithID = commandWithID.replace(s, "");
        }
        long id = Long.parseLong(commandWithID);

        //trying to find element
        if (returnIndexById(id) != -1){
            updateFields(returnIndexById(id));
        } else {
            System.out.println("Element not found");
        }
    }

    protected void updateFields(int index){
        if (workers.get(index) == null || index > workers.size()){
            System.out.println("Invalid index!");
        } else {
            System.out.println("Which fields would you like to update: " + Arrays.stream(Position.getPositions()).toArray() + " ?");
            //todo
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
        this.workers.add(new Worker(name, salary));
    }

    protected void show(){
        System.out.println("Worker name | Worker's id | Worker's salary");
        for (int i = 0; i < workers.size(); i++) {
            System.out.println(workers.get(i).getName() + " " + workers.get(i).getId() + " " + workers.get(i).getSalary());
        }
    }

    protected void info(){
        System.out.println("Type: Linked List");
        System.out.println("Initialization date: " + initializationTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)));
        System.out.println("Number of Workers: " + this.workers.size());
    }
}

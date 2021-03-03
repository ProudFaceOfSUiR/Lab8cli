package com.company.main;

import com.company.classes.Position;
import com.company.classes.Worker;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class DataBase {
    private LinkedList<Worker> workers;
    private Scanner terminal;

    ZonedDateTime initializationTime;

    public DataBase(){}

    public void initialize(String filePath){
        this.workers = new LinkedList<>();
        this.terminal = new Scanner(System.in);

        readFromFile(filePath);

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
        if (!filePath.matches("\\s*[A-Z]:\\/(\\w*\\/)*\\w+.xml")){ //todo change on xml
            System.out.println(filePath);
            System.out.println("Invalid filename. Try to read from file using READFROMFILE command");
        } else {
            System.out.println("File found! Reading...");
            xmlParser(new File(filePath));
        }
    }

    protected void xmlParser(File file){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("worker");

            String name;
            double salary = 1;
            String positionString;
            Position position;

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    salary = Double.parseDouble(eElement.getElementsByTagName("salary").item(0).getTextContent() );
                    positionString = eElement.getElementsByTagName("position").item(0).getTextContent();

                    if (Position.findEnum(positionString) != null){
                        position = Position.findEnum(positionString);
                        this.workers.add(new Worker(name, salary, position));
                    } else {
                        System.out.println(name + "'s position is invalid");
                        System.out.println(positionString);
                        this.workers.add(new Worker(name, salary));
                    }
                }
            }

            System.out.println("DataBase has been successfully filled with " + nList.getLength() + " workers");
        } catch (Exception e) {
            e.printStackTrace();
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

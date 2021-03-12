package com.company.database;

import com.company.classes.Worker;
import com.company.enums.Position;
import com.company.exceptions.OperationCanceledException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class FileParser {

    public static boolean pathCheck(String filePath){
        if (!filePath.matches("\\s*[A-Z]:/(\\w*/)*\\w+.xml")){
            System.out.println(filePath);
            System.out.println("Invalid path. Couldn't get file");
            return false;
        }
        return true;
    }

    public static boolean permissionToReadCheck(Path filePath){
        if (!Files.isReadable((filePath))){
            System.out.println("File is restricted from editing.");
            return false;
        } else return true;
    }

    public static boolean alreadyExistCheck(String filePath) {
        File f = new File(filePath);
        return f.exists() && !f.isDirectory();
    }

    public static boolean overWriteFile(String filePath) throws OperationCanceledException{
        //check if file exists
        if (alreadyExistCheck(filePath)) {
            //giving the choice
            try {
                return Terminal.binaryChoice("overwrite the existing file");
            } catch (OperationCanceledException e) {
                throw e;
            }
        } else return true; //"overwriting" nonexistent file
    }

    public static Path getPath(String path){
        Path p = Paths.get(path);
        if (Files.notExists(p)){
            System.out.println("File doesn't exist!");
            return null;
        } else return p;
    }

    public static LinkedList<Worker> xmlToDatabase(String filepath) throws Exception{

        if (!pathCheck(filepath)){
            throw new Exception("Invalid path. Operation cancelled");
        }

        if (!permissionToReadCheck(getPath(filepath))){
            throw new Exception("File is restricted from editing. Operation cancelled");
        }

        LinkedList<Worker> database = new LinkedList<>();
        File file = new File(filepath);
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("worker");

            //fields of a worker
            String name;
            double salary;
            String positionString;
            Position position;
            Long height;
            Integer weight;

            //counter of successfully added workers
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

                    //adding position
                    positionString = eElement.getElementsByTagName("position").item(0).getTextContent();
                    position = Position.findEnum(positionString);

                    //adding personal qualities
                    if (eElement.getElementsByTagName("position").item(0).getTextContent().isEmpty()){
                        height = null;
                    } else {
                        height = Long.valueOf(eElement.getElementsByTagName("position").item(0).getTextContent());
                    }



                    successfullyAddedWorkers++;
                }
            }

            System.out.println("DataBase has been successfully filled with " + successfullyAddedWorkers + " workers");
            System.out.println("------------------------------------");
            return database;
        } catch (Exception e) {
            System.out.println("Something went wrong :0");
            return null;
        }
    }

    public static String dataBaseToString(LinkedList<Worker> database){
        StringBuilder sb = new StringBuilder();

        //writing preamble
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
        sb.append("<database>").append("\n");

        //writing workers
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

    public static void dataBasetoXML(String database, String filename){
        try {
            // Creates a FileWriter
            FileWriter file = new FileWriter(filename);

            // Creates a BufferedWriter
            BufferedWriter buffer = new BufferedWriter(file);

            //writing and flushing to file
            buffer.write(database);
            buffer.flush();

            System.out.println("Database was successfully saved to a new file!");
            buffer.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

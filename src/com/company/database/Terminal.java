package com.company.database;

import com.company.enums.Commands;
import com.company.exceptions.InvalidDataException;
import com.company.exceptions.UnknownCommandException;

import java.util.Arrays;
import java.util.Scanner;

public class Terminal {
    private static Scanner terminal = new Scanner(System.in);
    private static boolean readingFile = false;

    public static void changeScanner(Scanner s){
        Terminal.terminal = s;
        readingFile = !readingFile;
    }

    public static String removeString(String input, String string){
        if (input == null){
            return null;
        }

        //removing string plus whitespaces and tabulations
        String output = input;
        for (String s : Arrays.asList(string, " ", "\t")) {
            output = output.replace(s, "");
        }
        return output;
    }

    public static String removeSpaces(String input){
        if (input == null){
            return null;
        }

        String output = input;
        for (String s : Arrays.asList(" ", "\t")) {
            output = output.replace(s, "");
        }
        return output;
    }

    public static boolean binaryChoice(String move){
        System.out.println("Do you want to " + move + "? (Yes/No)");
        if (readingFile) {
            if (!terminal.hasNext()) {
                return false;
            }
        }
        String command = terminal.nextLine();

        command = command.toUpperCase();
        if (command.matches("\\s*YES\\s*\\w*\\s*")){
            return true;
        } else if (command.matches("\\s*NO\\s*\\w*\\s*")){
            return false;
        } else {
            binaryChoice(move);
        }
        return false;
    }

    public static String repeatInputAndExpectRegex(String waitFor, String expectedRegex){
        if (readingFile) {
            if (!terminal.hasNext()) {
                return null;
            }
        }
        String output = terminal.nextLine();
        while (!output.matches(expectedRegex)){
            System.out.println("Incorrect " + waitFor + " Please, try again: ");
            output = terminal.nextLine();
        }
        return output;
    }

    public static String repeatInputAndExpectRegexOrNull(String waitFor, String expectedRegex){
        if (readingFile) {
            if (!terminal.hasNext()) {
                return null;
            }
        }
        String output;
        output = terminal.nextLine();
        while (!output.matches(expectedRegex) && !output.isEmpty()){
            System.out.println("Incorrect " + waitFor + " Please, try again: ");
            output = terminal.nextLine();
        }
        if (output.isEmpty()){
            return null;
        } else return output;
    }

    public static Commands matchCommand(String input) throws UnknownCommandException{
        input = input.toLowerCase();
        for (Commands c: Commands.values()) {
            if (input.matches("\\s*" + c.toString().toLowerCase() + "\\s*\\w*") || input.matches("\\s*" + c.toString().toLowerCase() + "\\s+[0-9]+\\s*")){
                return c;
            }
        }
        throw new UnknownCommandException();
    }
}

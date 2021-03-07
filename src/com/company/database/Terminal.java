package com.company.database;

import com.company.exceptions.UnknownCommandException;

import java.util.Arrays;
import java.util.Scanner;

public class Terminal {
    private static final Scanner terminal = new Scanner(System.in);

    public static String removeString(String input, String string){
        //removing string plus whitespaces and tabulations
        String output = input;
        for (String s : Arrays.asList(string, " ", "\t")) {
            output = output.replace(s, "");
        }
        return output;
    }

    public static String removeSpaces(String input){
        String output = input;
        for (String s : Arrays.asList(" ", "\t")) {
            output = output.replace(s, "");
        }
        return output;
    }

    public static boolean binaryChoice(String move) throws UnknownCommandException {
        System.out.println("Do you want to " + move + "? (Yes/No)");
        String command = terminal.nextLine();

        command = command.toUpperCase();
        if (command.matches("\\s*YES\\s*\\w*\\s*")){
            return true;
        } else if (command.matches("\\s*NO\\s*\\w*\\s*")){
            return false;
        } else {
            throw new UnknownCommandException();
        }
    }

    public static String repeatInputAndExpectRegex(String waitFor, String expectedRegex){
        String output = terminal.nextLine();
        while (!output.matches(expectedRegex)){
            System.out.println("Incorrect " + waitFor + " Please, try again: ");
            output = terminal.nextLine();
        }
        return output;
    }

}

package com.company.database;

import com.company.enums.Commands;
import com.company.exceptions.OperationCanceledException;
import com.company.exceptions.UnknownCommandException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Terminal {
    public static Scanner terminal = new Scanner(System.in);

    /**
     * Changes scanner (it's needed when we execute script
     * @param s
     */
    public static void changeScanner(Scanner s){
        Terminal.terminal = s;
    }

    /**
     * Removes given string from input
     * @param input
     * @param string
     * @return
     */
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

    /**
     * Removes spaces and tabs from string
     * @param input
     * @return
     */
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

    /**
     * Gives user a choice YES or NO
     * @param move
     * @return
     * @throws OperationCanceledException
     */
    public static boolean binaryChoice(String move) throws OperationCanceledException{
        System.out.println("Are you sure you want to " + move + "? (Yes/No)");

        String command;
        try {
            command = terminal.nextLine();
        } catch (NoSuchElementException e){
            throw new OperationCanceledException();
        }
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
    public static boolean binaryChoice1(String move, Scanner scanner) throws OperationCanceledException{
        System.out.println("Are you sure you want to " + move + "? (Yes/No)");

        String command;

        try {
            command = scanner.nextLine();
        } catch (NoSuchElementException e){
            return false;
            //throw new OperationCanceledException();
        }
        command = command.toUpperCase();
        if (command.matches("\\s*YES\\s*\\w*\\s*")){
            return true;
        } else if (command.matches("\\s*NO\\s*\\w*\\s*")){
            return false;
        } else {
            return (binaryChoice1(move,scanner));
        }

    }

    /**
     * Repeats input until user inputs the right (matching regex) string
     * @param waitFor
     * @param expectedRegex
     * @return
     * @throws OperationCanceledException
     */
    public static String repeatInputAndExpectRegex(String waitFor, String expectedRegex) throws OperationCanceledException {
        String output;
        try {
            output = terminal.nextLine();
            while (!output.matches(expectedRegex)) {
                System.out.println("Incorrect " + waitFor + " Please, try again: ");
                output = terminal.nextLine();
            }
        }
        catch(NoSuchElementException e){
            throw new OperationCanceledException();
        }
        return output;
    }

    /**
     * Repeats input until user inputs the right (matching regex) string or null (empty line)
     * @param waitFor
     * @param expectedRegex
     * @return
     * @throws OperationCanceledException
     */
    public static String repeatInputAndExpectRegexOrNull(String waitFor, String expectedRegex) throws OperationCanceledException {
        String output;
        try {
            output = terminal.nextLine();
            while (!output.matches(expectedRegex) && !output.isEmpty()) {
                System.out.println("Incorrect " + waitFor + " Please, try again: ");
                output = terminal.nextLine();
            }
        } catch (NoSuchElementException e){
            throw new OperationCanceledException();
        }
        if (output.isEmpty()){
            return null;
        } else return output;
    }

    /**
     * Finds the command (from Commands) from string
     * @param input
     * @return
     * @throws UnknownCommandException
     */
    public static Commands matchCommand(String input) throws UnknownCommandException{
        input = input.toLowerCase();
        for (Commands c: Commands.values()) {
            if (input.matches("\\s*" + c.toString().toLowerCase() + "\\s*\\w*")
                    || input.matches("\\s*" + c.toString().toLowerCase() + "\\s+[0-9]+\\s*")
                    || input.matches("\\s*" + c.toString().toLowerCase() + "\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
                if (Terminal.removeSpaces(input).matches(Commands.ADD_IF_MAX.toString().toLowerCase())){
                    return Commands.ADD_IF_MAX;
                }
                return c;
            }
        }
        throw new UnknownCommandException();
    }
}

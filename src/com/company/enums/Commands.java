package com.company.enums;
//todo write descriptions
public enum Commands {
    HELP ("","Available commands"),
    INFO ("","Information about current stat of Collection"),
    SHOW ("","Prints all the elements"),
    ADD ("","Add a new worker"),
    UPDATE("{id}","Update worker's fields"),
    REMOVE_BY_ID("{id}","Removes worker"), //it's not me shit-naming, it's the tech task
    CLEAR("","Clears the database"),
    SAVE("","Save the database to a file"),
    EXECUTE_SCRIPT("",""),
    EXIT("","Exit databse"),
    ADD_IF_MAX("",""),
    REMOVE_GREATER("",""),
    REMOVE_LOWER("",""),
    GROUP_COUNTING_BY_POSITION("",""),
    COUNT_LESS_THAN_START_DATE("",""),
    FILTER_GREATER_THAN_START_DATE("","");

    private String argument;
    private String description;

    Commands(String argument, String description) {
        this.argument = argument;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getArgument(){
        return argument;
    }

    public static String[] getCommands() {
        String[] commands = new String[Commands.values().length];
        Commands[] commandsEnum = values();
        for (int i = 0; i < Commands.values().length; i++) {
            commands[i] = commandsEnum[i].toString();
        }
        return commands;
    }

    public static String[] getCommandsWithDescriptions() {
        String[] commands = new String[Commands.values().length];
        Commands[] commandsEnum = values();
        for (int i = 0; i < Commands.values().length; i++) {
            if (commandsEnum[i].getArgument() != ""){
                commands[i] = commandsEnum[i].toString() + " " + commandsEnum[i].getArgument() + ": " + commandsEnum[i].getDescription();
            } else {
                commands[i] = commandsEnum[i].toString() + ": " + commandsEnum[i].getDescription();
            }
        }
        return commands;
    }

    public static Commands findEnum(String s){
        s = s.toUpperCase();
        Commands command = null;
        for (int i = 0; i < values().length; i++) {
            if (s.equals(Commands.values()[i].toString())){
                command = Commands.values()[i];
            }
        }
        return command;
    }

    public static boolean isEnum(String s){
        s = s.toUpperCase();
        for (int i = 0; i < values().length; i++) {
            if (s.equals(Commands.values()[i].toString())){
                return true;
            }
        }
        return false;
    }
}

package com.company.main;

public enum Commands {
    HELP ("Available commands"),
    INFO ("Information about current stat of Collection"),
    SHOW ("Prints all the elemnts "),
    ADD (""),
    UPDATE(""),
    REMOVE_BY_ID(""), //it's not me shit-naming, it's the tech task
    CLEAR(""),
    SAVE(""),
    EXECUTE_SCRIPT(""),
    EXIT(""),
    ADD_IF_MAX(""),
    REMOVE_GREATER(""),
    REMOVE_LOWER(""),
    GROUP_COUNTING_BY_POSITION(""),
    COUNT_LESS_THAN_START_DATE(""),
    FILTER_GREATER_THAN_START_DATE("");

    private String description;

    Commands(String description) {
        this.description = description;
    }

    public String getCommandDescription() {
        return description;
    }

    public static String[] getCommands()
    {
        String[] commands = new String[Commands.values().length];
        Commands[] commandsEnum = values();
        for (int i = 0; i < Commands.values().length; i++) {
            commands[i] = commandsEnum[i].toString();
        }
        return commands;
    }

    public static String[] getCommandsWithDescriptions()
    {
        String[] commands = new String[Commands.values().length];
        Commands[] commandsEnum = values();
        for (int i = 0; i < Commands.values().length; i++) {
            commands[i] = commandsEnum[i].toString() + ": " + commandsEnum[i].getCommandDescription();
        }
        return commands;
    }
}

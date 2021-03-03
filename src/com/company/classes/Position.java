package com.company.classes;

public enum Position {
    MANAGER,
    LABORER,
    LEAD_DEVELOPER,
    BAKER,
    MANAGER_OF_CLEANING;

    public static String[] getPositions()
    {
        String[] commands = new String[Position.values().length];
        Position[] positionsEnum = values();
        for (int i = 0; i < Position.values().length; i++) {
            commands[i] = positionsEnum[i].toString();
        }
        return commands;
    }
}

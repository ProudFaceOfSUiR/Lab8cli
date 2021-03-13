package com.company.main;

//variant 312709

/*
Main class here is DataBase: it contains the database as LinkedList and operates it.
To start the database you need to initialize it, it will automatically try to load data from the file,
which path was given as an argument of the command line.

!The DataBase will not work without initialization!
 */

import com.company.database.DataBase;
import com.company.database.FileParser;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        if (args.length == 0){
            dataBase.initialize();
        } else {
            dataBase.initialize(args[0]);
        }
    }
}

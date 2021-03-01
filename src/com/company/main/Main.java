package com.company.main;

import com.company.DataBase;
import com.company.classes.Worker;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//variant 312709

public class Main {

    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        dataBase.initialize(args[0]);
    }
}

package com.company.Login;

//import com.company.PostgreSQL.Check;
import com.company.database.Terminal;
import com.company.exceptions.OperationCanceledException;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class User implements Serializable {
    private static final long serialVersionUID = 60L;
    private String password;
    private String login;
    private boolean newUser;
    private int id;

    public boolean getNew(){
        return this.newUser;
    }



    public void setId(int id) {
        this.id = id;
    }

    public static String encryptThisString(String input) {
        try {
            // getInstance() method is called with algorithm SHA-224
            MessageDigest md = MessageDigest.getInstance("SHA-224");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValid(){
        if ((this.password!=null&&this.login!=null&&this.login.length()>1&&this.password.length()>1)){
            return true;
        }else {return false;}
    }

    public void initiate2(){
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        String command;
        while(!done){
            System.out.println("Are you sure you want to sign in? (Yes/No)");
            command = scanner.nextLine();
            command.toLowerCase();
            if (command.equals("yes")){
                System.out.println("Enter login");
                this.login = scanner.nextLine();
                System.out.println("Enter password");
                this.password = scanner.nextLine();
                done = true;
                newUser = false;
            } else if (command.equals("no")){
                System.out.println("Are you sure you want to sign up? (Yes/No)");
                command = scanner.nextLine();
                command.toLowerCase();
                if (command.equals("yes")){
                    System.out.println("Enter login");
                    this.login = scanner.nextLine();
                    System.out.println("Enter password");
                    this.password = scanner.nextLine();
                    done = true;
                    newUser = true;
                }
            }
        }
    }

    public void initiate() {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        String command;
        while(!done) {
            try {
                if (Terminal.binaryChoice1("sign in", scanner)) {
                    System.out.println("Enter login");
                    command = scanner.nextLine();
                    this.login = command;
                    System.out.println("Enter password");
                    command = scanner.nextLine();
                    this.password = command;
                    if (!this.login.equals(null)||this.password.equals(null)) {
                        done = true;
                        newUser = false;
                    }
                } else if (!done && Terminal.binaryChoice1("sign up",scanner)) {
                    System.out.println("Enter login");
                    this.login = scanner.nextLine();
                    System.out.println("Enter password");
                    this.password = scanner.nextLine();
                    if (!this.login.equals(null)||this.password.equals(null)) {
                        done = true;
                        newUser = true;
                    }
                }
            } catch (OperationCanceledException e){
                scanner.close();
                scanner = new Scanner(System.in);
                System.out.println("Unknown command");
            }
        }

    }

    public String getPassword() {
        return password;
    }
    public String getLogin(){
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


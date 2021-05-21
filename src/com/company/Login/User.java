package com.company.Login;

import com.company.database.Terminal;
import com.company.exceptions.OperationCanceledException;

import java.io.Serializable;
import java.util.Scanner;

public class User implements Serializable {
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
    public boolean isValid(){
        if (this.password!=null&&this.login!=null&&this.login.length()>1&&this.password.length()>1){
            return true;
        }else {return false;}
    }

    public void initiate() throws OperationCanceledException {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        while(!done) {
            if (Terminal.binaryChoice("sign in")) {
                System.out.println("Enter login");
                this.login = scanner.nextLine();
                System.out.println("Enter password");
                this.password = scanner.nextLine();
                done = true;
                newUser = false;
            } else if(!done && Terminal.binaryChoice("sign up")){
                System.out.println("Enter login");
                this.login = scanner.nextLine();
                System.out.println("Enter password");
                this.password = scanner.nextLine();
                done = true;
                newUser = true;
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

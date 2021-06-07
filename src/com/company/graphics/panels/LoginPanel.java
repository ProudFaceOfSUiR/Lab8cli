package com.company.graphics.panels;

import com.company.Login.User;
import com.company.graphics.frames.GeneralFrame;
import com.company.graphics.frames.MainFrame;
import com.company.network.Client;
import com.company.network.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class LoginPanel extends GeneralPanel{
    //to check
    private String login;
    private char[] password;
    Client client;

    public String getLogin() {
        return login;
    }

    public char[] getPassword() {
        return password;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public LoginPanel(GeneralFrame parentFrame, Container c, Client client) {
        super(parentFrame, c);
        
        this.client = client;
    }


    //graphics

    public void initializeLoginFrame(){
        container = this;
        cards = new CardLayout(0, 0);
        this.setLayout(cards);
        JPanel loginWindow = getLoginWindow();
        JPanel registerWindow = getRegisterWindow();
        this.add(loginWindow);
        this.add(registerWindow);
    }

    public JPanel getLoginWindow(){
        JPanel jPanel = new JPanel(new GridBagLayout());
        jPanel.setBounds(screenWidth/2 - 250, screenHeigth/2-250, width,heigth);

        JLabel userNameLabel = new JLabel("Enter username:");
        JTextField userNameField = new JTextField(20);

        JPasswordField passwordField = new JPasswordField( 20);
        JLabel passwordLabel = new JLabel("Enter password:");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        JLabel messageLabel = new JLabel("");
        constraints.gridx = 1;
        constraints.gridy = 0;
        jPanel.add(messageLabel,constraints);


        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 1;
        jPanel.add(userNameLabel, constraints);

        constraints.gridx = 1;
        jPanel.add(userNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        jPanel.add(passwordLabel, constraints);

        constraints.gridx = 1;
        jPanel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;

        //setting button to send data
        Button loginButton = new Button("Log in");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = userNameField.getText();
                char[] password = passwordField.getPassword();

                //validating login
                if (login.equals("") || login.matches("\\*")){
                    messageLabel.setText("Login can't be empty!");
                    passwordField.setText("");
                    return;
                }

                //check if password is empty
                if (password.length == 0){
                    messageLabel.setText("Password can't be empty");
                    return;
                }

                messageLabel.setText("");

                setLogin(login);
                setPassword(password);

                String pass = Arrays.toString(password);
                User user = new User(login, pass);
                user.newUser = false;

                client.user = user;
                client.setUser();

                Messages messages = null;
                try {
                    messages = client.sendMessage();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                if (messages.getObject(1).equals(true)){
                    nextPanelInFrame();
                } else {
                    messageLabel.setText("This user doesn't exist");
                }
            }
        });
        jPanel.add(loginButton, constraints);


        constraints.gridy = 4;
        Button registerButton = new Button("Register instead");
        registerButton.addActionListener(new ChangeCard());
        jPanel.add(registerButton, constraints);

        // set border for the panel
        jPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Login Panel"));

        //showing everything
        jPanel.revalidate();
        return jPanel;
    }

    public JPanel getRegisterWindow(){
        JPanel jPanel = new JPanel(new GridBagLayout());
        jPanel.setBounds(screenWidth/2 - 250, screenHeigth/2-250, width, heigth);

        JLabel userNameLabel = new JLabel("Enter username:");
        JTextField userNameField = new JTextField(20);

        JPasswordField passwordField = new JPasswordField( 20);

        JLabel passwordLabel = new JLabel("Enter password:");

        JPasswordField repeatPasswordField = new JPasswordField( 20);
        JLabel repeatPasswordLabel = new JLabel("Enter password:");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        JLabel messageLabel = new JLabel("");
        constraints.gridx = 1;
        constraints.gridy = 0;
        jPanel.add(messageLabel,constraints);

        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 1;
        jPanel.add(userNameLabel, constraints);

        constraints.gridx = 1;
        jPanel.add(userNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        jPanel.add(passwordLabel, constraints);

        constraints.gridx = 1;
        jPanel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        jPanel.add(repeatPasswordLabel, constraints);

        constraints.gridx = 1;
        jPanel.add(repeatPasswordField,constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;

        //setting button to send data
        Button registerButton = new Button("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = userNameField.getText();
                char[] password = passwordField.getPassword();
                char[] repreatPassword = repeatPasswordField.getPassword();

                //validating login
                if (login.equals("") || login.matches("\\*")){
                    messageLabel.setText("Login can't be empty!");
                    passwordField.setText("");
                    repeatPasswordField.setText("");
                    return;
                }

                //check if password is empty
                if (password.length == 0 && repreatPassword.length == 0){
                    messageLabel.setText("Password can't be empty");
                    return;
                }

                //check if passwords' lenght are different == passwords are diff
                if (password.length != repreatPassword.length){
                    messageLabel.setText("Passwords don't match!");
                    passwordField.setText("");
                    repeatPasswordField.setText("");
                    return;
                }

                //check pass match
                for (int i = 0; i < password.length; i++) {
                    if (password[i] != repreatPassword[i]) {
                        messageLabel.setText("Passwords don't match!");
                        passwordField.setText("");
                        repeatPasswordField.setText("");
                        return;
                    }
                }

                messageLabel.setText("");
                setLogin(login);
                setPassword(password);

                String pass = Arrays.toString(password);
                User user = new User(login, pass);
                user.newUser = true;

                client.user = user;
                client.setUser();

                Messages messages = null;
                try {
                    messages = client.sendMessage();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                if (messages.getObject(1).equals(true)){
                    nextPanelInFrame();
                } else {
                    messageLabel.setText("Abstract error");
                }
            }
        });
        jPanel.add(registerButton, constraints);

        constraints.gridy = 5;
        Button loginButton = new Button("Log In instead");
        loginButton.addActionListener(new ChangeCard());
        jPanel.add(loginButton, constraints);

        // set border for the panel
        jPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Register Panel"));

        //showing everything
        jPanel.revalidate();
        return jPanel;
    }
}

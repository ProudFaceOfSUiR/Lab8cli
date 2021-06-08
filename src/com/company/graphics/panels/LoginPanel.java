package com.company.graphics.panels;

import com.company.Login.User;
import com.company.enums.Languages;
import com.company.enums.Position;
import com.company.graphics.Language;
import com.company.graphics.frames.GeneralFrame;
import com.company.graphics.frames.MainFrame;
import com.company.network.Client;
import com.company.network.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

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

    public LoginPanel(GeneralFrame parentFrame, Container c, Client client, Locale locale, Language language, Languages currentLanguage) {
        super(parentFrame, c,locale, language, currentLanguage);
        
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

    Button loginButtonLogin;
    Button registerButtonLogin;
    JLabel userNameLabel;
    JLabel passwordLabel;

    public void changeLanguageInPanel(){

    }

    public JPanel getLoginWindow(){

        //changeLanguage(Languages.ru_RU);

        JPanel jPanel = new JPanel(new GridBagLayout());
        jPanel.setBounds(screenWidth/2 - 250, screenHeigth/2-250, width,heigth);

        userNameLabel = new JLabel(language.getLoginLabel());
        JTextField userNameField = new JTextField(20);

        JPasswordField passwordField = new JPasswordField( 20);
        passwordLabel = new JLabel(language.getPassLabel());

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

        Locale l = new Locale("ru", "RU");

        ResourceBundle r = ResourceBundle.getBundle("text", l);

        String buttonText = r.getString("login.button");
        //buttonText = new String(buttonText.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        //setting button to send data
        loginButtonLogin = new Button(buttonText);
        loginButtonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = userNameField.getText();
                char[] password = passwordField.getPassword();

                //validating login
                if (login.equals("") || login.matches("\\*")){
                    messageLabel.setText(language.getInvalidLogin());
                    passwordField.setText("");
                    return;
                }

                //check if password is empty
                if (password.length == 0){
                    messageLabel.setText(language.getInvalidPassword());
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
                    messageLabel.setText(language.getNoUser());
                }
            }
        });
        jPanel.add(loginButtonLogin, constraints);


        constraints.gridy = 4;
        registerButtonLogin = new Button(language.getRegisterButton());
        registerButtonLogin.addActionListener(new ChangeCard());
        jPanel.add(registerButtonLogin, constraints);

        constraints.gridy = 5;
        JComboBox languagesJComboBox = new JComboBox<>(Languages.values());
        languagesJComboBox.setSelectedItem(language);
        jPanel.add(languagesJComboBox, constraints);
        languagesJComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLanguage((Languages) languagesJComboBox.getSelectedItem());

                loginButtonLogin.setLabel(language.getLoginButton());
                registerButtonLogin.setLabel(language.getRegisterInsteadButton());
                userNameLabel.setText(language.getLoginLabel());
                passwordLabel.setText(language.getPassLabel());

                revalidate();
            }
        });

        // set border for the panel
        jPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Login Panel"));

        jPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                changeLanguage(language.getCurrentLang());
                languagesJComboBox.setSelectedItem(language.getCurrentLang());

                loginButtonLogin.setLabel(language.getLoginInsteadButton());
                registerButtonLogin.setLabel(language.getRegisterButton());
                userNameLabel.setText(language.getLoginLabel());
                passwordLabel.setText(language.getPassLabel());

                revalidate();
            }
        });

        //showing everything
        jPanel.revalidate();
        return jPanel;
    }

    public JPanel getRegisterWindow(){
        JPanel jPanel = new JPanel(new GridBagLayout());
        jPanel.setBounds(screenWidth/2 - 250, screenHeigth/2-250, width, heigth);

        JLabel userNameLabel = new JLabel(language.getLoginLabel());
        JTextField userNameField = new JTextField(20);

        JPasswordField passwordField = new JPasswordField( 20);

        JLabel passwordLabel = new JLabel(language.getPassLabel());

        JPasswordField repeatPasswordField = new JPasswordField( 20);
        JLabel repeatPasswordLabel = new JLabel(language.getRepeatPassLabel());

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
        Button registerButton = new Button(language.getRegisterButton());
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = userNameField.getText();
                char[] password = passwordField.getPassword();
                char[] repreatPassword = repeatPasswordField.getPassword();

                //validating login
                if (login.equals("") || login.matches("\\*")){
                    messageLabel.setText(language.getInvalidLogin());
                    passwordField.setText("");
                    repeatPasswordField.setText("");
                    return;
                }

                //check if password is empty
                if (password.length == 0 && repreatPassword.length == 0){
                    messageLabel.setText(language.getInvalidPassword());
                    return;
                }

                //check if passwords' lenght are different == passwords are diff
                if (password.length != repreatPassword.length){
                    messageLabel.setText(language.getPassNotMatch());
                    passwordField.setText("");
                    repeatPasswordField.setText("");
                    return;
                }

                //check pass match
                for (int i = 0; i < password.length; i++) {
                    if (password[i] != repreatPassword[i]) {
                        messageLabel.setText(language.getPassNotMatch());
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
        Button loginButton = new Button(language.getLoginInsteadButton());
        loginButton.addActionListener(new ChangeCard());
        jPanel.add(loginButton, constraints);


        constraints.gridy = 6;
        JComboBox langJComboBoxRegister = new JComboBox<>(Languages.values());
        langJComboBoxRegister.setSelectedItem(language);
        jPanel.add(langJComboBoxRegister, constraints);
        langJComboBoxRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                changeLanguage((Languages) langJComboBoxRegister.getSelectedItem());

                loginButton.setLabel(language.getLoginInsteadButton());
                registerButton.setLabel(language.getRegisterButton());
                userNameLabel.setText(language.getLoginLabel());
                passwordLabel.setText(language.getPassLabel());
                repeatPasswordLabel.setText(language.getRepeatPassLabel());

                revalidate();
            }
        });

        // set border for the panel
        jPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Register Panel"));

        jPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                changeLanguage(language.getCurrentLang());
                langJComboBoxRegister.setSelectedItem(language.getCurrentLang());

                loginButtonLogin.setLabel(language.getLoginInsteadButton());
                registerButton.setLabel(language.getRegisterButton());
                userNameLabel.setText(language.getLoginLabel());
                passwordLabel.setText(language.getPassLabel());
                repeatPasswordLabel.setText(language.getRepeatPassLabel());

                revalidate();
            }
        });

        //showing everything
        jPanel.revalidate();
        return jPanel;
    }
}

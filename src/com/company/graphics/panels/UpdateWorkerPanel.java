package com.company.graphics.panels;

import com.company.Login.User;
import com.company.classes.Coordinates;
import com.company.classes.Person;
import com.company.classes.Worker;
import com.company.enums.Commands;
import com.company.enums.Languages;
import com.company.enums.Position;
import com.company.exceptions.InvalidDataException;
import com.company.graphics.Language;
import com.company.graphics.frames.GeneralFrame;
import com.company.graphics.frames.MainFrame;
import com.company.network.Client;
import com.company.network.Messages;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UpdateWorkerPanel extends GeneralPanel{

    Client client;

    String name = "";
    long id;
    double salary;
    Position position = null;
    Person person = null;
    Coordinates coordinates = null;
    ZonedDateTime startDay = null;
    ZonedDateTime endDay = null;

    public void onShow(MainFrame parentFrame){
        Worker w = parentFrame.getWorkerToUpdate();

        this.name = w.getName();
        this.id = w.getId();
        this.salary = w.getSalary();
        this.position = w.getPosition();
        this.person = w.getPerson();
        this.coordinates = w.getCoordinates();
        this.startDay = w.getStartDate();
        this.endDay = w.getEndDate();

        container = this;
        cards = new CardLayout(0, 0);
        this.setLayout(cards);
        this.add(initializeAddWorkerFrame());
        this.revalidate();
        this.setVisible(true);
    }

    public UpdateWorkerPanel(MainFrame parentFrame, Container c, Client client, Locale locale, Language language, Languages currentLanguage) {
        super(parentFrame, c, locale, language, currentLanguage);

        this.client = client;

        this.addComponentListener ( new ComponentAdapter()
        {
            public void componentShown ( ComponentEvent e )
            {
                System.out.println ( "Update shown" );
                onShow(parentFrame);
                changeLangue(language.getCurrentLang());
            }

            public void componentHidden ( ComponentEvent e )
            {
                System.out.println ( "Update hidden" );
            }
        } );
    }

    public void changeLangue(Languages lang){
        changeLanguage(lang);

        nameLabel.setText(language.getNameLabel());
        salaryLabel.setText(language.getSalaryLabel());
        positionLabel.setText(language.getPositionLabel());
        personLabel.setText(language.getPersonLabel());
        coordinatesLabel.setText(language.getCoordLabel());
        startDateLabel.setText(language.getStartDateLabel());
        endDateLabel.setText(language.getEndDateLabel());

        submitButton.setText(language.getSubmitButton());
        backButton.setText(language.getBackButton());

        langJComboBox.setSelectedItem(lang);

        revalidate();
    }

    JLabel nameLabel;
    JLabel salaryLabel;
    JLabel positionLabel;
    JLabel personLabel;
    JLabel coordinatesLabel;
    JLabel startDateLabel;
    JLabel endDateLabel;
    JComboBox langJComboBox;

    JButton submitButton;
    JButton backButton;

    public JPanel initializeAddWorkerFrame(){
        JPanel jPanel = new JPanel(new GridBagLayout());
        jPanel.setBounds(screenWidth/2 - 250, screenHeigth/2-250, width,heigth);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10, 10, 10, 10);

        JTextArea messageLabel = new JTextArea("");
        messageLabel.setLineWrap(true);
        messageLabel.setWrapStyleWord(true);
        messageLabel.setEditable(false);
        messageLabel.setMinimumSize(new Dimension(350, 50));
        messageLabel.setPreferredSize(new Dimension(350, 50));
        messageLabel.setMaximumSize(new Dimension(350, 50));

        nameLabel = new JLabel(language.getNameLabel());
        salaryLabel = new JLabel(language.getSalaryLabel());
        positionLabel = new JLabel(language.getPositionLabel());
        personLabel = new JLabel(language.getPersonLabel());
        coordinatesLabel = new JLabel(language.getCoordLabel());
        startDateLabel = new JLabel(language.getStartDateLabel());
        endDateLabel = new JLabel(language.getEndDateLabel());

        JLabel coordinatesHintLabel = new JLabel("(x,y)");
        JLabel personalityHintLabel = new JLabel("(height,weight)");
        JLabel dateHintLabel = new JLabel("(yyyy-mm-dd)");
        JLabel dateHintLabel1 = new JLabel("(yyyy-mm-dd)");

        JTextField nameField = new JTextField(this.name);
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            public void changeColor(){
                nameLabel.setForeground(Color.BLACK);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeColor();
            }
        });

        JTextField salaryField = new JTextField(String.valueOf(this.salary));
        salaryField.getDocument().addDocumentListener(new DocumentListener() {
            public void changeColor(){
                salaryLabel.setForeground(Color.BLACK);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeColor();
            }
        });

        JComboBox positionJComboBox = new JComboBox<>(Position.getPositions());
        positionJComboBox.setSelectedItem(this.position);

        JTextField personField = new JTextField(this.person.getWeight() + "," + this.person.getHeight());
        personField.getDocument().addDocumentListener(new DocumentListener() {
            public void changeColor(){
                personLabel.setForeground(Color.BLACK);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeColor();
            }
        });

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        jPanel.add(messageLabel,constraints);

        constraints.anchor = GridBagConstraints.WEST;
        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 1;
        jPanel.add(nameLabel, constraints);

        constraints.gridx = 1;
        jPanel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        jPanel.add(salaryLabel, constraints);

        constraints.gridx = 1;
        jPanel.add(salaryField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        jPanel.add(positionLabel, constraints);

        constraints.gridx = 1;
        jPanel.add(positionJComboBox, constraints);

        //personality
        constraints.gridx = 0;
        constraints.gridy = 4;
        jPanel.add(personLabel, constraints);

        constraints.gridx = 1;
        jPanel.add(personField, constraints);

        constraints.gridx = 2;
        jPanel.add(personalityHintLabel, constraints);

        //coordinates
        constraints.gridx = 0;
        constraints.gridy = 5;
        jPanel.add(coordinatesLabel, constraints);

        JTextField coordinatesField = new JTextField(this.coordinates.getX() + "," + this.coordinates.getY());
        coordinatesField.getDocument().addDocumentListener(new DocumentListener() {
            public void changeColor(){
                coordinatesLabel.setForeground(Color.BLACK);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeColor();
            }
        });
        constraints.gridx = 1;
        jPanel.add(coordinatesField, constraints);

        constraints.gridx = 2;
        jPanel.add(coordinatesHintLabel, constraints);

        backButton = new JButton(language.getBackButton());
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanelInFrame("database");
            }
        });

        /*
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
        JFormattedTextField.AbstractFormatter formatter = new DateFormatter();
        JDatePickerImpl startDatePicker = new JDatePickerImpl(datePanel,formatter);
        JDatePickerImpl endDatePicker = new JDatePickerImpl(datePanel,formatter);
         */

        JTextField startDateField = new JTextField(this.startDay.toString().substring(0,10));
        startDateField.getDocument().addDocumentListener(new DocumentListener() {
            public void changeColor(){
                startDateLabel.setForeground(Color.BLACK);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeColor();
            }
        });

        constraints.gridy = 6;
        constraints.gridx = 0;
        jPanel.add(startDateLabel,constraints);

        constraints.gridx = 1;
        jPanel.add(startDateField, constraints);

        constraints.gridx = 2;
        jPanel.add(dateHintLabel, constraints);

        JTextField endDateField = new JTextField(this.endDay.toString().substring(0,10));
        endDateField.getDocument().addDocumentListener(new DocumentListener() {
            public void changeColor(){
                endDateLabel.setForeground(Color.BLACK);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeColor();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeColor();
            }
        });

        constraints.gridy = 7;
        constraints.gridx = 0;
        jPanel.add(endDateLabel,constraints);

        constraints.gridx = 1;
        jPanel.add(endDateField, constraints);

        constraints.gridx = 2;
        jPanel.add(dateHintLabel1, constraints);

        constraints.gridx = 0;
        constraints.gridy = 8;
        jPanel.add(backButton, constraints);

        submitButton = new JButton(language.getSubmitButton());
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder message = new StringBuilder();
                boolean hasError = false;

                String name = nameField.getText();
                if (name.isEmpty() || name.matches("\\s*")){
                    message.append("Name cannot be empty").append("; ");
                    nameField.setText(name);
                    nameLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String salary = salaryField.getText();
                if (!salary.matches("\\s*\\d+\\.*\\d*\\s*")){
                    message.append("Invalid salary").append("; ");
                    salaryField.setText(salary);
                    salaryLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String personality = personField.getText();
                if (!personality.matches("\\s*\\d+,\\d+\\s*")){
                    message.append("Invalid personality").append("; ");
                    personField.setText(person.getHeight().toString() + ", " + person.getWeight().toString());
                    personLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String coordinatesString = coordinatesField.getText();
                if (!coordinatesString.matches("\\s*\\d+,\\d+\\s*")){
                    message.append("Invalid coordinates").append("; ");
                    coordinatesField.setText(coordinates.getX() + ", " + coordinates.getY());
                    coordinatesLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String startDate = startDateField.getText();
                if (!startDate.matches("\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
                    message.append("Invalid start date").append("; ");
                    startDateField.setText(startDay.toString().substring(0,10));
                    startDateLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String endDate = endDateField.getText();
                if (!endDate.matches("\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
                    message.append("Invalid end date").append("; ");
                    endDateField.setText(endDay.toString().substring(0,10));
                    endDateLabel.setForeground(Color.RED);
                    hasError = true;
                }

                if (hasError){
                    messageLabel.setText(message.toString());
                } else {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate sdate = LocalDate.parse(startDate, formatter);
                    LocalDate edate = LocalDate.parse(endDate, formatter);
                    Worker w = null;
                    Position position = Position.findEnum((String) positionJComboBox.getSelectedItem());
                    try {
                        w = new Worker(0,
                                name,
                                Double.parseDouble(salary),
                                position,
                                new Person(
                                        Long.parseLong(personality.split(",")[0]),
                                        Integer.parseInt(personality.split(",")[1])
                                ),
                                new Coordinates(
                                        Long.parseLong(coordinatesString.split(",")[0]),
                                        Integer.parseInt(coordinatesString.split(",")[1]
                                        )),
                                sdate.atStartOfDay(ZoneId.systemDefault()),
                                sdate.atStartOfDay(ZoneId.systemDefault()),
                                client.user
                        );
                    } catch (InvalidDataException invalidDataException) {
                        invalidDataException.printStackTrace();
                    }

                    client.output.addObject(w);
                    Messages input = new Messages();
                    //System.out.println("Message recieved");
                    try {
                        input = client.sendMessage();
                    } catch (Exception ee) {
                        changePanelInFrame("loading");
                    }
                    System.out.println(input.getObject(1));

                    messageLabel.setText((String) input.getObject(1));

                    messageLabel.setText("Updated");
                    nameField.setText("");
                    salaryField.setText("");
                    personField.setText("");
                    coordinatesField.setText("");
                    startDateField.setText("");
                    endDateField.setText("");
                }
            }
        });

        constraints.gridx = 1;
        jPanel.add(submitButton, constraints);

        constraints.gridx = 2;
        langJComboBox = new JComboBox<>(Languages.values());
        langJComboBox.setSelectedItem(language);
        langJComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLangue((Languages) langJComboBox.getSelectedItem());
            }
        });
        jPanel.add(langJComboBox, constraints);

        // set border for the panel
        jPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Add Worker Panel"));

        //showing everything
        jPanel.revalidate();
        return jPanel;
    }
}

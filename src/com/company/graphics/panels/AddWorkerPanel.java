package com.company.graphics.panels;

import com.company.classes.Coordinates;
import com.company.classes.Person;
import com.company.classes.Worker;
import com.company.database.Terminal;
import com.company.enums.Commands;
import com.company.enums.Position;
import com.company.exceptions.InvalidDataException;
import com.company.exceptions.NotConnectedException;
import com.company.graphics.frames.GeneralFrame;
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

public class AddWorkerPanel extends GeneralPanel{

    Client client;

    public AddWorkerPanel(GeneralFrame parentFrame, Container c, Client client) {
        super(parentFrame, c);
        container = this;
        cards = new CardLayout(0, 0);
        this.setLayout(cards);
        this.add(initializeAddWorkerFrame());
        this.client = client;

        this.addComponentListener ( new ComponentAdapter()
        {
            public void componentShown ( ComponentEvent e )
            {
                System.out.println("Addworker shown");
                //todo erase everything
            }

            public void componentHidden ( ComponentEvent e )
            {
                System.out.println ( "Addworker hidden" );
            }
        } );
    }

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

        JLabel nameLabel = new JLabel("Name: ");
        JLabel salaryLabel = new JLabel("Salary: ");
        JLabel positionLabel = new JLabel("Position: ");
        JLabel personLabel = new JLabel("Personality: ");
        JLabel coordinatesLabel = new JLabel("Coordinates: ");
        JLabel startDateLabel = new JLabel("Start date: ");
        JLabel endDateLabel = new JLabel("End date: ");

        JLabel coordinatesHintLabel = new JLabel("(x,y)");
        JLabel personalityHintLabel = new JLabel("(height,weight)");
        JLabel dateHintLabel = new JLabel("(yyyy-mm-dd)");
        JLabel dateHintLabel1 = new JLabel("(yyyy-mm-dd)");

        JTextField nameField = new JTextField();
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

        JTextField salaryField = new JTextField();
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

        JTextField personField = new JTextField();
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

        JTextField coordinatesField = new JTextField();
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

        JButton backButton = new JButton("Back to database");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousPanelInFrame();
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

        JTextField startDateField = new JTextField();
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

        JTextField endDateField = new JTextField();
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
        JLabel addIfMaxLabel = new JLabel("Add if salary is maximum");
        jPanel.add(addIfMaxLabel, constraints);
        JCheckBox addIfMaxCheckBox = new JCheckBox();
        constraints.gridx = 1;
        jPanel.add(addIfMaxCheckBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 9;
        jPanel.add(backButton, constraints);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder message = new StringBuilder();
                boolean hasError = false;

                String name = nameField.getText();
                if (name.isEmpty() || name.matches("\\s*")){
                    message.append("Name cannot be empty").append("; ");
                    nameField.setText("");
                    nameLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String salary = salaryField.getText();
                if (!salary.matches("\\s*\\d+\\.*\\d*\\s*")){
                    message.append("Invalid salary").append("; ");
                    salaryField.setText("");
                    salaryLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String personality = personField.getText();
                if (!personality.matches("\\s*\\d+,\\d+\\s*")){
                    message.append("Invalid personality").append("; ");
                    personField.setText("");
                    personLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String coordinates = coordinatesField.getText();
                if (!personality.matches("\\s*\\d+,\\d+\\s*")){
                    message.append("Invalid coordinates").append("; ");
                    coordinatesField.setText("");
                    coordinatesLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String startDate = startDateField.getText();
                if (!startDate.matches("\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
                    message.append("Invalid start date").append("; ");
                    startDateField.setText("");
                    startDateLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String endDate = endDateField.getText();
                if (!startDate.matches("\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
                    message.append("Invalid end date").append("; ");
                    endDateField.setText("");
                    endDateLabel.setForeground(Color.RED);
                    hasError = true;
                }

                if (hasError){
                    messageLabel.setText(message.toString());
                } else {

                    client.output.addObject(client.user);
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
                                        Long.parseLong(personality.substring(0,personality.indexOf(","))),
                                        Integer.parseInt(personality.substring(personality.length() - personality.indexOf(","))
                                )),
                                new Coordinates(
                                        Long.parseLong(coordinates.substring(0,coordinates.indexOf(","))),
                                        Integer.parseInt(coordinates.substring(coordinates.length() - coordinates.indexOf(","))
                                )),
                                sdate.atStartOfDay(ZoneId.systemDefault()),
                                sdate.atStartOfDay(ZoneId.systemDefault()),
                                client.user
                        );
                    } catch (InvalidDataException invalidDataException) {
                        invalidDataException.printStackTrace();
                    }

                    if (addIfMaxCheckBox.isSelected()){
                        client.output.addObject(Commands.ADD_IF_MAX);
                    } else {
                        client.output.addObject(Commands.ADD);
                    }

                    client.output.addObject(w);
                    Messages input = new Messages();
                    //System.out.println("Message recieved");
                    try {
                        input = client.sendMessage();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    System.out.println(input.getObject(1));

                    messageLabel.setText((String) input.getObject(1));
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

        // set border for the panel
        jPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Add Worker Panel"));

        //showing everything
        jPanel.revalidate();
        return jPanel;
    }
}

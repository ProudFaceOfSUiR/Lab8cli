package com.company.graphics.panels;

import com.company.classes.Coordinates;
import com.company.classes.Person;
import com.company.classes.Worker;
import com.company.database.Terminal;
import com.company.enums.Commands;
import com.company.enums.Languages;
import com.company.enums.Position;
import com.company.exceptions.InvalidDataException;
import com.company.exceptions.NotConnectedException;
import com.company.graphics.Language;
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
import java.util.Locale;

public class AddWorkerPanel extends GeneralPanel{

    Client client;

    public AddWorkerPanel(GeneralFrame parentFrame, Container c, Client client, Locale locale, Language language, Languages currentLanguage) {
        super(parentFrame, c, locale, language, currentLanguage);
        container = this;
        cards = new CardLayout(0, 0);
        this.setLayout(cards);
        this.add(initializeAddWorkerFrame());
        this.client = client;

        this.addComponentListener ( new ComponentAdapter()
        {
            public void componentShown ( ComponentEvent e )
            {
                changeLangue(language.getCurrentLang());
                System.out.println("Addworker shown");
            }

            public void componentHidden ( ComponentEvent e )
            {
                System.out.println ( "Addworker hidden" );
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
        addIfMaxLabel.setText(language.getAddIfMaxLabel());

        submitButton.setText(language.getSubmitButton());
        backButton.setText(language.getBackButton());

        personalityHintLabel.setText(language.getWeightHeight());

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
    JLabel addIfMaxLabel;

    JButton submitButton;
    JButton backButton;

    JLabel personalityHintLabel;

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
        personalityHintLabel = new JLabel(language.getWeightHeight());
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

        backButton = new JButton(language.getBackButton());
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
        addIfMaxLabel = new JLabel(language.getAddIfMaxLabel());
        jPanel.add(addIfMaxLabel, constraints);
        JCheckBox addIfMaxCheckBox = new JCheckBox();
        constraints.gridx = 1;
        jPanel.add(addIfMaxCheckBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 9;
        jPanel.add(backButton, constraints);

        submitButton = new JButton(language.getSubmitButton());
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder message = new StringBuilder();
                boolean hasError = false;

                String name = nameField.getText();
                if (name.isEmpty() || name.matches("\\s*")){
                    message.append( language.getInvalidName() ).append("; ");
                    nameField.setText("");
                    nameLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String salary = salaryField.getText();
                if (!salary.matches("\\s*\\d+\\.*\\d*\\s*")){
                    message.append(language.getInvalidSalary()).append("; ");
                    salaryField.setText("");
                    salaryLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String personality = personField.getText();
                if (!personality.matches("\\s*\\d+,\\d+\\s*")){
                    message.append(language.getInvalidPersonality()).append("; ");
                    personField.setText("");
                    personLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String coordinates = coordinatesField.getText();
                if (!personality.matches("\\s*\\d+,\\d+\\s*")){
                    message.append(language.getInvalidCoordinates()).append("; ");
                    coordinatesField.setText("");
                    coordinatesLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String startDate = startDateField.getText();
                if (!startDate.matches("\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
                    message.append(language.getInvalidStart()).append("; ");
                    startDateField.setText("");
                    startDateLabel.setForeground(Color.RED);
                    hasError = true;
                }

                String endDate = endDateField.getText();
                if (!endDate.matches("\\s*(?!0000)(\\d{4})-(0[1-9]|1[0-2])-[0-3]\\d\\s*")){
                    message.append(language.getInvalidEnd()).append("; ");
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
                                        Long.parseLong(personality.split(",")[0]),
                                        Integer.parseInt(personality.split(",")[1])
                                ),
                                new Coordinates(
                                        Long.parseLong(coordinates.split(",")[0]),
                                        Integer.parseInt(coordinates.split(",")[1]
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
                        changePanelInFrame("loading");
                        return;
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

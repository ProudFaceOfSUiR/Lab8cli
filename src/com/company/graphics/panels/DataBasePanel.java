package com.company.graphics.panels;

import com.company.Login.User;
import com.company.classes.Coordinates;
import com.company.classes.Person;
import com.company.classes.Worker;
import com.company.database.DataBase;
import com.company.enums.Commands;
import com.company.enums.Position;
import com.company.exceptions.InvalidDataException;
import com.company.exceptions.NotConnectedException;
import com.company.graphics.frames.GeneralFrame;
import com.company.network.Client;
import com.company.network.Messages;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Vector;

public class DataBasePanel extends GeneralPanel{

    List<Vector<String>> database;
    Client client;

    JTable table;
    DatabaseTableModel tableModel;
    JTextArea informationArea;
    JTextArea messageArea;
    JLabel currentUser;

    Messages input;

    public void updateTable(){
        try {
            input = (Messages) client.readCommand(Commands.SHOW);
            database = (List<Vector<String>>) input.getObject(1);

            System.out.println("repainting database");
            System.out.println(database);

            tableModel.setRowCount(0);
            for (Vector<String> strings : database) {
                tableModel.addRow(strings);
            }

            table.repaint();

        } catch (NotConnectedException notConnectedException) {
            notConnectedException.printStackTrace();
        }
    }

    public DataBasePanel(GeneralFrame parentFrame, Container c, Client client) {
        super(parentFrame, c);

        this.client = client;


        this.addComponentListener ( new ComponentAdapter()
        {
            public void componentShown ( ComponentEvent e )
            {
                System.out.println("Database shown");
                input = null;
                try {
                    input = (Messages) client.readCommand(Commands.INFO);
                } catch (NotConnectedException ee) {
                    ee.printStackTrace();
                }
                informationArea.setText((String) input.getObject(1));
                currentUser.setText(client.user.getLogin());
                updateTable();
            }

            public void componentHidden ( ComponentEvent e )
            {
                System.out.println ( "Database hidden" );
            }
        } );
    }


    public void initializeDatabaseFrame(){
        container = this;
        cards = new CardLayout(0, 0);
        this.setLayout(cards);

        this.add(getDatabaseWindow());
    }

    public class DatabaseTableModel extends DefaultTableModel {

        public boolean isCellEditable(int row, int column){
            return false;
        }

    }

    public JPanel getDatabaseWindow(){
        JPanel databaseWindow = new JPanel();
        BoxLayout boxlayout = new BoxLayout(databaseWindow, BoxLayout.Y_AXIS);
        databaseWindow.setLayout(boxlayout);

        databaseWindow.setBounds(screenWidth/2 - 250, screenHeigth/2-250, width,heigth);


        FlowLayout header = new FlowLayout();
        JPanel headerPanel = new JPanel(header);

        informationArea = new JTextArea();
        informationArea.setMinimumSize(new Dimension(250, 50));
        informationArea.setPreferredSize(new Dimension(250, 50));
        informationArea.setMaximumSize(new Dimension(250, 50));
        informationArea.setEditable(false);


        String message = "message";
        messageArea = new JTextArea(message);
        messageArea.setMinimumSize(new Dimension(250, 50));
        messageArea.setPreferredSize(new Dimension(250, 50));
        messageArea.setMaximumSize(new Dimension(250, 50));
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JPanel userP = new JPanel();
        BoxLayout userLayout = new BoxLayout(userP,BoxLayout.Y_AXIS);
        userP.add(new JLabel("Current user:"));
        currentUser = new JLabel("null");
        userP.add(currentUser);
        userP.setLayout(userLayout);

        headerPanel.add(informationArea);
        headerPanel.add(messageArea);
        headerPanel.add(userP);

        databaseWindow.add(headerPanel);


        //headers for the table
        String[] columns = {"Name", "id", "Salary", "Position", "Personality", "Coordinates", "Start Date", "End Date", "User"};

        tableModel = new DatabaseTableModel();
        tableModel.setColumnIdentifiers(columns);
        String personality;
        String coordinates;

        //create table with data
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);
        table.setDragEnabled(false);

        //table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);

        JScrollPane jScrollPane = new JScrollPane(table);
        jScrollPane.createHorizontalScrollBar();
        //add the table to the frame
        databaseWindow.add(jScrollPane);

        JButton addWorkerButton = new JButton("Add worker");
        addWorkerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanelInFrame("addworker");
            }
        });

        JButton removeSelectedButton = new JButton("Remove selected");
        removeSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int column = 1;
                int row = table.getSelectedRow();
                String value = table.getModel().getValueAt(row, column).toString();
                long id = Long.parseLong(value);

                client.output.addObject(client.user);
                client.output.addObject(Commands.REMOVE_BY_ID);
                client.output.addObject(String.valueOf(id));

                try {
                    input = client.sendMessage();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                messageArea.setText((String) input.getObject(1));
                updateTable();
            }
        });

        JButton removeGreaterButton = new JButton("Remove greater");
        removeGreaterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int column = 2;
                int row = table.getSelectedRow();
                String value = table.getModel().getValueAt(row, column).toString();
                double id = Double.parseDouble(value);
                System.out.println(id);

                client.output.addObject(client.user);
                client.output.addObject(Commands.REMOVE_GREATER);
                client.output.addObject(String.valueOf(id));

                try {
                    input = client.sendMessage();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                messageArea.setText((String) input.getObject(1));
                updateTable();
            }
        });

        JButton removeLowerButton = new JButton("Remove lower");
        removeLowerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int column = 2;
                int row = table.getSelectedRow();
                String value = table.getModel().getValueAt(row, column).toString();
                double id = Double.parseDouble(value);
                System.out.println(id);

                client.output.addObject(client.user);
                client.output.addObject(Commands.REMOVE_LOWER);
                client.output.addObject(String.valueOf(id));

                try {
                    input = client.sendMessage();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                messageArea.setText((String) input.getObject(1));
                updateTable();
            }
        });

        JButton updateSelectedButton = new JButton("Update selected");
        updateSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int column = 1;
                int row = table.getSelectedRow();
                String value = table.getModel().getValueAt(row, column).toString();
                long id = Long.parseLong(value);
                //todo sending and checking if yours

                changePanelInFrame("updateworker");
            }
        });

        JButton clearButton = new JButton("Clear database");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.output.addObject(client.user);
                client.output.addObject(Commands.CLEAR);

                try {
                    input = client.sendMessage();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                messageArea.setText((String) input.getObject(1));
                updateTable();
            }
        });

        JButton visualisationButton = new JButton("Visualisation");
        visualisationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanelInFrame("visualisation");
            }
        });

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });

        FlowLayout footer = new FlowLayout();
        JPanel footerPanel = new JPanel(footer);

        footerPanel.add(addWorkerButton);
        footerPanel.add(removeSelectedButton);
        footerPanel.add(removeGreaterButton);
        footerPanel.add(removeLowerButton);
        footerPanel.add(updateSelectedButton);
        footerPanel.add(clearButton);

        FlowLayout footer2 = new FlowLayout();
        JPanel footerPanel2 = new JPanel(footer2);

        footerPanel2.add(visualisationButton);
        footerPanel2.add(refreshButton);

        databaseWindow.add(footerPanel);
        databaseWindow.add(footerPanel2);

        databaseWindow.revalidate();
        return databaseWindow;
    }
}

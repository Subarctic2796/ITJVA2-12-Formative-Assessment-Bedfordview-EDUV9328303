package org.Eduvos.Q3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class UI extends JFrame {
    private JTabbedPane mainTabbedPane;
    private JPanel mainPane;
    private JTextArea addInfoTextArea;
    private JTextField addNameTextField;
    private JTextField addDestTextField;
    // use a combo box as this stops users from
    // entering an invalid option for the ticket class
    private JComboBox<String> ticketClassComboBox;
    private JButton addTicketBtn;
    private JTextArea findInfoTextArea;
    private JButton findBtn;
    private JPanel addPane;
    private JPanel findPane;
    private JLabel addPassengerNameLbl;
    private JLabel ticketClassLbl;
    private JLabel addDestLbl;
    private JLabel findDestLbl;
    private JTextField findDestTextField;

    // added tickets
    private final ArrayList<TrainTicket> addTickets = new ArrayList<>();
    // find tickets
    private final ArrayList<TrainTicket> findTickets = new ArrayList<>();

    // use to build the string of tickets to show to the user
    private final StringBuilder sb = new StringBuilder();
    // the db connection
    private Connection con;

    // the adding query
    private static final String ADD_QUERY = "INSERT INTO Tickets (TicketID, TicketClass, Name, Destination, Price) VALUES(?, ?, ?, ?)";
    // the finding query
    private static final String FIND_QUERY = "SELECT * FROM Tickets";

    public UI() {
        String url = "jdbc:mysql://localhost:3306/TrainTicketsdDB";
        String user = "root";
        String password = "";

        // reuse the connection
        boolean connectionGood = false;
        for (int i = 0; i < 3; i++) {
            try {
                con = DriverManager.getConnection(url, user, password);
                System.out.println("established connection");
                connectionGood = true;
                break;
            } catch (SQLException e) {
                System.out.printf("could not establish connection: trying %d more times", 3 -i);
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }

        // could not get connection meaning there
        // is nothing we can do so just return
        if (!connectionGood) return;

        // set up other stuff
        setMinimumSize(new Dimension(500, 500));
        setResizable(true);
        setTitle("Train Ticket Booker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(mainPane);

        // set combobox default to nothing
        // https://stackoverflow.com/questions/6276177/jcombobox-want-first-entry-to-be-blank-entry
        ticketClassComboBox.setSelectedIndex(-1);

        addTicketBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // get the ticket
                TrainTicket ticket = getTrainTicket();
                if (ticket == null) return;
                addTickets.add(ticket);

                // only add the most recent ticket
                sb.append(ticket);
                sb.append("\n--------------------------\n");

                // actually show the user
                addInfoTextArea.setText(sb.toString());

                // add the ticket to the db
                 addTicketToDB(ticket);

                // reset everything to a clean state
                ticketClassComboBox.setSelectedIndex(-1);
                addNameTextField.setText("");
                addDestTextField.setText("");
            }
        });

        findBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                ArrayList<TrainTicket> filtered = getFindTickets();
                // errored so return
                if (filtered == null) return;

                // build string
                StringBuilder sb = new StringBuilder("Matching Tickets:\n");
                for (TrainTicket ticket : filtered) {
                    sb.append(ticket);
                    sb.append("\n--------------------------\n");
                }

                // show the user
                findInfoTextArea.setText(sb.toString());

                // reset everything to a clean state
                findDestTextField.setText("");
            }
        });
    }

    private TrainTicket getTrainTicket() {
        String name = addNameTextField.getText();
        // make sure not empty
        if (Objects.equals(name, "")) {
            JOptionPane.showMessageDialog(null, "must enter passenger name");
            return null;
        }

        String destination = addDestTextField.getText();
        // make sure not empty
        if (Objects.equals(destination, "")) {
            JOptionPane.showMessageDialog(null, "must enter a destination");
            return null;
        }

        int idx = ticketClassComboBox.getSelectedIndex();
        // make sure not empty
        if (idx == -1) {
            JOptionPane.showMessageDialog(null, "must choose either Economy or Business");
            return null;
        }

        // determine the tickets class
        TicketClass ticketClass = switch (idx) {
            case 0 -> TicketClass.ECONOMY;
            case 1 -> TicketClass.BUSINESS;
            default -> TicketClass.NONE;
        };

        return new TrainTicket(name, destination, ticketClass, 100);
    }

    private void addTicketToDB(TrainTicket ticket) {
        // ticket class to int
        int ticketClassInt = switch (ticket.getTicket_class()){
            case NONE -> 0;
            case ECONOMY -> 1;
            case BUSINESS -> 2;
        };

        try {
            PreparedStatement stmt = con.prepareStatement(ADD_QUERY);

            // set values
            stmt.setInt(1, ticketClassInt);
            stmt.setString(2, ticket.getName());
            stmt.setString(3, ticket.getDestination());
            double basePrice = ticket.getBase_price();
            if (ticket.getTicket_class() == TicketClass.BUSINESS) basePrice *= 1.5;
            stmt.setDouble(4, basePrice);

            // try add
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("failed to prepare statement");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "could not add ticket to the database");
        }
    }

    private ArrayList<TrainTicket> getFindTickets() {
        // reuse the arraylist
        findTickets.clear();

        try {
            // try and get the tickets
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_QUERY);

            // add to the array
            while (rs.next()) {
                TicketClass ticketClass = switch(rs.getInt("TicketClass")) {
                    case 1 -> TicketClass.ECONOMY;
                    case 2 -> TicketClass.BUSINESS;
                    default -> TicketClass.NONE;
                };

                String name = rs.getString("Name");
                String destination = rs.getString("Destination");
                double price = rs.getDouble("Price");
                findTickets.add(new TrainTicket(name, destination, ticketClass, price));
            }
        } catch (SQLException e) {
            System.out.println("could not get the tickes");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "was unable to get the tickets from the database");
            return null;
        }

        // get the destination to filter with
        String dest = findDestTextField.getText();
        if (dest.isEmpty()) {
            JOptionPane.showMessageDialog(null, "must enter a destination");
            return null;
        }

        // filter the tickets
        ArrayList<TrainTicket> filtered = new ArrayList<>(findTickets.size());
        for (TrainTicket ticket : findTickets) {
            // add if matches
            if (ticket.getDestination().equals(dest)) filtered.add(ticket);
        }
        return filtered;
    }
}

package org.Eduvos.Q2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

public class UI extends JFrame{
    private JPanel contentPanel;
    private JLabel passengerNameLbl;
    private JLabel destinationLbl;
    private JLabel ticketClassLbl;
    private JTextArea ticketInfoTextArea;
    private JButton addTicketBtn;
    private JTextField passengerNameTextField;
    private JTextField destinationTextField;
    // use a combo box as this stops users from
    // entering an invalid option for the ticket class
    private JComboBox<String> ticketClasscomboBox;

    // store tickets
    private final ArrayList<TrainTicket> tickets = new ArrayList<>();
    // use to build the string of tickets to show to the user
    private final StringBuilder sb = new StringBuilder();

    public UI() {
        setMinimumSize(new Dimension(500, 500));
        setResizable(true);
        setTitle("Train Ticket Booker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(contentPanel);

        // set combobox default to nothig
        // https://stackoverflow.com/questions/6276177/jcombobox-want-first-entry-to-be-blank-entry
        ticketClasscomboBox.setSelectedIndex(-1);

        addTicketBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // get the ticket
                TrainTicket ticket = getTrainTicket();
                if (ticket == null) return;
                tickets.add(ticket);

                // only add the most recent ticket
                sb.append(ticket);
                sb.append("\n--------------------------\n");

                // actually show the user
                ticketInfoTextArea.setText(sb.toString());

                // reset everything to a clean state
                ticketClasscomboBox.setSelectedIndex(-1);
                passengerNameTextField.setText("");
                destinationTextField.setText("");
            }
        });
    }

    private TrainTicket getTrainTicket() {
        String name = passengerNameTextField.getText();
        // make sure not empty
        if (Objects.equals(name, "")) {
            JOptionPane.showMessageDialog(null, "must enter passenger name");
            return null;
        }

        String destination = destinationTextField.getText();
        // make sure not empty
        if (Objects.equals(destination, "")) {
            JOptionPane.showMessageDialog(null, "must enter a destination");
            return null;
        }

        int idx = ticketClasscomboBox.getSelectedIndex();
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
}

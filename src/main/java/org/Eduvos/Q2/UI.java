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
    private JComboBox<String> ticketClasscomboBox;

    private ArrayList<TrainTicket> tickets = new ArrayList<>();

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
                TrainTicket ticket = getTrainTicket();
                if (ticket == null) return;
                tickets.add(ticket);

                // show all tickets
                StringBuilder sb = new StringBuilder();
                for (TrainTicket t : tickets) {
                    sb.append(t.toString());
                    sb.append("\n--------------------------\n");
                }

                ticketInfoTextArea.setText(sb.toString());

                // reset combobox to empty default
                ticketClasscomboBox.setSelectedIndex(-1);
            }
        });
    }

    private TrainTicket getTrainTicket() {
        String name = passengerNameTextField.getText();
        if (Objects.equals(name, "")) {
            JOptionPane.showMessageDialog(null, "must enter passenger name");
            return null;
        }

        String destination = destinationTextField.getText();
        if (Objects.equals(destination, "")) {
            JOptionPane.showMessageDialog(null, "must enter a destination");
            return null;
        }

        int idx = ticketClasscomboBox.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(null, "must choose either Economy or Business");
            return null;
        }
        System.out.printf("selected idx: %d\n", idx);
        TicketClass ticketClass = switch (idx) {
            case 0 -> TicketClass.ECONOMY;
            case 1 -> TicketClass.BUSINESS;
            default -> TicketClass.NONE;
        };

        return new TrainTicket(name, destination, ticketClass, 100);
    }
}

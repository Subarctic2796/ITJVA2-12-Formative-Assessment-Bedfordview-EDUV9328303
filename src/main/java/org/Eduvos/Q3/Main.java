package org.Eduvos.Q3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        System.out.print("Enter passenger name: ");
        String name = reader.readLine();
        if (name == null) return;

        System.out.print("Enter destination: ");
        String destination = reader.readLine();
        if (destination == null) return;

        TicketClass ticket_class = TicketClass.NONE;
        while (ticket_class == TicketClass.NONE) {
            System.out.print("Enter class (Economy/Business): ");
            String ticket_class_str = reader.readLine();
            if (ticket_class_str == null) return;

            switch (ticket_class_str) {
                case "Economy":  ticket_class = TicketClass.ECONOMY; break;
                case "Business": ticket_class = TicketClass.BUSINESS; break;
                default: {
                    System.out.println("ticket class can only be 'Economy' or 'Business'");
                } break;
            }
        }


        double base_price = -1.0;
        while (base_price == -1.0) {
            try {
                System.out.print("Enter base price: ");
                String base_price_str = reader.readLine();
                if (base_price_str == null) return;

                base_price = Double.parseDouble(base_price_str);
                if (base_price <= 0.0) {
                    System.out.println("base price can only be a positive number");
                    base_price = -1.0;
                }
            } catch (Exception e) {
                System.out.println("invalid base price, try again");
                base_price = -1.0;
            }
        }

        TrainTicket ticket = new TrainTicket(name, destination, ticket_class, base_price);

        // show train ticket details
        System.out.println("==== Train Ticket Details ====");
        System.out.println(ticket);
    }
}
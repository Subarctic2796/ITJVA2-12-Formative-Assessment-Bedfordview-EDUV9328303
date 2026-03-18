package org.Eduvos.Q2;

public class TrainTicket {
    private String name;
    private String destination;
    private TicketClass ticket_class;
    private double base_price;

    TrainTicket(String name, String destination, TicketClass ticket_class, double base_price) {
        this.name = name;
        this.destination = destination;
        this.ticket_class = ticket_class;
        this.base_price = base_price;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public TicketClass getTicket_class() { return ticket_class; }
    public void setTicket_class(TicketClass ticket_class) { this.ticket_class = ticket_class; }

    public double getBase_price() { return base_price; }
    public void setBase_price(double base_price) { this.base_price = base_price; }

    @Override
    public String toString() {
        double price = base_price;
        if (ticket_class == TicketClass.BUSINESS) price *= 1.5;
        return "Passenger Name: %s\n".formatted(name)
                + "Destination: %s\n".formatted(destination)
                + "Class: %s\n".formatted(ticket_class)
                + "Total Price: R%.2f".formatted(price);

    }

}

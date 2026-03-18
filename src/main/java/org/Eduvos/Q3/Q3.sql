CREATE DATABASE IF NOT EXISTS TrainTicketsDB;

CREATE TABLE IF NOT EXISTS Tickets (
    TicketID int NOT NULL AUTO_INCREMENT,
    TicketClass int DEFAULT 0, -- 0 -> NONE, 1 -> Economy 2 -> Business
    Name varchar(255) NOT NULL,
    Destination varchar(255) NOT NULL,
    Price double DEFAULT 100.00,
    PRIMARY KEY (TicketID)
);
package org.Eduvos.Q3;

public enum TicketClass {
    NONE,
    ECONOMY,
    BUSINESS;

    @Override
    public String toString() {
        return switch(this) {
            case NONE     -> "NONE";
            case ECONOMY  -> "Economy";
            case BUSINESS -> "Business";
        };
    }
}

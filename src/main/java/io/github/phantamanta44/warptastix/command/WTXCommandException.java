package io.github.phantamanta44.warptastix.command;

public class WTXCommandException extends Exception {

    public WTXCommandException(String message) {
        super(message);
    }

    public WTXCommandException() {
        this("usage");
    }

}

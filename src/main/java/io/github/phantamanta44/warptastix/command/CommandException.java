package io.github.phantamanta44.warptastix.command;

public class CommandException extends Exception {

    public CommandException(String message) {
        super(message);
    }

    public CommandException() {
        this("usage");
    }

}

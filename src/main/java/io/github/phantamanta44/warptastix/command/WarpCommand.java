package io.github.phantamanta44.warptastix.command;

import org.bukkit.command.CommandSender;

public class WarpCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws CommandException {
        switch (args.length) {
            case 1:
                // TODO Implement
                break;
            case 2:
                // TODO Implement
                break;
            default:
                throw new CommandException();
        }
    }

}

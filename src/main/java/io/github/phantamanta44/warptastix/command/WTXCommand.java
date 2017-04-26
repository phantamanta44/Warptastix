package io.github.phantamanta44.warptastix.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class WTXCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            execute(sender, args);
        } catch (CommandException e) {
            if (e.getMessage().equals("usage"))
                sender.sendMessage(ChatColor.RED + "Usage: " + command.getUsage());
            else
                sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    protected abstract void execute(CommandSender sender, String[] args) throws CommandException;

}
package io.github.phantamanta44.warptastix.command.condition;

import io.github.phantamanta44.warptastix.command.WTXCommandException;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface ICondition {

    void verify(CommandSender sender) throws WTXCommandException;

    default void execute(CommandSender sender) {
        // NO-OP
    }

}

package io.github.phantamanta44.warptastix.command.condition;

import io.github.phantamanta44.warptastix.command.WTXCommandException;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface ICondition {

    void verify(CommandSender sender) throws WTXCommandException;

    default void execute(CommandSender sender) {
        // NO-OP
    }

    default ICondition and(ICondition other) {
        final ICondition original = this;
        return new ICondition() {
            @Override
            public void verify(CommandSender sender) throws WTXCommandException {
                original.verify(sender);
                other.verify(sender);
            }

            @Override
            public void execute(CommandSender sender) {
                original.execute(sender);
                other.execute(sender);
            }
        };
    }

}

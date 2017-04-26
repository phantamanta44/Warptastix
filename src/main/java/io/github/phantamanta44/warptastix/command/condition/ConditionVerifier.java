package io.github.phantamanta44.warptastix.command.condition;

import io.github.phantamanta44.warptastix.command.WTXCommandException;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.LinkedList;

public class ConditionVerifier {

    private final Collection<ICondition> executors = new LinkedList<>();
    private CommandSender sender;

    public void check(ICondition condition) throws WTXCommandException {
        condition.verify(sender);
        executors.add(condition);
    }

    public void flush() {
        executors.forEach(e -> e.execute(sender));
    }

    public void setSender(CommandSender sender) {
        executors.clear();
        this.sender = sender;
    }

}

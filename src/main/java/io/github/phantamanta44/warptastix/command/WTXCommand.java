package io.github.phantamanta44.warptastix.command;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.command.condition.ConditionVerifier;
import io.github.phantamanta44.warptastix.command.condition.ICondition;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class WTXCommand implements CommandExecutor {

    private final ConditionVerifier verifier = new ConditionVerifier();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        verifier.setSender(sender);
        try {
            execute(sender, args);
        } catch (WTXCommandException e) {
            if (e.getMessage().equals("usage"))
                WTXLang.send(sender, "command.usage", command.getUsage());
            else
                sender.sendMessage(WTXLang.localize("prefix") + e.getMessage());
        }
        return true;
    }

    protected abstract void execute(CommandSender sender, String[] args) throws WTXCommandException;

    protected void verify(ICondition condition) throws WTXCommandException {
        verifier.check(condition);
    }

    protected void flushConditions() {
        verifier.flush();
    }

}
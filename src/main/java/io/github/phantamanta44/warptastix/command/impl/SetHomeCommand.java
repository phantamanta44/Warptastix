package io.github.phantamanta44.warptastix.command.impl;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommand;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.data.WTXAction;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws WTXCommandException {
        verify(Conditions.playerOnly());
        OfflinePlayer owner;
        switch (args.length) {
            case 0:
                verify(Conditions.self(WTXAction.HOME_SET));
                verify(Conditions.price(WTXAction.HOME_SET));
                owner = (Player)sender;
                break;
            case 1:
                verify(Conditions.otherPlayer(WTXAction.HOME_SET));
                owner = Warptastix.getPlayer(args[0]);
                if (owner == null)
                    throw new WTXCommandException(WTXLang.localize("command.noplayer", args[0]));
                break;
            default:
                throw new WTXCommandException();
        }
        if (Warptastix.hdb().forPlayer(owner) != null) {
            throw owner.equals(sender)
                    ? new WTXCommandException(WTXLang.localize("command.sethome.exists"))
                    : new WTXCommandException(WTXLang.localize("command.sethome.exists.other", owner.getName()));
        }
        flushConditions();
        Warptastix.hdb().set(owner, ((Player)sender).getLocation());
        Warptastix.hdb().save();
        if (owner.equals(sender))
            WTXLang.send(sender, "command.sethome.set");
        else
            WTXLang.send(sender, "command.sethome.set.other", owner.getName());
    }

}

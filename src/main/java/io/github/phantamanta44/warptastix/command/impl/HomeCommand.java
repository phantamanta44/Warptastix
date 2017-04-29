package io.github.phantamanta44.warptastix.command.impl;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommand;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.data.WTXAction;
import io.github.phantamanta44.warptastix.util.LazyLoc;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws WTXCommandException {
        OfflinePlayer owner = null;
        Player target = null;
        switch (args.length) {
            case 2:
                verify(Conditions.otherPlayer(WTXAction.HOME));
                target = Bukkit.getPlayerExact(args[1]);
                if (target == null)
                    throw new WTXCommandException(WTXLang.localize("command.noplayer", args[1]));
            case 1:
                verify(Conditions.permission("warptastix.home.intrude"));
                owner = Warptastix.getPlayer(args[0]);
                if (owner == null)
                    throw new WTXCommandException(WTXLang.localize("command.noplayer", args[0]));
                if (args.length == 2)
                    break;
            case 0:
                verify(Conditions.playerOnly());
                verify(Conditions.self(WTXAction.HOME));
                if (args.length == 0) {
                    verify(Conditions.price(WTXAction.HOME));
                    owner = (Player)sender;
                }
                target = (Player)sender;
                break;
            default:
                throw new WTXCommandException();
        }
        LazyLoc home = Warptastix.hdb().forPlayer(owner);
        if (home == null) {
            if (sender.equals(target) && target.equals(owner))
                throw new WTXCommandException(WTXLang.localize("command.delhome.nohome"));
            else
                throw new WTXCommandException(WTXLang.localize("command.delhome.nohome.other", owner.getName()));
        }
        if (!home.isWorldLoaded())
            throw new WTXCommandException(WTXLang.localize("command.home.unloaded"));
        flushConditions();
        Warptastix.teleport(target, home.getLocation());
        if (!target.equals(sender))
            WTXLang.send(sender, "command.home.home.other", target.getName(), owner.getName());
        if (target.equals(owner))
            WTXLang.send(target, "command.home.home");
        else
            WTXLang.send(target, "command.home.home.intrude", owner.getName());
    }

}

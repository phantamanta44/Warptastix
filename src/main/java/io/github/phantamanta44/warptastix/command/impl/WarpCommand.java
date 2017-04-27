package io.github.phantamanta44.warptastix.command.impl;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommand;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.data.WTXAction;
import io.github.phantamanta44.warptastix.data.Warp;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws WTXCommandException {
        Player target;
        switch (args.length) {
            case 1:
                verify(Conditions.playerOnly());
                verify(Conditions.self(WTXAction.WARP));
                verify(Conditions.cooldown(WTXAction.WARP));
                verify(Conditions.price(WTXAction.WARP));
                target = (Player)sender;
                break;
            case 2:
                verify(Conditions.otherPlayer(WTXAction.WARP));
                target = Bukkit.getPlayerExact(args[1]);
                if (target == null)
                    throw new WTXCommandException(WTXLang.prefix("command.noplayer", args[1]));
                break;
            default:
                throw new WTXCommandException();
        }
        Warp warp = Warptastix.wdb().byName(args[0]);
        if (warp == null)
            throw new WTXCommandException(WTXLang.prefix("command.nowarp", args[0]));
        if (!warp.getLocation().isWorldLoaded())
            throw new WTXCommandException(WTXLang.prefix("command.unloaded"));
        verify(Conditions.privateAccess(warp));
        flushConditions();
        target.teleport(warp.getLocation().getLocation()); // TODO Warptastic magic
        // TODO Sound effects
        // TODO Warp effect
        warp.incrementUses();
        if (!target.equals(sender))
            WTXLang.send(sender, "warp.warp.other", target.getName(), warp.getName());
        WTXLang.send(target, "warp.warp", warp.getName());
    }

}

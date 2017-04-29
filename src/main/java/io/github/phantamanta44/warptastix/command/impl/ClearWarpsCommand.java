package io.github.phantamanta44.warptastix.command.impl;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommand;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.data.WTXAction;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class ClearWarpsCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws WTXCommandException {
        OfflinePlayer target;
        if (args.length == 0) {
            verify(Conditions.playerOnly());
            verify(Conditions.self(WTXAction.WARP_CLEAR));
            target = (OfflinePlayer)sender;
        } else {
            verify(Conditions.otherPlayer(WTXAction.WARP_CLEAR));
            target = Warptastix.getPlayer(args[0]);
            if (target == null)
                throw new WTXCommandException(WTXLang.localize("command.noplayer", args[0]));
        }
        Warptastix.wdb().removeIf(w -> w.getOwner().equals(target.getUniqueId()));
        WTXLang.send(sender, "clearwarps.clear", target.getName());
    }

}

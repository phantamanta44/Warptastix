package io.github.phantamanta44.warptastix.command;

import io.github.phantamanta44.warptastix.Warptastix;
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
                    throw new WTXCommandException("No such player \"" + args[1] + "\"!");
                break;
            default:
                throw new WTXCommandException();
        }
        Warp warp = Warptastix.wdb().byName(args[0]);
        if (warp == null)
            throw new WTXCommandException("No such warp \"" + args[0] + "\"!");
        if (!warp.getLocation().isWorldLoaded())
            throw new WTXCommandException("This warp goes to a non-existent world!");
        verify(Conditions.privateAccess(warp));
        flushConditions();
        target.teleport(warp.getLocation().getLocation()); // TODO Warptastic magic
        // TODO Warp effect
        // TODO Send confirmation message(s)
    }

}

package io.github.phantamanta44.warptastix.command.impl;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommand;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.config.WTXConfig;
import io.github.phantamanta44.warptastix.data.WTXAction;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws WTXCommandException {
        Player target;
        switch (args.length) {
            case 0:
                verify(Conditions.playerOnly());
                verify(Conditions.self(WTXAction.SPAWN));
                verify(Conditions.price(WTXAction.SPAWN));
                target = (Player)sender;
                break;
            case 1:
                verify(Conditions.otherPlayer(WTXAction.SPAWN));
                target = Bukkit.getServer().getPlayerExact(args[0]);
                if (target == null)
                    throw new WTXCommandException(WTXLang.localize("command.noplayer", args[0]));
                break;
            default:
                throw new WTXCommandException();
        }
        flushConditions();
        Warptastix.teleport(target, WTXConfig.SPAWN.getSpawnCentered(target.getWorld()));
        if (target.equals(sender))
            WTXLang.send(sender, "command.spawn.spawn");
        else
            WTXLang.send(sender, "command.spawn.spawn.other", target.getName());
    }

}

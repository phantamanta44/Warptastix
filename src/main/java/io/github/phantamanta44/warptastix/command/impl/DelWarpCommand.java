package io.github.phantamanta44.warptastix.command.impl;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommand;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.data.Warp;
import org.bukkit.command.CommandSender;

public class DelWarpCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws WTXCommandException {
        if (args.length != 1)
            throw new WTXCommandException();
        Warp warp = Warptastix.wdb().byName(args[0]);
        if (warp == null)
            throw new WTXCommandException(WTXLang.localize("command.nowarp", args[0]));
        verify(Conditions.deletion(warp));
        Warptastix.wdb().remove(warp);
        Warptastix.wdb().save();
        WTXLang.send(sender, "command.delwarp.delete", warp.getName());
    }

}

package io.github.phantamanta44.warptastix.command.impl;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommand;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.data.Warp;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;

public class WarpInfoCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws WTXCommandException {
        verify(Conditions.permission("warptastix.info"));
        if (args.length != 1)
            throw new WTXCommandException();
        Warp warp = Warptastix.wdb().byName(args[0]);
        if (warp == null)
            throw new WTXCommandException(WTXLang.localize("command.nowarp", args[0]));
        if (warp.isPriv())
            verify(Conditions.privateAccess(warp));
        List<String> msg = new LinkedList<>();
        msg.add(WTXLang.localize("command.warpinfo.name", warp.getName()));
        if (warp.isServer()) {
            msg.add(WTXLang.localize("command.warpinfo.owner", WTXLang.localize("command.warpinfo.server")));
        } else {
            msg.add(WTXLang.localize("command.warpinfo.owner", warp.getOwnerName()));
            msg.add(WTXLang.localize("command.warpinfo.private", WTXLang.localize(warp.isPriv() ? "on" : "off")));
        }
        msg.add(WTXLang.localize("command.warpinfo.createtime", WTXLang.date(warp.getCreateTime())));
        msg.add(WTXLang.localize("command.warpinfo.uses", warp.getUses()));
        sender.sendMessage(msg.toArray(new String[msg.size()]));
    }

}

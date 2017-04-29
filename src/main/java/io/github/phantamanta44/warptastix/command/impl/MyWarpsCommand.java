package io.github.phantamanta44.warptastix.command.impl;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommand;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.data.WTXAction;
import io.github.phantamanta44.warptastix.data.Warp;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class MyWarpsCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws WTXCommandException {
        verify(Conditions.playerOnly());
        verify(Conditions.self(WTXAction.WARP_LIST));
        int page = 0;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException e) {
                throw new WTXCommandException(WTXLang.localize("command.warps.invalidpage", args[1]));
            }
        }
        List<Warp> warpList = Warptastix.wdb().byOwner((Player)sender).collect(Collectors.toList());
        int pages = (int)Math.ceil((float)warpList.size() / 8F);
        if (page < 0 || page >= pages)
            throw new WTXCommandException(WTXLang.localize("command.warps.invalidpage", Integer.toString(page + 1)));
        int pageSize = Math.min(8, warpList.size() - page * 8);
        String[] msg = new String[3 + pageSize];
        msg[0] = WTXLang.localize("command.warps.header");
        for (int i = 8 * page; i < 8 * page + pageSize; i++) {
            msg[i - 8 * page + 1] = WTXLang.localize("command.warps.entry",
                    warpList.get(i).getName(), warpList.get(i).getLocation());
        }
        msg[msg.length - 2] = WTXLang.localize("command.warps.footer", page + 1, pages);
        msg[msg.length - 1] = WTXLang.localize("command.warps.line");
        sender.sendMessage(msg);
    }

}

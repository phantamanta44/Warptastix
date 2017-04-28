package io.github.phantamanta44.warptastix.command.impl;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommand;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.command.opt.OptParser;
import io.github.phantamanta44.warptastix.data.WTXAction;
import io.github.phantamanta44.warptastix.data.Warp;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WarpsCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws WTXCommandException {
        if (!sender.hasPermission("warptastix.list"))
            throw new WTXCommandException(WTXLang.localize("noperms"));
        OptParser opts = new OptParser(args).with("pop").with("s").parse();
        args = opts.getArgs();
        int page = 0;
        Predicate<Warp> ownerFilter;
        switch (args.length) {
            case 0:
                verify(Conditions.playerOnly());
                verify(Conditions.self(WTXAction.WARP_LIST));
                ownerFilter = warp -> true;
                break;
            case 1:
                try {
                    page = Integer.parseInt(args[0]) - 1;
                    verify(Conditions.playerOnly());
                    verify(Conditions.self(WTXAction.WARP_LIST));
                    ownerFilter = warp -> true;
                } catch (NumberFormatException e) {
                    verify(Conditions.otherPlayer(WTXAction.WARP_LIST));
                    OfflinePlayer ownerPl = Warptastix.getPlayer(args[0]);
                    if (ownerPl == null)
                        throw new WTXCommandException(WTXLang.localize("command.noplayer", args[0]));
                    ownerFilter = warp -> !warp.isServer() && warp.getOwner().equals(ownerPl.getUniqueId());
                }
                break;
            case 2:
                verify(Conditions.otherPlayer(WTXAction.WARP_LIST));
                OfflinePlayer ownerPl = Warptastix.getPlayer(args[0]);
                if (ownerPl == null)
                    throw new WTXCommandException(WTXLang.localize("command.noplayer", args[0]));
                ownerFilter = warp -> !warp.isServer() && warp.getOwner().equals(ownerPl.getUniqueId());
                try {
                    page = Integer.parseInt(args[1]) - 1;
                } catch (NumberFormatException e) {
                    throw new WTXCommandException(WTXLang.localize("command.warp.invalidpage", args[1]));
                }
                break;
            default:
                throw new WTXCommandException();
        }
        if (opts.has("s"))
            ownerFilter = Warp::isServer;
        Stream<Warp> warps = Warptastix.wdb().warps().filter(ownerFilter);
        if (opts.has("pop"))
            warps = warps.sorted(Warp.byPopularity());
        List<Warp> warpList = warps.collect(Collectors.toList());
        if (page < 0 || page >= (int)Math.ceil((float)warpList.size() / 8F))
            throw new WTXCommandException(WTXLang.localize("command.warp.invalidpage", Integer.toString(page)));
        String[] msg = new String[3 + Math.min(8, warpList.size() - page * 8)];
        msg[0] = WTXLang.localize("command.warps.header");
        for (int i = 0; i < warpList.size(); i++) {
            msg[i + 1] = WTXLang.localize("command.warps.entry",
                    warpList.get(i).getName(), warpList.get(i).getOwnerName());
        }
        msg[msg.length - 2] = WTXLang.localize("command.warps.footer");
        msg[msg.length - 1] = WTXLang.localize("command.warps.line");
        sender.sendMessage(msg);
    }

}

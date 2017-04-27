package io.github.phantamanta44.warptastix.command.impl;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommand;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.command.opt.OptParser;
import io.github.phantamanta44.warptastix.data.WTXAction;
import io.github.phantamanta44.warptastix.data.Warp;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws WTXCommandException {
        verify(Conditions.playerOnly());
        OptParser opts = new OptParser(args).with("p").with("s").parse();
        args = opts.getArgs();
        if (opts.has("p") && !sender.hasPermission("warptastix.setwarp.private"))
            throw new WTXCommandException(WTXLang.localize("noperms"));
        if (opts.has("s")) {
            if (!sender.hasPermission("warp.setwarp.server"))
                throw new WTXCommandException(WTXLang.localize("noperms"));
            if (args.length != 1)
                throw new WTXCommandException();
            Warp warp = Warptastix.wdb().byName(args[0]);
            if (warp != null) {
                if (warp.getOwner() == null) {
                    throw new WTXCommandException(WTXLang.localize("command.setwarp.server.already", warp.getName()));
                } else {
                    warp.setOwner(null);
                    warp.setPriv(false);
                    Warptastix.wdb().save();
                    WTXLang.send(sender, "command.setwarp.server.assimilate", warp.getName());
                }
            } else {
                Warptastix.wdb().add(warp = new Warp(args[0], (Player)sender, false));
                Warptastix.wdb().save();
                WTXLang.send(sender, "command.setwarp.server.set", warp.getName());
            }
        } else {
            WTXAction action = opts.has("p") ? WTXAction.WARP_SET_PRIVATE : WTXAction.WARP_SET_PUBLIC;
            OfflinePlayer owner;
            switch (args.length) {
                case 1:
                    verify(Conditions.self(action));
                    verify(Conditions.price(action));
                    owner = (OfflinePlayer)sender;
                    break;
                case 2:
                    verify(Conditions.otherPlayer(action));
                    owner = Bukkit.getPlayerExact(args[1]);
                    if (owner == null)
                        throw new WTXCommandException(WTXLang.localize("command.noplayer", args[1]));
                    break;
                default:
                    throw new WTXCommandException();
            }
            Warp warp = Warptastix.wdb().byName(args[0]);
            if (warp != null) {
                if (opts.has("p")) {
                    if (warp.getOwner() == null)
                        throw new WTXCommandException(WTXLang.localize("command.setwarp.server.already", warp.getName()));
                    if (owner.getUniqueId().equals(warp.getOwner())) {
                        warp.setPriv(!warp.isPriv());
                        Warptastix.wdb().save();
                        WTXLang.send(sender, "command.setwarp.privtoggle",
                                WTXLang.localize(warp.isPriv() ? "on" : "off"), warp.getName());
                    } else {
                        WTXLang.send(sender, "command.setwarp.notowner",
                                Bukkit.getServer().getOfflinePlayer(warp.getOwner()).getName());
                    }
                } else {
                    throw new WTXCommandException(WTXLang.localize("command.setwarp.already", warp.getName()));
                }
            } else { // TODO Check warp limits
                flushConditions();
                Warptastix.wdb().add(warp = new Warp(args[0], (Player)sender, opts.has("p")));
                Warptastix.wdb().save();
                if (owner.equals(sender))
                    WTXLang.send(sender, "command.setwarp.set", warp.getName());
                else
                    WTXLang.send(sender, "command.setwarp.set.other", warp.getName(), owner.getName());
            }
        }
    }

}

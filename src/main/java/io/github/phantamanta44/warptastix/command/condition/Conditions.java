package io.github.phantamanta44.warptastix.command.condition;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.config.WTXConfig;
import io.github.phantamanta44.warptastix.data.WTXAction;
import io.github.phantamanta44.warptastix.data.Warp;
import io.github.phantamanta44.warptastix.util.VaultUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class Conditions {

    public static ICondition noop() {
        return sender -> {};
    }

    public static ICondition privateAccess(Warp warp) {
        return warp.isServer() ? noop() : predicate(sender ->
                        (sender instanceof Entity && ((Entity)sender).getUniqueId().equals(warp.getOwner()))
                                || sender.hasPermission("warptastix.warp.bypassprivate"),
                WTXLang.localize("command.warp.private", warp.getName()));
    }

    public static ICondition deletion(Warp warp) {
        return sender -> {
            if (warp.isServer()) {
                if (!sender.hasPermission("warptastix.setwarp.server"))
                    throw new WTXCommandException(WTXLang.localize("command.delwarp.noaccess", warp.getName()));
            } else {
                if (!((sender instanceof Entity && ((Entity)sender).getUniqueId().equals(warp.getOwner()))
                                || sender.hasPermission("warptastix.setwarp.other"))) {
                    throw new WTXCommandException(WTXLang.localize("command.delwarp.noaccess",warp.getName()));
                }
            }
        };
    }

    public static ICondition price(WTXAction action) {
        return new ICondition() {
            @Override
            public void verify(CommandSender sender) throws WTXCommandException {
                if (WTXConfig.ECON.isEnabled()
                        && !sender.hasPermission("warptastix.nocost")
                        && !VaultUtils.hasMoney((OfflinePlayer)sender, WTXConfig.ECON.getPrice(action))) {
                    throw new WTXCommandException(WTXLang.localize(
                            "money.cannotafford", VaultUtils.formatMoney(WTXConfig.ECON.getPrice(action))));
                }
            }

            @Override
            public void execute(CommandSender sender) {
                double price = WTXConfig.ECON.getPrice(action);
                if (price > 0 && WTXConfig.ECON.isEnabled() && !sender.hasPermission("warptastix.nocost")) {
                    VaultUtils.offsetMoney((OfflinePlayer)sender, price);
                    WTXLang.send(sender, "money.spent",
                            VaultUtils.formatMoney(price));
                    WTXLang.send(sender, "money.balance",
                            VaultUtils.formatMoney(VaultUtils.balanceOf((OfflinePlayer)sender)));
                }
            }
        };
    }

    public static ICondition self(WTXAction action) {
        switch (action) {
            case WARP:
                return permission("warptastix.warp");
            case WARP_SET_PUBLIC:
                return permission("warptastix.setwarp.public");
            case WARP_SET_PRIVATE:
                return permission("warptastix.setwarp.private");
            case WARP_LIST:
                return permission("warptastix.list");
            case WARP_CLEAR:
                return permission("warptastix.clear");
            case HOME:
            case HOME_SET:
                return permission("warptastix.home");
            case SPAWN:
                return permission("warptastix.spawn");
            default:
                return noop();
        }
    }

    public static ICondition otherPlayer(WTXAction action) {
        switch (action) {
            case WARP:
                return permission("warptastix.warp.other");
            case WARP_SET_PUBLIC:
            case WARP_SET_PRIVATE:
                return permission("warptastix.setwarp.other").and(self(action));
            case WARP_LIST:
                return permission("warptastix.list.other");
            case WARP_CLEAR:
                return permission("warptastix.clear.other");
            case HOME:
                return permission("warptastix.home.other");
            case HOME_SET:
                return permission("warptastix.home.intrude");
            case SPAWN:
                return permission("warptastix.spawn.other");
            default:
                return noop();
        }
    }

    public static ICondition warpLimit(OfflinePlayer owner) {
        return sender -> {
            if (!(sender.hasPermission("warptastix.limit.unlimited")
                    || VaultUtils.hasPerm(owner, "warptastix.limit.unlimited"))) {
                if (Warptastix.wdb().byOwner(owner).count() >= WTXConfig.WARP.getLimit(owner))
                    throw new WTXCommandException(WTXLang.localize("command.setwarp.limit"));
            }
        };
    }

    public static ICondition permission(String node) {
        return predicate(sender -> sender.hasPermission(node), WTXLang.localize("noperms"));
    }

    public static ICondition playerOnly() {
        return predicate(sender -> sender instanceof Player, WTXLang.localize("command.playeronly"));
    }
    
    public static ICondition predicate(Predicate<CommandSender> predicate, String message) {
        return sender -> {
            if (!predicate.test(sender))
                throw new WTXCommandException(message);
        };
    }

}

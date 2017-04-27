package io.github.phantamanta44.warptastix.command.condition;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.data.WTXAction;
import io.github.phantamanta44.warptastix.data.Warp;
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
                WTXLang.prefix("command.warp.private", warp.getName()));
    }

    public static ICondition cooldown(WTXAction action) {
        return sender -> {}; // TODO Implement
    };

    public static ICondition price(WTXAction action) {
        return sender -> {}; // TODO Implement
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
            case HOME:
            case HOME_SET:
                return permission("warptastix.home.other");
            case SPAWN:
                return permission("warptastix.spawn.other");
            default:
                return noop();
        }
    }

    public static ICondition permission(String node) {
        return predicate(sender -> sender.hasPermission(node), WTXLang.prefix("noperms"));
    }

    public static ICondition playerOnly() {
        return predicate(sender -> sender instanceof Player, WTXLang.prefix("command.playeronly"));
    }
    
    public static ICondition predicate(Predicate<CommandSender> predicate, String message) {
        return sender -> {
            if (!predicate.test(sender))
                throw new WTXCommandException(message);
        };
    }

}

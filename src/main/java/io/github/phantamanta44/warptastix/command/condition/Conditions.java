package io.github.phantamanta44.warptastix.command.condition;

import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.data.WTXAction;
import io.github.phantamanta44.warptastix.data.Warp;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class Conditions {

    public static ICondition privateAccess(Warp warp) {
        return sender -> {}; // TODO Implement
    }

    public static ICondition cooldown(WTXAction action) {
        return sender -> {}; // TODO Implement
    };

    public static ICondition price(WTXAction action) {
        return sender -> {}; // TODO Implement
    }

    public static ICondition self(WTXAction action) {
        return sender -> {}; // TODO Implement
    }

    public static ICondition otherPlayer(WTXAction action) {
        return sender -> {}; // TODO Implement
    }

    public static ICondition permission(String node) {
        return predicate(sender -> sender.hasPermission(node), "No permission!");
    }

    public static ICondition playerOnly() {
        return predicate(sender -> sender instanceof Player, "Only players can use this command!");
    }
    
    public static ICondition predicate(Predicate<CommandSender> predicate, String message) {
        return sender -> {
            if (!predicate.test(sender))
                throw new WTXCommandException(message);
        };
    }

}

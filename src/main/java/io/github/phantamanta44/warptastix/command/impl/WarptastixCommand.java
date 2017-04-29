package io.github.phantamanta44.warptastix.command.impl;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommand;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.command.opt.OptParser;
import io.github.phantamanta44.warptastix.config.WTXConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class WarptastixCommand extends WTXCommand {

    @Override
    protected void execute(CommandSender sender, String[] args) throws WTXCommandException {
        OptParser opts = new OptParser(args).with("r").parse();
        if (opts.has("r")) {
            verify(Conditions.permission("warptastix.reload"));
            Warptastix.clearEffects();
            WTXConfig.load();
            WTXLang.load();
            Warptastix.wdb().load();
            Warptastix.hdb().load();
            WTXLang.send(sender, "command.reload");
        } else {
            sender.sendMessage(
                    ChatColor.LIGHT_PURPLE + "Warptastix " +
                            ChatColor.GRAY + Warptastix.INSTANCE.getDescription().getVersion());
        }
    }

}

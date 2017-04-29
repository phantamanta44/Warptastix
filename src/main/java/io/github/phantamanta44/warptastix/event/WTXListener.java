package io.github.phantamanta44.warptastix.event;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import io.github.phantamanta44.warptastix.command.condition.ConditionVerifier;
import io.github.phantamanta44.warptastix.command.condition.Conditions;
import io.github.phantamanta44.warptastix.config.WTXConfig;
import io.github.phantamanta44.warptastix.data.WTXAction;
import io.github.phantamanta44.warptastix.data.Warp;
import io.github.phantamanta44.warptastix.util.LazyLoc;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class WTXListener implements Listener {

    @EventHandler
    public void onDeath(PlayerRespawnEvent event) {
        if (WTXConfig.HOME.doHomeOnDeath()) {
            LazyLoc home = Warptastix.hdb().forPlayer(event.getPlayer());
            if (home != null && home.isWorldLoaded()) {
                event.setRespawnLocation(home.getLocation());
                return;
            }
        }
        if (WTXConfig.SPAWN.shouldSpawnAtSpawn())
            event.setRespawnLocation(event.getPlayer().getWorld().getSpawnLocation());
    }

    @EventHandler
    public void onSpawn(PlayerSpawnLocationEvent event) {
        if (WTXConfig.SPAWN.shouldSpawnAtSpawn() && !event.getPlayer().hasPlayedBefore())
            event.setSpawnLocation(event.getSpawnLocation().getWorld().getSpawnLocation());
    }

    @EventHandler
    public void onSignEdit(SignChangeEvent event) {
        if (WTXConfig.SIGN.isEnabled()) {
            if (event.getLine(1).equals(WTXConfig.SIGN.getWarpText())) {
                if (event.getPlayer().hasPermission("warptastix.makesign")) {
                    if (!event.getLine(2).isEmpty())
                        event.setLine(1, WTXConfig.SIGN.getWarpTitle());
                    else
                        WTXLang.send(event.getPlayer(), "sign.nowarp");
                } else {
                    WTXLang.send(event.getPlayer(), "noperms");
                    event.setCancelled(true);
                }
            } else if (event.getLine(1).equals(WTXConfig.SIGN.getSpawnText())) {
                if (event.getPlayer().hasPermission("warptastix.makesign")) {
                    event.setLine(1, WTXConfig.SIGN.getSpawnTitle());
                } else {
                    WTXLang.send(event.getPlayer(), "noperms");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (WTXConfig.SIGN.isEnabled()) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                    (event.getClickedBlock().getType() == Material.SIGN_POST
                            || event.getClickedBlock().getType() == Material.WALL_SIGN)) {
                Sign sign = (Sign)event.getClickedBlock().getState();
                if (sign.getLine(1).equals(WTXConfig.SIGN.getSpawnTitle())) {
                    try {
                        ConditionVerifier verifier = new ConditionVerifier();
                        verifier.setSender(event.getPlayer());
                        if (WTXConfig.SIGN.shouldCharge())
                            verifier.check(Conditions.price(WTXAction.SPAWN));
                        verifier.flush();
                        Warptastix.teleport(event.getPlayer(), event.getPlayer().getWorld().getSpawnLocation());
                        WTXLang.send(event.getPlayer(), "command.spawn.spawn");
                    } catch (WTXCommandException e) {
                        event.getPlayer().sendMessage(WTXLang.localize("prefix") + e.getMessage());
                    }
                } else if (sign.getLine(1).equals(WTXConfig.SIGN.getWarpTitle())) {
                    Warp warp = Warptastix.wdb().byName(sign.getLine(2));
                    if (warp == null) {
                        WTXLang.send(event.getPlayer(), "command.nowarp", sign.getLine(2));
                    } else if (!warp.getLocation().isWorldLoaded()) {
                        WTXLang.send(event.getPlayer(), "command.warp.unloaded");
                    } else {
                        try {
                            ConditionVerifier verifier = new ConditionVerifier();
                            verifier.setSender(event.getPlayer());
                            if (warp.isPriv())
                                verifier.check(Conditions.privateAccess(warp));
                            if (WTXConfig.SIGN.shouldCharge())
                                verifier.check(Conditions.price(WTXAction.WARP));
                            verifier.flush();
                            Warptastix.teleport(event.getPlayer(), warp.getLocation().getLocation());
                            warp.incrementUses();
                            WTXLang.send(event.getPlayer(), "command.warp.warp", warp.getName());
                        } catch (WTXCommandException e) {
                            event.getPlayer().sendMessage(WTXLang.localize("prefix") + e.getMessage());
                        }
                    }
                }
            }
        }
    }

}

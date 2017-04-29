package io.github.phantamanta44.warptastix.event;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.config.WTXConfig;
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
            } else if (event.getLine(1).equals(WTXConfig.SIGN.getWarpText())) {
                if (!event.getPlayer().hasPermission("warptastix.makesign")) {
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

                } else if (sign.getLine(1).equals(WTXConfig.SIGN.getWarpTitle())) {

                }
            }
        }
    }

}

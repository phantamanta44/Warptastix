package io.github.phantamanta44.warptastix;

import io.github.phantamanta44.warptastix.event.EffectHandler;
import io.github.phantamanta44.warptastix.command.impl.*;
import io.github.phantamanta44.warptastix.config.WTXConfig;
import io.github.phantamanta44.warptastix.data.HomeDB;
import io.github.phantamanta44.warptastix.data.WarpDB;
import io.github.phantamanta44.warptastix.event.WTXListener;
import io.github.phantamanta44.warptastix.util.VaultUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Warptastix extends JavaPlugin {

    public static Warptastix INSTANCE;

    private WarpDB warpDb;
    private HomeDB homeDb;
    private WTXListener listener;
    private EffectHandler effector;

    public static WarpDB wdb() {
        return INSTANCE.warpDb;
    }

    public static HomeDB hdb() {
        return INSTANCE.homeDb;
    }

    public static OfflinePlayer getPlayer(String name) {
        OfflinePlayer pl = Bukkit.getServer().getOfflinePlayer(name);
        return pl.isOnline() || pl.hasPlayedBefore() ? pl : null;
    }

    public static void teleport(Player player, Location dest) {
        if (WTXConfig.EFFECT.isEnabled()) {
            INSTANCE.effector.teleport(player, dest);
        } else {
            Location loc = player.getLocation();
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
            player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 12);
            player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 18, EffectHandler.TP_EFF_MAT);
            player.teleport(dest);
            dest.getWorld().playSound(dest, Sound.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
            dest.getWorld().spawnParticle(Particle.SMOKE_NORMAL, dest, 12);
            dest.getWorld().spawnParticle(Particle.BLOCK_CRACK, dest, 18, EffectHandler.TP_EFF_MAT);
        }
    }

    public static void clearEffects() {
        INSTANCE.effector.clear();
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        VaultUtils.init();
        WTXConfig.load();
        WTXLang.load();
        warpDb = new WarpDB();
        homeDb = new HomeDB();
        Bukkit.getServer().getPluginCommand("warptastix").setExecutor(new WarptastixCommand());
        Bukkit.getServer().getPluginCommand("warp").setExecutor(new WarpCommand());
        Bukkit.getServer().getPluginCommand("warps").setExecutor(new WarpsCommand());
        Bukkit.getServer().getPluginCommand("mywarps").setExecutor(new MyWarpsCommand());
        Bukkit.getServer().getPluginCommand("warpinfo").setExecutor(new WarpInfoCommand());
        Bukkit.getServer().getPluginCommand("setwarp").setExecutor(new SetWarpCommand());
        Bukkit.getServer().getPluginCommand("delwarp").setExecutor(new DelWarpCommand());
        Bukkit.getServer().getPluginCommand("clearwarps").setExecutor(new ClearWarpsCommand());
        Bukkit.getServer().getPluginCommand("sethome").setExecutor(new SetHomeCommand());
        Bukkit.getServer().getPluginCommand("delhome").setExecutor(new DelHomeCommand());
        Bukkit.getServer().getPluginCommand("home").setExecutor(new HomeCommand());
        Bukkit.getServer().getPluginCommand("spawn").setExecutor(new SpawnCommand());
        Bukkit.getServer().getPluginManager().registerEvents(listener = new WTXListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(effector = new EffectHandler(), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(listener);
        HandlerList.unregisterAll(effector);
        clearEffects();
        warpDb.save();
        homeDb.save();
    }

}

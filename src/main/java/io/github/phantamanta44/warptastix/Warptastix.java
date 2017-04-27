package io.github.phantamanta44.warptastix;

import io.github.phantamanta44.warptastix.command.impl.SetWarpCommand;
import io.github.phantamanta44.warptastix.command.impl.WarpCommand;
import io.github.phantamanta44.warptastix.command.impl.WarpsCommand;
import io.github.phantamanta44.warptastix.command.impl.WarptastixCommand;
import io.github.phantamanta44.warptastix.config.WTXConfig;
import io.github.phantamanta44.warptastix.data.HomeDB;
import io.github.phantamanta44.warptastix.data.WarpDB;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Warptastix extends JavaPlugin {

    public static Warptastix INSTANCE;

    private WarpDB warpDb;
    private HomeDB homeDb;

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

    @Override
    public void onEnable() {
        INSTANCE = this;
        WTXConfig.load();
        warpDb = new WarpDB();
        homeDb = new HomeDB();
        Bukkit.getServer().getPluginCommand("warptastix").setExecutor(new WarptastixCommand());
        Bukkit.getServer().getPluginCommand("warp").setExecutor(new WarpCommand());
        Bukkit.getServer().getPluginCommand("warps").setExecutor(new WarpsCommand());
        Bukkit.getServer().getPluginCommand("setwarp").setExecutor(new SetWarpCommand());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        warpDb.save();
        homeDb.save();
    }

}

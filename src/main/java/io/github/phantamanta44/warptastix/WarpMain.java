package io.github.phantamanta44.warptastix;

import io.github.phantamanta44.warptastix.config.WarpConfig;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpMain extends JavaPlugin {

    public static WarpMain INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        WarpConfig.load();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

}

package io.github.phantamanta44.warptastix.config;

import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.Map;

public class WTXConfig {
    
    public static final Warp WARP = new Warp();
    public static final Home HOME = new Home();
    public static final Spawn SPAWN = new Spawn();
    public static final Effect EFFECT = new Effect();
    public static final Sign SIGN = new Sign();
    public static final Economy ECON = new Economy();

    public static void load() {
        Warptastix.INSTANCE.saveDefaultConfig();
        ConfigurationSection config = Warptastix.INSTANCE.getConfig();
        WARP.load(config.getConfigurationSection("Warps"));
        HOME.load(config.getConfigurationSection("Homes"));
        SPAWN.load(config.getConfigurationSection("Spawn"));
        EFFECT.load(config.getConfigurationSection("Effect"));
        SIGN.load(config.getConfigurationSection("Sign"));
        ECON.load(config.getConfigurationSection("Economy"));
    }
    
    public static class Warp {

        private int defaultLimit;
        private Map<Permission, Integer> limitPerms = new HashMap<>();
        private int warmup;
        private int cooldown;

        private void load(ConfigurationSection config) {
            if (config == null)
                config = new DummyConfig();
            defaultLimit = config.getInt("DefaultLimit", 3);
            limitPerms.keySet().forEach(Bukkit.getServer().getPluginManager()::removePermission);
            limitPerms.clear();
            config.getIntegerList("LimitNodes").stream()
                    .sorted()
                    .map(node -> Pair.of(new Permission("warptastix.limit." + Integer.toString(node)), node))
                    .peek(p -> limitPerms.put(p.getA(), p.getB()))
                    .map(Pair::getA)
                    .forEach(Bukkit.getServer().getPluginManager()::addPermission);
            warmup = (int)Math.floor(config.getDouble("Warmup", 0D) * 20);
            cooldown = (int)Math.floor(config.getDouble("Cooldown", 0D) * 20);
        }

        public int getDefaultLimit() {
            return defaultLimit;
        }

        public Map<Permission, Integer> getLimitPerms() {
            return limitPerms;
        }

        public int getWarmup() {
            return warmup;
        }

        public int getCooldown() {
            return cooldown;
        }

    }

    public static class Home {

        private boolean homeOnDeath;
        private int warmup;
        private int cooldown;

        private void load(ConfigurationSection config) {
            if (config == null)
                config = new DummyConfig();
            homeOnDeath = config.getBoolean("HomeOnDeath", false);
            warmup = (int)Math.floor(config.getDouble("Warmup", 0D) * 20);
            cooldown = (int)Math.floor(config.getDouble("Cooldown", 0D) * 20);
        }

        public boolean doHomeOnDeath() {
            return homeOnDeath;
        }

        public int getWarmup() {
            return warmup;
        }

        public int getCooldown() {
            return cooldown;
        }

    }

    public static class Spawn {

        private boolean spawnAtSpawn;
        private int warmup;
        private int cooldown;

        private void load(ConfigurationSection config) {
            if (config == null)
                config = new DummyConfig();
            spawnAtSpawn = config.getBoolean("SpawnAtSpawn", true);
            warmup = (int)Math.floor(config.getDouble("Warmup", 0D) * 20);
            cooldown = (int)Math.floor(config.getDouble("Cooldown", 0D) * 20);
        }

        public boolean isSpawnAtSpawn() {
            return spawnAtSpawn;
        }

        public int getWarmup() {
            return warmup;
        }

        public int getCooldown() {
            return cooldown;
        }

    }

    public static class Effect {

        private boolean enable;
        private int duration;

        private void load(ConfigurationSection config) {
            if (config == null)
                config = new DummyConfig();
            enable = config.getBoolean("Enable", true);
            duration = (int)Math.floor(config.getDouble("Duration", 1.5D) * 20);
        }

        public boolean isEnabled() {
            return enable;
        }

        public int getDuration() {
            return duration;
        }

    }

    public static class Sign {

        private boolean enable;
        private String warpTitle;
        private String spawnTitle;

        private void load(ConfigurationSection config) {
            if (config == null)
                config = new DummyConfig();
            enable = config.getBoolean("Enable", true);
            warpTitle = config.getString("WarpSignTitle", "[Warp]");
            spawnTitle = config.getString("SpawnSignTitle", "[Spawn]");
        }

        public boolean isEnabled() {
            return enable;
        }

        public String getWarpTitle() {
            return warpTitle;
        }

        public String getSpawnTitle() {
            return spawnTitle;
        }

    }

    public static class Economy {

        private boolean enable;
        private int warpSetPrice;
        private int warpPrice;
        private int homeSetPrice;
        private int homePrice;
        private int spawnPrice;

        private void load(ConfigurationSection config) {
            if (config == null)
                config = new DummyConfig();
            enable = config.getBoolean("Enable", true);
            warpSetPrice = config.getInt("Warp.SetPrice", 100);
            warpPrice = config.getInt("Warp.WarpPrice", 0);
            homeSetPrice = config.getInt("Home.SetPrice", 100);
            homePrice = config.getInt("Home.WarpPrice", 0);
            spawnPrice = config.getInt("Spawn.WarpPrice", 0);
        }

        public boolean isEnabled() {
            return enable;
        }

        public int getWarpSetPrice() {
            return warpSetPrice;
        }

        public int getWarpPrice() {
            return warpPrice;
        }

        public int getHomeSetPrice() {
            return homeSetPrice;
        }

        public int getHomePrice() {
            return homePrice;
        }

        public int getSpawnPrice() {
            return spawnPrice;
        }

    }

}

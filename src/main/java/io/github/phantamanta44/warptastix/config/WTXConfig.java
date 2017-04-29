package io.github.phantamanta44.warptastix.config;

import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.data.WTXAction;
import io.github.phantamanta44.warptastix.util.Pair;
import io.github.phantamanta44.warptastix.util.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class WTXConfig {
    
    public static final WarpConfig WARP = new WarpConfig();
    public static final HomeConfig HOME = new HomeConfig();
    public static final SpawnConfig SPAWN = new SpawnConfig();
    public static final EffectConfig EFFECT = new EffectConfig();
    public static final SignConfig SIGN = new SignConfig();
    public static final EconomyConfig ECON = new EconomyConfig();
    
    private static final ConfigurationSection DUMMY_CONF = new YamlConfiguration();

    public static void load() {
        Warptastix.INSTANCE.saveDefaultConfig();
        ConfigurationSection config = Warptastix.INSTANCE.getConfig();
        WARP.load(config.getConfigurationSection("Warps"));
        HOME.load(config.getConfigurationSection("Homes"));
        SPAWN.load(config.getConfigurationSection("Spawn"));
        EFFECT.load(config.getConfigurationSection("TeleportEffect"));
        SIGN.load(config.getConfigurationSection("Sign"));
        ECON.load(config.getConfigurationSection("Economy"));
    }
    
    public static class WarpConfig {

        private int defaultLimit;
        private List<Pair<Permission, Integer>> limitPerms = new LinkedList<>();

        private void load(ConfigurationSection config) {
            if (config == null)
                config = DUMMY_CONF;
            defaultLimit = config.getInt("DefaultLimit", 3);
            limitPerms.forEach(e -> Bukkit.getServer().getPluginManager().removePermission(e.getA()));
            limitPerms.clear();
            config.getIntegerList("LimitNodes").stream()
                    .sorted(Comparator.reverseOrder())
                    .map(node -> Pair.of(new Permission("warptastix.limit." + Integer.toString(node)), node))
                    .peek(limitPerms::add)
                    .map(Pair::getA)
                    .forEach(Bukkit.getServer().getPluginManager()::addPermission);
        }

        public int getLimit(OfflinePlayer pl) {
            return limitPerms.stream()
                    .filter(perm -> VaultUtils.hasPerm(pl, perm.getA()))
                    .map(Pair::getB)
                    .findFirst()
                    .orElse(defaultLimit);
        }
        
    }

    public static class HomeConfig {

        private boolean homeOnDeath;

        private void load(ConfigurationSection config) {
            if (config == null)
                config = DUMMY_CONF;
            homeOnDeath = config.getBoolean("HomeOnDeath", false);
        }

        public boolean doHomeOnDeath() {
            return homeOnDeath;
        }

    }

    public static class SpawnConfig {

        private boolean spawnAtSpawn;

        private void load(ConfigurationSection config) {
            if (config == null)
                config = DUMMY_CONF;
            spawnAtSpawn = config.getBoolean("SpawnAtSpawn", true);
        }

        public boolean shouldSpawnAtSpawn() {
            return spawnAtSpawn;
        }

    }

    public static class EffectConfig {

        private boolean enable;
        private int duration;

        private void load(ConfigurationSection config) {
            if (config == null)
                config = DUMMY_CONF;
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

    public static class SignConfig {

        private boolean enable;
        private String warpText, warpTitle;
        private String spawnText, spawnTitle;
        private boolean charge;

        private void load(ConfigurationSection config) {
            if (config == null)
                config = DUMMY_CONF;
            enable = config.getBoolean("Enable", true);
            warpText = config.getString("WarpSignText", "[Warp]");
            warpTitle = ChatColor.translateAlternateColorCodes('&', config.getString("WarpSignTitle", "&a[Warp]"));
            spawnText = config.getString("SpawnSignText", "[Spawn]");
            warpTitle = ChatColor.translateAlternateColorCodes('&', config.getString("SpawnSignTitle", "&a[Spawn]"));
            charge = config.getBoolean("ChargeOnSign", false);
        }

        public boolean isEnabled() {
            return enable;
        }

        public String getWarpText() {
            return warpText;
        }

        public String getWarpTitle() {
            return warpTitle;
        }

        public String getSpawnText() {
            return spawnText;
        }

        public String getSpawnTitle() {
            return spawnTitle;
        }
        
        public boolean shouldCharge() {
            return charge;
        }

    }

    public static class EconomyConfig {

        private boolean enable;
        private double warpSetPrice;
        private double warpPrice;
        private double homeSetPrice;
        private double homePrice;
        private double spawnPrice;

        private void load(ConfigurationSection config) {
            if (config == null)
                config = DUMMY_CONF;
            enable = config.getBoolean("Enable", true);
            warpSetPrice = config.getDouble("Warp.SetPrice", 100);
            warpPrice = config.getDouble("Warp.WarpPrice", 0);
            homeSetPrice = config.getDouble("Home.SetPrice", 100);
            homePrice = config.getDouble("Home.WarpPrice", 0);
            spawnPrice = config.getDouble("Spawn.WarpPrice", 0);
        }

        public boolean isEnabled() {
            return enable;
        }

        public double getPrice(WTXAction action) {
            switch (action) {
                case WARP:
                    return warpPrice;
                case WARP_SET_PUBLIC:
                case WARP_SET_PRIVATE:
                    return warpSetPrice;
                case HOME:
                    return homePrice;
                case HOME_SET:
                    return homeSetPrice;
                case SPAWN:
                    return spawnPrice;
                default:
                    return 0;
            }
        }
        
    }

}

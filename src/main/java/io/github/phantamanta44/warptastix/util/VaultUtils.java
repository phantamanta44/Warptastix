package io.github.phantamanta44.warptastix.util;

import io.github.phantamanta44.warptastix.Warptastix;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class VaultUtils {

    private static VaultUtils INSTANCE;

    public static void init() {
        INSTANCE = new VaultUtils();
    }

    public static boolean hasPerm(OfflinePlayer pl, String perm) {
        return INSTANCE.perms != null
                ? INSTANCE.perms.playerHas(null, pl, perm)
                : pl.isOnline() && pl.getPlayer().hasPermission(perm);
    }

    public static boolean hasPerm(OfflinePlayer pl, org.bukkit.permissions.Permission perm) {
        return hasPerm(pl, perm.getName());
    }

    public static double balanceOf(OfflinePlayer pl) {
        return INSTANCE.econ != null ? INSTANCE.econ.getBalance(pl) : 0D;
    }

    public static boolean hasMoney(OfflinePlayer pl, double amount) {
        return INSTANCE.econ != null && INSTANCE.econ.getBalance(pl) >= amount;
    }

    public static void offsetMoney(OfflinePlayer pl, double amount) {
        if (INSTANCE.econ != null && amount != 0) {
            if (amount > 0)
                INSTANCE.econ.depositPlayer(pl, amount);
            else
                INSTANCE.econ.withdrawPlayer(pl, amount);
        }
    }

    public static String formatMoney(double amount) {
        return INSTANCE.econ != null ? INSTANCE.econ.format(amount) : String.format("$%.2f", amount);
    }

    private final Permission perms;
    private final Economy econ;

    private VaultUtils() {
        Warptastix.INSTANCE.getLogger().info("Initializing Vault integration...");
        this.perms = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        if (this.perms == null)
            Warptastix.INSTANCE.getLogger().warning("No permissions manager detected; offline player permissions won't work!");
        this.econ = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        if (this.econ == null)
            Warptastix.INSTANCE.getLogger().warning("No economy plugin detected; economy integration won't work!");
    }

}

package io.github.phantamanta44.warptastix;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class WTXLang { // TODO Configurable locale

    private static final Map<String, String> l10nStore = new HashMap<>();

    public static void load() {
        Warptastix.INSTANCE.saveResource("locale.txt", false);
        File file = new File(Warptastix.INSTANCE.getDataFolder(), "locale.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("#")) {
                    int split = line.indexOf('=');
                    if (split > 0 && line.length() > split + 1) {
                        l10nStore.put(
                                line.substring(0, split),
                                ChatColor.translateAlternateColorCodes('&', line.substring(split + 1)));
                    }
                }
            }
        } catch (Exception e) {
            Warptastix.INSTANCE.getLogger().log(Level.SEVERE, "Failed to load locale!", e);
        }
    }

    public static String prefix(String key, Object... args) {
        return localize("prefix") + localize(key, args);
    }

    public static String localize(String key, Object... args) {
        String format = l10nStore.get(key);
        return format != null ? String.format(format, args) : key;
    }

    public static void send(CommandSender target, String key, Object... args) {
        target.sendMessage(prefix(key, args));
    }

}

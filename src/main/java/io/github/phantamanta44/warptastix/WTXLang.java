package io.github.phantamanta44.warptastix;

import java.util.HashMap;
import java.util.Map;

public class WTXLang { // TODO Configurable locale

    private static final Map<String, String> l10nStore = new HashMap<>();

    static {
        // TODO Put some values
    }

    public static String localize(String key, Object... args) {
        String format = l10nStore.get(key);
        return format != null ? String.format(format, args) : key;
    }

}

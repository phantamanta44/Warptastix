package io.github.phantamanta44.warptastix.data;

import com.google.gson.*;
import io.github.phantamanta44.warptastix.Warptastix;
import org.bukkit.OfflinePlayer;

import java.io.*;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WarpDB {

    public static final JsonParser JSONP = new JsonParser();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final SortedSet<Warp> warps;

    public WarpDB() {
        this.warps = new TreeSet<>();
        load();
    }

    public void load() {
        File file = new File(Warptastix.INSTANCE.getDataFolder(), "warps.json");
        warps.clear();
        if (file.exists()) {
            try (Reader in = new BufferedReader(new FileReader(file))) {
                StreamSupport.stream(JSONP.parse(in).getAsJsonArray().spliterator(), false)
                        .map(JsonElement::getAsJsonObject)
                        .map(Warp::deserialize)
                        .forEach(warps::add);
            } catch (Exception e) {
                Warptastix.INSTANCE.getLogger().log(Level.SEVERE, "Failed to load warps!", e);
            }
        }
    }

    public void save() {
        File file = new File(Warptastix.INSTANCE.getDataFolder(), "warps.json");
        file.getParentFile().mkdirs();
        try (PrintStream out = new PrintStream(new FileOutputStream(file))) {
            JsonArray dto = new JsonArray();
            warps.stream().map(Warp::serialize).forEach(dto::add);
            out.println(GSON.toJson(dto));
        } catch (Exception e) {
            Warptastix.INSTANCE.getLogger().log(Level.SEVERE, "Failed to save warps!", e);
        }
    }

    public Stream<Warp> warps() {
        return warps.stream();
    }

    public Stream<Warp> serverOnly() {
        return warps().filter(Warp.notServer().negate());
    }

    public Stream<Warp> notServer() {
        return warps().filter(Warp.notServer());
    }

    public Stream<Warp> byOwner(UUID owner) {
        return notServer().filter(w -> w.getOwner().equals(owner));
    }

    public Stream<Warp> byOwner(OfflinePlayer owner) {
        return byOwner(owner.getUniqueId());
    }

    public Warp byName(String name) {
        return warps().filter(w -> w.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public void add(Warp warp) {
        warps.add(warp);
    }

    public void remove(Warp warp) {
        warps.remove(warp);
    }

    public void removeIf(Predicate<Warp> condition) {
        warps.removeIf(condition);
    }

}

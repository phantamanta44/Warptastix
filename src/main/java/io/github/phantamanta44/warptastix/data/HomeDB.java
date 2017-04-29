package io.github.phantamanta44.warptastix.data;

import com.google.gson.JsonObject;
import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.util.LazyLoc;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class HomeDB {

    private Map<UUID, LazyLoc> homes;

    public HomeDB() {
        homes = new ConcurrentHashMap<>();
        load();
    }

    public void load() {
        File file = new File(Warptastix.INSTANCE.getDataFolder(), "homes.json");
        homes.clear();
        if (file.exists()) {
            try (Reader in = new BufferedReader(new FileReader(file))) {
                WarpDB.JSONP.parse(in).getAsJsonObject().entrySet().forEach(e -> homes.put(
                                UUID.fromString(e.getKey()), LazyLoc.deserialize(e.getValue().getAsJsonObject())));
            } catch (Exception e) {
                Warptastix.INSTANCE.getLogger().log(Level.SEVERE, "Failed to load homes!", e);
            }
        }
    }

    public void save() {
        File file = new File(Warptastix.INSTANCE.getDataFolder(), "homes.json");
        file.getParentFile().mkdirs();
        try (PrintStream out = new PrintStream(new FileOutputStream(file))) {
            JsonObject dto = new JsonObject();
            homes.forEach((id, loc) -> dto.add(id.toString(), loc.serialize()));
            out.println(WarpDB.GSON.toJson(dto));
        } catch (Exception e) {
            Warptastix.INSTANCE.getLogger().log(Level.SEVERE, "Failed to save homes!", e);
        }
    }

    public LazyLoc forPlayer(UUID id) {
        return homes.get(id);
    }

    public LazyLoc forPlayer(OfflinePlayer pl) {
        return forPlayer(pl.getUniqueId());
    }

    public void set(OfflinePlayer owner, Location location) {
        homes.put(owner.getUniqueId(), new LazyLoc(location));
    }

    public void remove(OfflinePlayer owner) {
        homes.remove(owner.getUniqueId());
    }

}

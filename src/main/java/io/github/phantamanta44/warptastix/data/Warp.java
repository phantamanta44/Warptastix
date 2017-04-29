package io.github.phantamanta44.warptastix.data;

import com.google.gson.JsonObject;
import io.github.phantamanta44.warptastix.util.LazyLoc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.UUID;
import java.util.function.Predicate;

public class Warp implements Comparable<Warp> {

    private String name;
    private LazyLoc loc;
    private long timeCreate;
    private UUID owner;
    private boolean priv;
    private int uses;

    public Warp(String name, LazyLoc loc, UUID owner, boolean priv) {
        this.name = name;
        this.loc = loc;
        this.timeCreate = System.currentTimeMillis();
        this.owner = owner;
        this.priv = priv;
    }

    public Warp(String name, Location loc, UUID owner, boolean priv) {
        this(name, new LazyLoc(loc), owner, priv);
    }

    public Warp(String name, Player pl, boolean priv) {
        this(name, pl.getLocation(), pl.getUniqueId(), priv);
    }

    private Warp() {
        // NO-OP
    }

    public String getName() {
        return name;
    }

    public LazyLoc getLocation() {
        return loc;
    }

    public void setLocation(LazyLoc loc) {
        this.loc = loc;
    }

    public long getCreateTime() {
        return timeCreate;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public boolean isPriv() {
        return priv;
    }

    public void setPriv(boolean priv) {
        this.priv = priv;
    }

    public int getUses() {
        return uses;
    }

    public void incrementUses() {
        this.uses++;
    }

    public boolean isServer() {
        return owner == null;
    }

    public String getOwnerName() {
        return isServer() ? "Server Warp" : Bukkit.getServer().getOfflinePlayer(owner).getName();
    }

    public JsonObject serialize() {
        JsonObject dto = new JsonObject();
        dto.addProperty("name", name);
        dto.add("location", loc.serialize());
        dto.addProperty("timeCreate", timeCreate);
        if (owner != null)
            dto.addProperty("owner", owner.toString());
        dto.addProperty("private", priv);
        dto.addProperty("uses", uses);
        return dto;
    }

    public static Warp deserialize(JsonObject dto) {
        Warp warp = new Warp();
        warp.name = dto.get("name").getAsString();
        warp.loc = LazyLoc.deserialize(dto.get("location").getAsJsonObject());
        warp.timeCreate = dto.get("timeCreate").getAsLong();
        warp.owner = dto.has("owner") ? UUID.fromString(dto.get("owner").getAsString()) : null;
        warp.priv = dto.get("private").getAsBoolean();
        warp.uses = dto.get("uses").getAsInt();
        return warp;
    }

    @Override
    public int compareTo(Warp o) {
        return Long.compare(timeCreate, o.timeCreate);
    }

    public static Comparator<Warp> byPopularity() {
        return Comparator.<Warp>comparingInt(w -> w.uses).reversed();
    }

    public static Predicate<Warp> notServer() {
        return w -> w.owner != null;
    }

}

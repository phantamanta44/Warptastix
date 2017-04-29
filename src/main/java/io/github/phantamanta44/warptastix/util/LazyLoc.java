package io.github.phantamanta44.warptastix.util;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LazyLoc {

    private String worldName;
    private double x, y, z;
    private float yaw, pitch;

    public LazyLoc(String worldName, double x, double y, double z, float yaw, float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public LazyLoc(String worldName, double x, double y, double z) {
        this(worldName, x, y, z, 0F, 0F);
    }

    public LazyLoc(World world, double x, double y, double z, float yaw, float pitch) {
        this(world.getName(), x, y, z, yaw, pitch);
    }

    public LazyLoc(World world, double x, double y, double z) {
        this(world.getName(), x, y, z);
    }
    
    public LazyLoc(Location loc) {
        this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    private LazyLoc() {
        // NO-OP
    }

    public String getWorldName() {
        return worldName;
    }

    public World getWorld() {
        return Bukkit.getServer().getWorld(worldName);
    }

    public boolean isWorldLoaded() {
        return getWorld() != null;
    }

    public Location getLocation() {
        World world = getWorld();
        return world == null ? null : new Location(world, x, y, z, yaw, pitch);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public JsonObject serialize() {
        JsonObject dto = new JsonObject();
        dto.addProperty("worldName", worldName);
        dto.addProperty("x", x);
        dto.addProperty("y", y);
        dto.addProperty("z", z);
        dto.addProperty("yaw", yaw);
        dto.addProperty("pitch", pitch);
        return dto;
    }

    public static LazyLoc deserialize(JsonObject dto) {
        LazyLoc loc = new LazyLoc();
        loc.worldName = dto.get("worldName").getAsString();
        loc.x = dto.get("x").getAsDouble();
        loc.y = dto.get("y").getAsDouble();
        loc.z = dto.get("z").getAsDouble();
        loc.yaw = dto.get("yaw").getAsFloat();
        loc.pitch = dto.get("pitch").getAsFloat();
        return loc;
    }

    @Override
    public String toString() {
        return String.format("%s (%.0f, %.0f, %.0f)", worldName, x, y, z);
    }

}

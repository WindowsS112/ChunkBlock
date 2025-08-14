package com.jasper.chunkBlock.chunk;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.chunk.settings.SettingType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.*;

public class ClaimedChunk {

    private final String teamId;
    private final String chunkId;
    private final String world;
    private WorldBorder worldBorder;
    private final double radius;
    private Location home;
    private String owner;
    private final int x;
    private final int z;
    private int level;
    private double xp;
    private final Map<String, Object> upgrades = new HashMap<>();
    private final Map<SettingType, Boolean> settings = new HashMap<>();

    public ClaimedChunk(String chunkid, String teamid, String ownerUuid, int level, String world, int centerX, int centerZ, int borderRadius) {
        this.world = world;
        this.radius = borderRadius;
        this.owner = ownerUuid;
        this.x = centerX;
        this.z = centerZ;
        this.teamId = teamid;
        this.chunkId = chunkid;
        this.xp = xp;
        this.level = level;
        for (SettingType type : SettingType.values()) {
            settings.put(type, false);
        }
    }

    public Location getCenter(ClaimedChunk claimedChunk) {
        World w = Bukkit.getWorld(world);
        if (w == null) {
            throw new IllegalStateException("World not found! : " + world);
        }

        // Y-waarde kan je zo laten of ook uit db halen als je dat wilt
        double y = 64;

        return new Location(w, x, y, z);
    }


    public WorldBorder createBorder(Player player) {
        WorldBorder border = Bukkit.createWorldBorder();
        Location location = player.getLocation();

        border.setCenter(location);
        border.setSize(level * radius * 2); // 32 blokken per chunk, *2 voor diameter

        player.setWorldBorder(border);
        return border;
    }

    public WorldBorder removeBorder() {
        return worldBorder = null;
    }


    public String getWorld() { return world; }
    public int getX() { return x; }
    public int getZ() { return z; }
    public String getTeamId() { return teamId; }
    public int getLevel() { return level; }
    public double getXp() { return xp; }
    public boolean isSettingEnabled(SettingType type) { return settings.getOrDefault(type, false); }
    public Map<String, Object> getUpgrades() { return upgrades; }
    public Map<SettingType, Boolean> getSettings() { return settings; }
    public Location getHome() { return home; }
    public String getChunkId() {
        return chunkId;
    }
    public void toggleSetting(SettingType type) {
        settings.put(type, !isSettingEnabled(type));
    }
    public void setSetting(SettingType type, boolean value) {
        settings.put(type, value);
    }
    public void setHome(Location location) {
        home = location;
    }
    public void setXp(double xp) { this.xp = xp; }
    public void addXp(double amount) { this.xp += amount; }
    public void setUpgrade(String key, Object value) { this.upgrades.put(key, value); }
    public Object getUpgrade(String key) { return this.upgrades.get(key); }
    public boolean hasUpgrade(String key) { return this.upgrades.containsKey(key); }
    public int getClaimRadius() {
        return (int) radius;
    }
    public UUID getOwner() {
        return UUID.fromString(owner);
    }

    public WorldBorder getBorder() {
        return worldBorder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClaimedChunk)) return false;
        ClaimedChunk other = (ClaimedChunk) o;
        return x == other.x && z == other.z && world.equals(other.world);
    }

    public void setLevel(int newLevel) {
        this.level = newLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }
}
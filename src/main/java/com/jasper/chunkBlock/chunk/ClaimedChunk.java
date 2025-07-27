package com.jasper.chunkBlock.chunk;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.chunk.settings.SettingType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.*;

public class ClaimedChunk {

    private final String teamId;
    private final Team team;
    private final String world;
    private final double radius;
    private Location home;
    private final int x;
    private final int z;
    private int level = 1;
    private double xp = 0.0;
    private final Map<String, Object> upgrades = new HashMap<>();
    private final Map<SettingType, Boolean> settings = new HashMap<>();

    public ClaimedChunk(Team team, String world, double radius, int x, int z, String teamId) {
        this.team = team;
        this.world = world;
        this.radius = radius;
        this.x = x;
        this.z = z;
        this.teamId = teamId;
        for (SettingType type : SettingType.values()) {
            settings.put(type, false);
        }
    }

    public Location getCenter() {
        World w = Bukkit.getWorld(world);
        if (w == null) {
            throw new IllegalStateException("World not found! : " + world);
        }
        return new Location(w, x + 0.5, 64, z + 0.5);
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

    public void applyToTeam() {
        // Implementeer createPlayerBorder() of verwijder deze regel
        // WorldBorder wb = createPlayerBorder();
        Set<UUID> members = team.getMembersOfTeam();
        for (UUID uuid : members) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                // player.setWorldBorder(wb);
            }
        }
    }

    public void toggleSetting(SettingType type) {
        settings.put(type, !isSettingEnabled(type));
        ChunkBlock.getInstance().getBorderStorage().saveBorder(team);
    }

    public void setSetting(SettingType type, boolean value) {
        settings.put(type, value);
    }

    public void setHome(Location location) {
        home = location;
        // ChunkBlock.getInstance().getBorderStorage().saveBorder(team);
    }

    public void setLevel(int level) { this.level = level; }
    public void setXp(double xp) { this.xp = xp; }
    public void addXp(double amount) { this.xp += amount; }
    public void setUpgrade(String key, Object value) { this.upgrades.put(key, value); }
    public Object getUpgrade(String key) { return this.upgrades.get(key); }
    public boolean hasUpgrade(String key) { return this.upgrades.containsKey(key); }

    public int getClaimRadius() {
        return this.level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClaimedChunk)) return false;
        ClaimedChunk other = (ClaimedChunk) o;
        return x == other.x && z == other.z && world.equals(other.world);
    }

    @Override
    public String toString() {
        return "ClaimedChunk{" +
                "world='" + world + '\'' +
                ", x=" + x +
                ", z=" + z +
                ", radius=" + radius +
                ", team=" + team +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }
}
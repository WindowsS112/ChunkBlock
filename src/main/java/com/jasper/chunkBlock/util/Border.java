package com.jasper.chunkBlock.util;

import com.jasper.chunkBlock.ChunkBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.*;

import com.jasper.chunkBlock.gui.chunk.settings.SettingType;

public class Border {

    private final int x;             // chunk-coördinaat X
    private final int z;             // chunk-coördinaat Z
    private final String worldName;  // wereldnaam
    private final Team owner;        // eigenaar van deze border
    private final double radius;     // radius in blokken
    private Location defaultHome;

    private final Map<SettingType, Boolean> settings = new HashMap<>();

    private boolean allowPvP = false;
    private boolean allowBuild = true;

    public Border(int x, int z, String worldName, Team owner, double radius,Location defaultHome) {
        this.x = x;
        this.z = z;
        this.worldName = worldName;
        this.owner = owner;
        this.radius = radius;
        this.defaultHome = defaultHome;
        for (SettingType type : SettingType.values()) {
            settings.put(type, false);
        }
    }

    public void setHome(Location location) {
        defaultHome = location;
    }

    //–– Getters ––//
    public Location getDefaultHome() {
        return defaultHome;
    }
    public int getX() {
        return x;
    }
    public int getZ() {
        return z;
    }
    public String getWorldName() {
        return worldName;
    }
    public Team getOwner() {
        return owner;
    }
    public double getRadius() {
        return radius;
    }

    public Map<SettingType, Boolean> getSettings() {
        return settings;
    }

    public boolean isSettingEnabled(SettingType type) {
        return settings.getOrDefault(type, false);
    }

    public void toggleSetting(SettingType type) {
        settings.put(type, !isSettingEnabled(type));
        ChunkBlock.getInstance().getBorderStorage().saveBorder(owner);
    }

    public void setSetting(SettingType type, boolean value) {
        settings.put(type, value);
    }





    //–– Setters ––//
    public void setAllowPvP(boolean allowPvP) {
        this.allowPvP = allowPvP;
    }

    public void setAllowBuild(boolean allowBuild) {
        this.allowBuild = allowBuild;
    }

    //–– Utility ––//

    /**
     * Unieke key voor opslag/lookup in je BorderStorage: "world:x,z"
     */
    public String getKey() {
        return worldName + ":" + x + "," + z;
    }

    /**
     * Maak een nieuwe WorldBorder die je per-player kunt toewijzen.
     * @return een volledig geconfigureerde, lege WorldBorder
     */
    public WorldBorder createPlayerBorder() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IllegalStateException("World not found: " + worldName);
        }

        WorldBorder wb = Bukkit.createWorldBorder();
        wb.setCenter(x + 0.5, z + 0.5);
        wb.setSize(radius);
        wb.setWarningDistance(5);
        wb.setWarningTime(10);
        return wb;
    }

    /**
     * Pas deze border toe op alle leden van het team.
     */
    public void applyToTeam() {
        WorldBorder wb = createPlayerBorder();
        Set<UUID> members = owner.getMembersOfTeam();
        for (UUID uuid : members) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.setWorldBorder(wb);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Border)) return false;
        Border border = (Border) o;
        return x == border.x &&
                z == border.z &&
                Double.compare(border.radius, radius) == 0 &&
                Objects.equals(worldName, border.worldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldName, x, z, radius);
    }

    @Override
    public String toString() {
        return "Border{" +
                "world='" + worldName + '\'' +
                ", x=" + x +
                ", z=" + z +
                ", radius=" + radius +
                ", owner=" + owner.getOwner().toString() +
                '}';
    }
}

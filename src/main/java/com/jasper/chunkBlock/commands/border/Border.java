package com.jasper.chunkBlock.commands.border;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.chunk.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.*;

import com.jasper.chunkBlock.commands.chunk.settings.SettingType;

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

//    public Location getCenter() {
//        World world = Bukkit.getWorld(worldName);
//        if (world == null) {
//            throw new IllegalStateException("Wereld niet gevonden: " + worldName);
//        }
//        return new Location(world, x + 0.5, 64, z + 0.5); // 0.5 = midden van blok
//    }


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
}

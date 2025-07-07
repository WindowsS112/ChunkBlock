package com.jasper.chunkBlock.commands.border;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class Border {

    private Location center;
    private int radius;

    public Border(Location center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public void expand(int amount) {
        this.radius += amount;
    }

    public void shrink(int amount) {
        this.radius = Math.max(0, this.radius - amount);
    }

    public WorldBorder toWorldBorder() {
        WorldBorder worldBorder = Bukkit.createWorldBorder();
        worldBorder.setCenter(center);
        worldBorder.setSize(radius);
        return worldBorder;
    }

    public Location getCenter() { return center; }
    public int getRadius() { return radius; }
    public World getWorld() {
        return center.getWorld();
    }

    public int getMinX() {
        return center.getBlockX() - radius;
    }
    public int getMaxX() {
        return center.getBlockX() + radius;
    }
    public int getMinZ() {
        return center.getBlockZ() - radius;
    }
    public int getMaxZ() {
        return center.getBlockZ() + radius;
    }
}


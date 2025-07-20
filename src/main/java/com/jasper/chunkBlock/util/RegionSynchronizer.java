package com.jasper.chunkBlock.util;

import com.jasper.chunkBlock.commands.border.Border;
import com.jasper.chunkBlock.commands.team.Team;
import com.jasper.chunkBlock.gui.chunk.settings.SettingType;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class RegionSynchronizer {

    public static void syncRegionFromBorder(Team team, Border border) {
        World world = border.getCenter().getWorld();

        RegionManager manager = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(BukkitAdapter.adapt(world));

        if (manager == null) return;

        String regionId = "team_" + team.getTeamName().toLowerCase();

        // Delete oude regio als hij bestaat
        if (manager.hasRegion(regionId)) {
            manager.removeRegion(regionId);
        }

        // Bereken min/max hoek van de border
        Location center = border.getCenter();
        double radius = border.getRadius() / 2.0;

        int minX = (int) Math.floor(center.getX() - radius);
        int maxX = (int) Math.ceil(center.getX() + radius);
        int minZ = (int) Math.floor(center.getZ() - radius);
        int maxZ = (int) Math.ceil(center.getZ() + radius);

        // Debug output
        Bukkit.getLogger().info("Regio voor team " + team.getTeamName() + ":");
        Bukkit.getLogger().info("Center: " + center.getX() + ", " + center.getZ());
        Bukkit.getLogger().info("Min: " + minX + ", " + minZ);
        Bukkit.getLogger().info("Max: " + maxX + ", " + maxZ);
        Bukkit.getLogger().info("Breedte: " + (maxX - minX) + ", Hoogte: " + (maxZ - minZ));

        BlockVector3 min = BlockVector3.at(minX, 0, minZ);
        BlockVector3 max = BlockVector3.at(maxX, 255, maxZ);

        ProtectedCuboidRegion region = new ProtectedCuboidRegion(regionId, min, max);

        // Teamleden toevoegen
        team.getMembersOfTeam().forEach(uuid -> region.getMembers().addPlayer(uuid.toString()));

        // Flags zetten
        for (SettingType setting : SettingType.values()) {
            StateFlag flag = setting.getFlag();
            if (flag == null) continue;

            boolean enabled = border.isSettingEnabled(setting);
            region.setFlag(flag, enabled ? StateFlag.State.ALLOW : StateFlag.State.DENY);
        }

        manager.addRegion(region);
        try {
            manager.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void deleteRegion(Team team, Border border) {
        World world = border.getCenter().getWorld();
        RegionManager manager = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(BukkitAdapter.adapt(world));

        if (manager == null) return;

        String regionId = "team_" + team.getTeamName().toLowerCase();

        if (manager.hasRegion(regionId)) {
            manager.removeRegion(regionId);
        }
    }

}

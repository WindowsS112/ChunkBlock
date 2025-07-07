package com.jasper.chunkBlock.util;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.border.Border;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class BorderStorage {

    private final File borderData;
    private final ChunkBlock plugin;
    private final YamlConfiguration borderstoragefile;
    private TeamStorage teamStorage;


    public BorderStorage(File borderData, ChunkBlock plugin, YamlConfiguration borderstoragefile, TeamStorage teamStorage) {
        this.borderData = borderData;
        this.plugin = plugin;
        this.borderstoragefile = borderstoragefile;
        this.teamStorage = teamStorage;
    }

    public void saveChunk(Team team, Border border) {
        borderstoragefile.set("Teams." + team.getTeamName() + ".owner", team.getOwner().toString());
        borderstoragefile.set("Teams." + team.getTeamName() + ".world", border.getWorld().getName());
        borderstoragefile.set("Teams." + team.getTeamName() + ".radius", border.getRadius());
        borderstoragefile.set("Teams." + team.getTeamName() + ".center.x", border.getCenter().getX());
        borderstoragefile.set("Teams." + team.getTeamName() + ".center.y", border.getCenter().getY());
        borderstoragefile.set("Teams." + team.getTeamName() + ".center.z", border.getCenter().getZ());

        try {
            borderstoragefile.save(borderData);
        } catch (IOException e) {
            Bukkit.getLogger().info("Couldn't save borderData file.");
            return;
        }
    }

    public boolean loadChunk(Team team) {
        String path = "Teams." + team.getTeamName();

        if (!borderstoragefile.contains(path)) {
            Bukkit.getLogger().info("Failed loadchunk command: team not found");
            return false;
        }

        try {
            String worldName = borderstoragefile.getString(path + ".world");
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                Bukkit.getLogger().warning("World not found: " + worldName);
                return false;
            }

            int radius = borderstoragefile.getInt(path + ".radius");
            int locx = borderstoragefile.getInt(path + ".center.x");
            int locy = borderstoragefile.getInt(path + ".center.y");
            int locz = borderstoragefile.getInt(path + ".center.z");

            Location center = new Location(world, locx, locy, locz);
            Border teamBorder = new Border(center, radius);

            for (UUID uuid : team.getMembersOfTeam()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    player.setWorldBorder(teamBorder.toWorldBorder());
                    player.sendMessage(ChatColor.GREEN + "Chunk loaded");
                }
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeBorder(Border border, Team team) {
        borderstoragefile.set("Teams." + team.getTeamName(), null);
        saveConfig();

        for (UUID uuid : team.getMembersOfTeam()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.setWorldBorder(null);
            }
        }
    }

    public void saveConfig() {
        try {
            borderstoragefile.save(borderData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}

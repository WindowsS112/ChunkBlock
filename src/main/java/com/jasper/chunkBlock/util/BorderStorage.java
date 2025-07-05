package com.jasper.chunkBlock.util;

import com.jasper.chunkBlock.ChunkBlock;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class BorderStorage {

    private final File borderData;
    private final ChunkBlock plugin;
    private final YamlConfiguration modifyFile;


    public BorderStorage(File borderData, ChunkBlock plugin, YamlConfiguration modifyFile) {
        this.borderData = borderData;
        this.plugin = plugin;
        this.modifyFile = modifyFile;
    }

    public void saveChunk(Player player, double cSize) {
        modifyFile.set("Players." + player.getName() + ".borderSize", cSize);
        modifyFile.set("Players." + player.getName() + ".world", player.getWorld().getName());
        modifyFile.set("Players." + player.getName() + ".x", player.getLocation().getBlockX());
        modifyFile.set("Players." + player.getName() + ".y", player.getLocation().getBlockY());
        modifyFile.set("Players." + player.getName() + ".z", player.getLocation().getBlockZ());

        try {
            modifyFile.save(borderData);
        } catch (IOException e) {
            Bukkit.getLogger().info("Couldn't save borderData file.");
            return;
        }
    }


    public boolean loadChunk(Player player) {
        if (modifyFile.contains("Players." + player.getName())) {
            String path = "Players." + player.getName();
            String worldName = modifyFile.getString("Players." + player.getName() + ".world");
            World world = Bukkit.getWorld(worldName);
            double cSize = modifyFile.getDouble(path + ".borderSize");
            int locx = modifyFile.getInt(path + ".x");
            int locy = modifyFile.getInt(path + ".y");
            int locz = modifyFile.getInt(path + ".z");

            Location location = new Location(world, locx, locy, locz);

            try {
                WorldBorder border = Bukkit.createWorldBorder();
                border.setCenter(location);
                border.setSize(cSize);
                player.setWorldBorder(border);

                player.sendMessage(ChatColor.GREEN + "Chunk loaded");
                return true;
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Failed to load chunk");
                e.printStackTrace();
                return false;
            }

        } else {
            player.sendMessage(ChatColor.RED + "You don't have a chunk, create one with /c create");
            return false;
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}

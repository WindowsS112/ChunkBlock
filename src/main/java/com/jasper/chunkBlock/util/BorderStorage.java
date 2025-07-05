package com.jasper.chunkBlock.util;

import com.jasper.chunkBlock.ChunkBlock;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class BorderStorage {

    private final File borderData;
    private final ChunkBlock plugin;
    private final YamlConfiguration borderstoragefile;


    public BorderStorage(File borderData, ChunkBlock plugin, YamlConfiguration borderstoragefile) {
        this.borderData = borderData;
        this.plugin = plugin;
        this.borderstoragefile = borderstoragefile;
    }

    public void saveChunk(Player player, double cSize) {
        borderstoragefile.set("Players." + player.getName() + ".borderSize", cSize);
        borderstoragefile.set("Players." + player.getName() + ".world", player.getWorld().getName());
        borderstoragefile.set("Players." + player.getName() + ".x", player.getLocation().getBlockX());
        borderstoragefile.set("Players." + player.getName() + ".y", player.getLocation().getBlockY());
        borderstoragefile.set("Players." + player.getName() + ".z", player.getLocation().getBlockZ());

        try {
            borderstoragefile.save(borderData);
        } catch (IOException e) {
            Bukkit.getLogger().info("Couldn't save borderData file.");
            return;
        }
    }


    public boolean loadChunk(Player player) {
        if (borderstoragefile.contains("Players." + player.getName())) {
            String path = "Players." + player.getName();
            String worldName = borderstoragefile.getString("Players." + player.getName() + ".world");
            World world = Bukkit.getWorld(worldName);
            double cSize = borderstoragefile.getDouble(path + ".borderSize");
            int locx = borderstoragefile.getInt(path + ".x");
            int locy = borderstoragefile.getInt(path + ".y");
            int locz = borderstoragefile.getInt(path + ".z");

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

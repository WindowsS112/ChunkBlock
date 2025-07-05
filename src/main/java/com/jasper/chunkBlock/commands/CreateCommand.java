package com.jasper.chunkBlock.commands;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.util.BorderStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;


public class CreateCommand implements CommandExecutor {

    private double cSize;
    private final File borderData;
    private final ChunkBlock plugin;
    private final BorderStorage storage;


    public CreateCommand(double cSize, File borderData1, ChunkBlock plugin) {
        this.cSize = cSize;
        this.borderData = borderData1;
        this.plugin = plugin;
        this.storage = plugin.getBorderStorage();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        BorderStorage storage = plugin.getBorderStorage();

        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by a player!");
            return true;
        }

        if (args.length == 0) {
            createChunk(player, cSize, player.getLocation());
            return true;
        }

        player.sendMessage(ChatColor.RED + "Unknown command!");

        return true;
    }


    public void createChunk(Player player, double cSize, Location location) {
        try {
            WorldBorder border = Bukkit.createWorldBorder();
            border.setCenter(location);
            border.setSize(cSize);

            player.setWorldBorder(border);

            storage.saveChunk(player, cSize);

            player.sendMessage(ChatColor.GREEN + "Successfully created a chunk!");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static void resetPlayerBorder(Player player) {
        // Reset naar de wereld border
        player.setWorldBorder(null);
    }

    public double getcSize() { return cSize; }

}

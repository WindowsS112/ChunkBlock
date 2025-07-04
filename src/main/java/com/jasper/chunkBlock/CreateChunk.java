package com.jasper.chunkBlock;

import com.github.yannicklamprecht.worldborder.api.WorldBorderApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;


public class CreateChunk implements CommandExecutor {

    private final WorldBorderApi worldBorderApi;
    private double cSize;
    private final File borderData;
    private final YamlConfiguration modifyFile;

    public CreateChunk(WorldBorderApi worldBorderApi, double cSize, File borderData, YamlConfiguration modifyFile) {
        this.worldBorderApi = worldBorderApi;
        this.cSize = cSize;
        this.borderData = borderData;
        this.modifyFile = modifyFile;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

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
            worldBorderApi.setBorder(player, cSize, location);
            saveChunk(player);

            player.sendMessage(ChatColor.GREEN + "Sucessfully created a chunk!");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void saveChunk(Player player) {
        modifyFile.set("Players." + player.getName() + ".borderSize", cSize);
        modifyFile.set("Players." + player.getName() + ".x", player.getLocation().getBlockX());
        modifyFile.set("Players." + player.getName() + ".y", player.getLocation().getBlockY());
        modifyFile.set("Players." + player.getName() + ".z", player.getLocation().getBlockZ());


        try {
            modifyFile.save(borderData);
        } catch (IOException e) {
            Bukkit.getLogger().info("Couldn't load borderData file.");
            return;
        }
    }



    public double getcSize() { return cSize; }

}

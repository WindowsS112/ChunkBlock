package com.jasper.chunkBlock;

import com.github.yannicklamprecht.worldborder.api.WorldBorderApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class CreateChunk implements CommandExecutor {

    private final WorldBorderApi worldBorderApi;
    private double cSize;

    public CreateChunk(WorldBorderApi worldBorderApi, double cSize) {
        this.worldBorderApi = worldBorderApi;
        this.cSize = cSize;
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
            player.sendMessage(ChatColor.GREEN + "Sucessfully created a chunk!");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }



    public double getcSize() { return cSize; }

}

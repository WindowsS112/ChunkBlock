package com.jasper.chunkBlock.commands;

import com.jasper.chunkBlock.util.BorderStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BypassCommand implements CommandExecutor {

    private boolean hasBypass = false;
    private BorderStorage borderStorage;

    public BypassCommand(BorderStorage borderStorage) {
        this.borderStorage = borderStorage;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by a player!");
            return true;
        }

        toggleBypass(player);
        return true;
    }

    public void toggleBypass(Player player) {
        hasBypass = !hasBypass;

        if (hasBypass) {
            player.setWorldBorder(null);
            player.sendMessage("§aBypass activated.");
            System.out.println("Bypass activated.");
        } else {
            borderStorage.loadChunk(player);
            player.sendMessage("§cBypass deactivated.");
            System.out.println("Bypass deactivated.");
        }
    }
}


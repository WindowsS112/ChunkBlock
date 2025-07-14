package com.jasper.chunkBlock.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class MessageUtils {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "ChunkBlock" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

    public static void send(Player player, String message) {
        player.sendMessage(PREFIX + ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendSuccess(Player player, String message) {
        player.sendMessage(PREFIX + ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendError(Player player, String message) {
        player.sendMessage(PREFIX + ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendInfo(Player player, String message) {
        player.sendMessage(PREFIX + ChatColor.translateAlternateColorCodes('&', message));
    }

}

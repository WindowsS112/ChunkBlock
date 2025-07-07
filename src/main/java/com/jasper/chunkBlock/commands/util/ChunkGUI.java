package com.jasper.chunkBlock.commands.util;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

public class ChunkGUI extends SubCommand {

    private TeamStorage teamStorage;

    public ChunkGUI(String name, String description, String syntax, TeamStorage teamStorage) {
        super(name, description, syntax);
        this.teamStorage = teamStorage;
    }

    @Override
    public void perform(Player player, String[] args) {
        UUID playerUUID = player.getUniqueId();
        Team team = teamStorage.getTeamFromPlayer(playerUUID);

        if (team == null) {
            player.sendMessage(ChatColor.RED + "You are not in a team");
            return;
        }
        showChunkGUI(player, team);
    }

    public void showChunkGUI(Player player, Team team) {
        Inventory inv = Bukkit.createInventory(player, 27, ChatColor.DARK_GRAY.toString() + "          Chunk Main Menu " );

        // HOME
        ItemStack home = new ItemStack(Material.OAK_DOOR);
        ItemMeta homeMeta = home.getItemMeta();
        homeMeta.setDisplayName(ChatColor.BLUE + "Chunk Home");
        homeMeta.setLore(Arrays.asList(ChatColor.GRAY + "Teleports to Chunk home"));
        home.setItemMeta(homeMeta);
        inv.setItem(10,home);

        // SETTINGS
        ItemStack settings = new ItemStack(Material.BOOK);
        ItemMeta settingsMeta = home.getItemMeta();
        settingsMeta.setDisplayName(ChatColor.BLUE + "Chunk Home");
        settingsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Teleports to Chunk home"));
        home.setItemMeta(settingsMeta);
        inv.setItem(11,settings);

        player.openInventory(inv);

    }


    @Override
    public String getName() {
        return "chunk";
    }
    @Override
    public String getDescription() {
        return "Shows main chunk GUI";
    }
    @Override
    public String getSyntax() {
        return "/c";
    }
}

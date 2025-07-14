package com.jasper.chunkBlock.gui.chunk;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.border.Border;
import com.jasper.chunkBlock.commands.team.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChunkMainGUI {

    private Player player;
    private final Team team;
    private final Border border;

    public ChunkMainGUI(Player player, Team team) {
        this.player = player;
        this.team = team;

        this.border = ChunkBlock.getInstance().getBorderStorage().getBorder(team);
        if (this.border == null) {
            throw new IllegalStateException("Border niet d voor team: " + team.getTeamName());
        }
    }

    public void open() {
        ChestGui gui = new ChestGui(4, "Chunk - Menu");

        gui.setOnGlobalClick(event -> event.setCancelled(true));

        OutlinePane background = new OutlinePane(0, 0, 9, 4, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        gui.addPane(background);

        OutlinePane navigationPane = new OutlinePane(1, 1, 3, 2);

        ItemStack shop = new ItemStack(Material.OAK_DOOR);
        ItemMeta shopMeta = shop.getItemMeta();
        shopMeta.setDisplayName("Chunk - Home");
        shop.setItemMeta(shopMeta);

        navigationPane.addItem(new GuiItem(shop, event -> {
            player.teleport(border.getDefaultHome());
        }));

        ItemStack beacon = new ItemStack(Material.BOOK);
        ItemMeta beaconMeta = beacon.getItemMeta();
        beaconMeta.setDisplayName("Chunk - Settings");
        beacon.setItemMeta(beaconMeta);

        navigationPane.addItem(new GuiItem(beacon, event -> {
            ChunkSettingsGUI ch = new ChunkSettingsGUI(player, team);
            ch.open();
        }));

        ItemStack bed = new ItemStack(Material.RED_BED);
        ItemMeta bedMeta = bed.getItemMeta();
        bedMeta.setDisplayName("Chunk - Home");
        bed.setItemMeta(bedMeta);

        navigationPane.addItem(new GuiItem(bed, event -> {
            //navigate to home
        }));

        ItemStack players = new ItemStack(Material.ACACIA_LEAVES);
        ItemMeta  playerMeta = players.getItemMeta();
        playerMeta.setDisplayName("Chunk - Players");
        players.setItemMeta(playerMeta);

        navigationPane.addItem(new GuiItem(players, event -> {
            ChunkPlayersGUI chunkPlayersGUI = new ChunkPlayersGUI(player,team);
            chunkPlayersGUI.open();
        }));

        gui.addPane(navigationPane);
        gui.show(player);
    }
}

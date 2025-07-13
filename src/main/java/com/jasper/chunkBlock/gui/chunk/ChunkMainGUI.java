package com.jasper.chunkBlock.gui.chunk;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.util.Border;
import com.jasper.chunkBlock.util.Team;
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
        ChestGui gui = new ChestGui(3, "Chunk d");

        gui.setOnGlobalClick(event -> event.setCancelled(true));

        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        gui.addPane(background);

        OutlinePane navigationPane = new OutlinePane(3, 1, 3, 1);

        ItemStack shop = new ItemStack(Material.CHEST);
        ItemMeta shopMeta = shop.getItemMeta();
        shopMeta.setDisplayName("Shop");
        shop.setItemMeta(shopMeta);

        navigationPane.addItem(new GuiItem(shop, event -> {
            //navigate to the shop
        }));

        ItemStack beacon = new ItemStack(Material.BEACON);
        ItemMeta beaconMeta = beacon.getItemMeta();
        beaconMeta.setDisplayName("Spawn");
        beacon.setItemMeta(beaconMeta);

        navigationPane.addItem(new GuiItem(beacon, event -> {
            //navigate to spawn
        }));

        ItemStack bed = new ItemStack(Material.RED_BED);
        ItemMeta bedMeta = bed.getItemMeta();
        bedMeta.setDisplayName("Home");
        bed.setItemMeta(bedMeta);

        navigationPane.addItem(new GuiItem(bed, event -> {
            //navigate to home
        }));

        gui.addPane(navigationPane);
        gui.show(player);
    }
}

package com.jasper.chunkBlock.gui.base;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ConfirmationGUI {

    private final Player player;
    private final String title;

    public ConfirmationGUI(Player player, String title) {
        this.player = player;
        this.title = title;
    }

    public void open() {
        ChestGui gui = new ChestGui(3, title);
        StaticPane pane = new StaticPane(0, 1, 9, 1);

        // YES button (green)
        ItemStack yesItem = new ItemStack(Material.LIME_CONCRETE);
        ItemMeta yesMeta = yesItem.getItemMeta();
        yesMeta.setDisplayName("§aYes");
        yesItem.setItemMeta(yesMeta);

        pane.addItem(new GuiItem(yesItem, event -> {
            event.setCancelled(true);
            player.closeInventory();
            onConfirm();
        }), 2, 0);

        // NO button (red)
        ItemStack noItem = new ItemStack(Material.RED_CONCRETE);
        ItemMeta noMeta = noItem.getItemMeta();
        noMeta.setDisplayName("§cNo");
        noItem.setItemMeta(noMeta);

        pane.addItem(new GuiItem(noItem, event -> {
            event.setCancelled(true);
            player.closeInventory();
            onDeny();
        }), 6, 0);

        gui.addPane(pane);
        gui.show(player);
    }

    protected abstract void onConfirm();
    protected abstract void onDeny();
}

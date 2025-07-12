package com.jasper.chunkBlock.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public abstract class BaseGui {

    protected final Player player;
    protected final ChestGui gui;

    public BaseGui(Player player, String title, int rows) {
        this.player = player;
        this.gui = new ChestGui(rows, title);
        this.gui.setOnGlobalClick(event -> event.setCancelled(true)); // voorkomt slepen
    }

    public abstract void build();

    public void open() {
        this.build();
        GuiItem filler = new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)); // of via utility
        GuiItem[] pane = new GuiItem[gui.getRows() * 9];
        Arrays.fill(pane, filler);
//        gui.getFiller().fill(pane); // optioneel: alleen randjes vullen
        gui.show(player);
    }
}


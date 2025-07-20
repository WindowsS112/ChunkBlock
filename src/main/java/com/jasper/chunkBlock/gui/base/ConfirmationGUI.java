package com.jasper.chunkBlock.gui.base;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.HopperGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.border.Border;
import com.jasper.chunkBlock.commands.team.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ConfirmationGUI {

    private final Player player;
    private final Team team;

    public ConfirmationGUI(Player player, Team team) {
        this.player = player;
        this.team = team;
    }

    public void open() {
        HopperGui hopperGui = new HopperGui("My hopper");

        StaticPane pane = new StaticPane(0, 0, 9, 0);
        pane.addItem(new GuiItem(new ItemStack(Material.RED_WOOL)), 0, 0);
        pane.addItem(new GuiItem(new ItemStack(Material.GREEN_WOOL)), 4, 0);

        hopperGui.show(player);
    }
}

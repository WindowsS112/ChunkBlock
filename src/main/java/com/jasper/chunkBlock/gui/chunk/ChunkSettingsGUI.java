package com.jasper.chunkBlock.gui.chunk;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.jasper.chunkBlock.util.Border;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.gui.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.inventory.ItemStack;

public class ChunkSettingsGUI {

    private final Player player;
    private final Team team;
    private final Border border;

    public ChunkSettingsGUI(Player player, Team team) {
        this.player = player;
        this.team = team;

        this.border = ChunkBlock.getInstance().getBorderStorage().getBorder(team);
        if (this.border == null) {
            throw new IllegalStateException("Border niet gevonden voor team: " + team.getTeamName());
        }
    }

    public void open() {
        ChestGui gui = new ChestGui(3, "Chunk Settings");
        StaticPane pane = new StaticPane(0, 0, 9, 3);

        if (border == null) {
            ItemStack noBorderItem = new ItemBuilder(Material.BARRIER)
                    .setName("§cGeen border gevonden")
                    .setLore("§7Claim eerst een chunk voor dit team.")
                    .build();
            pane.addItem(new GuiItem(noBorderItem), 4, 1);
        } else {
            boolean pvp = border.isAllowPvP();

            ItemStack pvpItem = new ItemBuilder(Material.BOOK)
                    .setName("§bPvP")
                    .setLore("§7PvP is: " + (pvp ? "§aEnabled" : "§cDisabled"))
                    .build();

            pane.addItem(new GuiItem(pvpItem, event -> {
                border.setAllowPvP(!pvp);
                new ChunkSettingsGUI(player, team).open(); // Refresh GUI
            }), 3, 1);
        }

        gui.addPane(pane);
        gui.show(player);
    }
}

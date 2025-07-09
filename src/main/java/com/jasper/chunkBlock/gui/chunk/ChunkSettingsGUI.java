package com.jasper.chunkBlock.gui.chunk;

import com.jasper.chunkBlock.util.Team;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChunkSettingsGUI {

    public void open(Player player, Team team) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("Chunk Settings"))
                .rows(4)
                .disableAllInteractions()
                .pageSize(3)
                .create();

        // Initialize PvP item
        updatePvpItem(gui, team);

        // Previous & Next buttons
        gui.setItem(4, 3, ItemBuilder.from(Material.PAPER).setName("Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(4, 7, ItemBuilder.from(Material.PAPER).setName("Next").asGuiItem(event -> gui.next()));

        // Filler items
        GuiItem filler = ItemBuilder.from(Material.GREEN_STAINED_GLASS_PANE).asGuiItem();
        for (int slot : new int[]{27, 28, 30, 31, 32, 34, 35}) {
            gui.setItem(slot, filler);
        }

        gui.open(player);
    }

    private void updatePvpItem(PaginatedGui gui, Team team) {
        String name = "PVP " + (team.getBorder().isPvpAllowed() ? "§a[TRUE]" : "§c[FALSE]");
        String lore = "Enables pvp in region";

        GuiItem pvpItem = ItemBuilder.from(Material.BOOK)
                .setName(name)
                .setLore(lore)
                .asGuiItem(event -> {
                    team.getBorder().setPvpAllowed(!team.getBorder().isPvpAllowed());
                    updatePvpItem(gui, team); // Recursively update
                });

        gui.setItem(1, pvpItem);
    }


}


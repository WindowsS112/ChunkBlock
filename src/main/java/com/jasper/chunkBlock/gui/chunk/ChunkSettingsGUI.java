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

        // ### ITEMS ####

        String name = "PVP " + (team.getBorder().isPvpAllowed() ? "§a[TRUE]" : "§c[FALSE]");
        String lore = "Enables pvp in region";

        GuiItem pvpAllowed;
        pvpAllowed = ItemBuilder.from(Material.BOOK)
                .setName("PVP " + (team.getBorder().isPvpAllowed() ? "§a[TRUE]" : "§c[FALSE]"))
                .setLore("Enables pvp in region")
                .asGuiItem(event -> {
                    if (team.getBorder().isPvpAllowed()) {
                        team.getBorder().setPvpAllowed(false);
                    } else {
                        team.getBorder().setPvpAllowed(true);
                    }
                });

        gui.updateItem(1, ItemBuilder.from(Material.ENCHANTED_BOOK)
                .setName(name)
                .setLore(lore)
                .asGuiItem(event -> {
                    if (team.getBorder().isPvpAllowed()) {
                        team.getBorder().setPvpAllowed(false);
                    } else {
                        team.getBorder().setPvpAllowed(true);
                    }
                })
        );









        // SETTING ITEMS
//        gui.setItem(0, mobSpawning);
        gui.setItem(1,pvpAllowed);

        // NEXT OR PREVIOUS
        gui.setItem(4, 3, ItemBuilder.from(Material.PAPER).setName("Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(4, 7, ItemBuilder.from(Material.PAPER).setName("Next").asGuiItem(event -> gui.next()));

        // FILLER
        GuiItem filler = ItemBuilder.from(Material.GREEN_STAINED_GLASS_PANE).asGuiItem(inventoryClickEvent -> {
        });
        gui.setItem(27, filler);gui.setItem(28, filler);gui.setItem(30, filler);
        gui.setItem(31, filler);gui.setItem(32, filler);gui.setItem(34, filler);
        gui.setItem(35, filler);

        gui.open(player);
    }
}

package com.jasper.chunkBlock.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import net.kyori.adventure.text.Component;

import java.util.function.Consumer;

public class BaseGui {

    private final Gui gui;

    public BaseGui(String title, int rows) {
        this.gui = Gui.gui()
                .title(Component.text(title))
                .rows(rows)
                .create();
    }

    public BaseGui addItem(int slot, @NotNull Material material, @NotNull String name, @NotNull Consumer<Player> clickAction) {
        GuiItem item = ItemBuilder.from(material)
                .name(Component.text(name))
                .asGuiItem(event -> {
                    Player player = (Player) event.getWhoClicked();
                    clickAction.accept(player);
                });

        gui.setItem(slot, item);
        return this;
    }

    public Gui build() {
        return gui;
    }
}

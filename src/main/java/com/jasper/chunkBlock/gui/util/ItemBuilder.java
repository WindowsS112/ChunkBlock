package com.jasper.chunkBlock.gui.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;

import java.util.Arrays;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        meta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}




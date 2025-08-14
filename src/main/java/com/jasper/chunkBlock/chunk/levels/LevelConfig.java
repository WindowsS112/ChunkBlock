package com.jasper.chunkBlock.chunk.levels;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevelConfig {
    private final int level;
    private final int chunkSize;
    private final Map<Material, Integer> maxBlocks;
    private final List<String> unlocks;
    private final int cost;

    public LevelConfig(int level, int chunkSize, Map<Material, Integer> maxBlocks, List<String> unlocks, int cost) {
        this.level = level;
        this.chunkSize = chunkSize;
        this.maxBlocks = maxBlocks;
        this.unlocks = unlocks;
        this.cost = cost;
    }

    public List<String> toLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Max Blocks:");

        for (Map.Entry<Material, Integer> entry : maxBlocks.entrySet()) {
            String name = entry.getKey().name().toLowerCase().replace("_", " ");
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);

            lore.add(ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + name + ": " + ChatColor.YELLOW + entry.getValue());
        }

        lore.add("");
        lore.add(ChatColor.GRAY + "Cost: " + ChatColor.WHITE + cost);

        return lore;
    }

    // getters
    public int getLevel() {
        return level;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public List<String> getUnlocks() {
        return unlocks;
    }

    public Map<Material, Integer> getMaxBlocks() {
        return maxBlocks;
    }
}

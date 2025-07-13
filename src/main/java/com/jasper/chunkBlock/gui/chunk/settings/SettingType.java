package com.jasper.chunkBlock.gui.chunk.settings;

import org.bukkit.Material;

public enum SettingType {
    PVP("PvP", Material.DIAMOND_SWORD, true),
    EXPLOSIONS("Explosions", Material.TNT, true),
    VISITORS("Visitors Allowed", Material.PLAYER_HEAD, true),
    BORDER("Toon Rand", Material.BARRIER, true);

    private final String displayName;
    private final Material icon;
    private final boolean defaultValue;

    SettingType(String displayName, Material icon, boolean defaultValue) {
        this.displayName = displayName;
        this.icon = icon;
        this.defaultValue = defaultValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getIcon() {
        return icon;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }
}
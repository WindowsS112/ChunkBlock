package com.jasper.chunkBlock.gui.chunk.settings;

import com.jasper.chunkBlock.commands.border.Border;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.Flags;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public enum SettingType {

    PVP("PvP", Material.DIAMOND_SWORD, true, Flags.PVP) {
        @Override
        public boolean shouldCancel(Player player, Block block, Border border) {
            return !border.isSettingEnabled(this);
        }
    },

    EXPLOSIONS("Explosions", Material.TNT, true, Flags.TNT) {
        @Override
        public boolean shouldCancel(Player player, Block block, Border border) {
            return !border.isSettingEnabled(this);
        }
    },

    VISITORS("Visitors Allowed", Material.PLAYER_HEAD, true, Flags.ENTRY) {
        @Override
        public boolean shouldCancel(Player player, Block block, Border border) {
            return !border.isSettingEnabled(this);
        }
    };

    private final String displayName;
    private final Material icon;
    private final boolean defaultValue;
    private final StateFlag flag;

    SettingType(String displayName, Material icon, boolean defaultValue, StateFlag flag) {
        this.displayName = displayName;
        this.icon = icon;
        this.defaultValue = defaultValue;
        this.flag = flag;
    }

    public abstract boolean shouldCancel(Player player, Block block, Border border);

    public String getDisplayName() {
        return displayName;
    }
    public Material getIcon() {
        return icon;
    }
    public boolean getDefaultValue() {
        return defaultValue;
    }
    public StateFlag getFlag() {
        return flag;
    }
}

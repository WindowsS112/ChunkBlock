package com.jasper.chunkBlock.commands.border;

import com.jasper.chunkBlock.commands.SubCommand;
import org.bukkit.entity.Player;

public class BorderUpgradeCommand extends SubCommand {

    public BorderUpgradeCommand(String name, String description, String syntax) {
        super(name, description, syntax);
    }

    @Override
    public String getName() {
        return "upgrade";
    }

    @Override
    public String getDescription() {
        return "Upgrades border";
    }

    @Override
    public String getSyntax() {
        return "/c upgrade";
    }

    @Override
    public void perform(Player player, String[] args) {

    }
}

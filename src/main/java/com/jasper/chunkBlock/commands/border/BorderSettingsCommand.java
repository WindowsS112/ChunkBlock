package com.jasper.chunkBlock.commands.border;

import com.jasper.chunkBlock.commands.SubCommand;
import org.bukkit.entity.Player;

public class BorderSettingsCommand extends SubCommand {

    public BorderSettingsCommand(String name, String description, String syntax) {
        super(name, description, syntax);
    }

    @Override
    public void perform(Player player, String[] args) {

    }

    @Override
    public String getName() {
        return "border";
    }

    @Override
    public String getDescription() {
        return "Opens the border settings GUI for the chunk.";
    }

    @Override
    public String getSyntax() {
        return "/c border>";
    }
}

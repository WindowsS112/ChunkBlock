package com.jasper.chunkBlock.commands.util;

import com.jasper.chunkBlock.commands.CommandManager;
import com.jasper.chunkBlock.commands.SubCommand;
import org.bukkit.entity.Player;

public class HulpCommand extends SubCommand {

    private final CommandManager commandManager;

    public HulpCommand(String name, String description, String syntax, CommandManager commandManager) {
        super(name, description, syntax);
        this.commandManager = commandManager;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows this page";
    }

    @Override
    public String getSyntax() {
        return "/c help <page>";
    }

    @Override
    public void perform(Player player, String[] args) {
        commandManager.showHelp(player);
    }
}

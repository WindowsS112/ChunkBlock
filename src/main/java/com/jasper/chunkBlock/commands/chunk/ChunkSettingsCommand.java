package com.jasper.chunkBlock.commands.chunk;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.gui.chunk.ChunkSettings;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;


public class ChunkSettingsCommand extends SubCommand {

    public ChunkSettingsCommand(String name, String description, String syntax) {
        super(name, description, syntax);
    }

    @Override
    public void perform(Player player, String[] args) {
        new ChunkSettings().open(player);
    }








    @Override
    public String getName() {
        return "settings";
    }
    @Override
    public String getDescription() {
        return "Opens your chunk settings";
    }
    @Override
    public String getSyntax() {
        return "/c settings";
    }
}

package com.jasper.chunkBlock.commands.chunk;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.gui.chunk.ChunkUpgradeGUI;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class ChunkUpgradeCommand extends SubCommand {

    private final TeamStorage teamStorage;

    public ChunkUpgradeCommand(String name, String description, String syntax, TeamStorage teamStorage) {
        super(name, description, syntax);
        this.teamStorage = teamStorage;
    }

    @Override
    public void perform(Player player, String[] args) {
        ChunkUpgradeGUI ch = new ChunkUpgradeGUI(player, teamStorage.getTeamFromPlayer(player.getUniqueId()));
        ch.open();
    }

    @Override
    public String getName() {
        return "upgrade";
    }

    @Override
    public String getDescription() {
        return "Upgrade your chunk to a higher level";
    }

    @Override
    public String getSyntax() {
        return "/c upgrade";
    }
}

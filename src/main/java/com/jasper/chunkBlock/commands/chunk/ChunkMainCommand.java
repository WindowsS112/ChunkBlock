package com.jasper.chunkBlock.commands.chunk;

import com.jasper.chunkBlock.gui.chunk.ChunkMainGUI;
import com.jasper.chunkBlock.util.TeamManager;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.entity.Player;

public class ChunkMainCommand {

    private final TeamManager teamManager;
    private final TeamStorage teamStorage;

    public ChunkMainCommand(TeamManager teamManager, TeamStorage teamStorage) {
        this.teamStorage = teamStorage;
        this.teamManager = teamManager;
    }

    public void open(Player player, TeamManager teamManager) {
        ChunkMainGUI chunkMainGUI = new ChunkMainGUI(player, teamManager.getTeamFromPlayer(player.getUniqueId()));
        chunkMainGUI.open();
    }
}

package com.jasper.chunkBlock.commands.chunk;

import com.jasper.chunkBlock.gui.chunk.ChunkMainGUI;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.entity.Player;

public class ChunkMainCommand {

    private final TeamStorage teamStorage;

    public ChunkMainCommand(TeamStorage teamStorage) {
        this.teamStorage = teamStorage;
    }

    public void open(Player player, TeamStorage teamStorage) {
        ChunkMainGUI chunkMainGUI = new ChunkMainGUI(player, teamStorage.getTeamFromPlayer(player.getUniqueId(),team));
        chunkMainGUI.open();
    }
}

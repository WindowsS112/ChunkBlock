package com.jasper.chunkBlock.commands.chunk;

import com.jasper.chunkBlock.gui.chunk.ChunkMainGUI;
import com.jasper.chunkBlock.team.TeamService;
import org.bukkit.entity.Player;

public class ChunkMainCommand {

    public void open(Player player, TeamService teamService) {
        ChunkMainGUI chunkMainGUI = new ChunkMainGUI(player,teamService.getTeamByPlayer(player.getUniqueId()));
        chunkMainGUI.open();
    }
}

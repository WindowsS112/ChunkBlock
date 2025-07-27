package com.jasper.chunkBlock.gui.chunk;

import com.jasper.chunkBlock.chunk.Team;
import com.jasper.chunkBlock.gui.base.ConfirmationGUI;
import com.jasper.chunkBlock.util.MessageUtils;
import com.jasper.chunkBlock.util.RegionSynchronizer;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.entity.Player;

public class DisbandChunkGUI extends ConfirmationGUI {

    private Player player;
    private Team team;
    private BorderStorage borderStorage;
    private TeamStorage teamStorage;

    public DisbandChunkGUI(Player player, Team team, BorderStorage borderStorage, TeamStorage teamStorage) {
        super(player, "Confirmation");
        this.team = team;
        this.player = player;
        this.borderStorage = borderStorage;
        this.teamStorage = teamStorage;
    }

    @Override
    protected void onConfirm() {
        RegionSynchronizer.deleteRegion(team,borderStorage.getBorder(team));
                    teamStorage.removeTeam(team, player);
                    borderStorage.removeBorder(team);
        MessageUtils.sendSuccess(player, "Chunk has succesfully been disbanded!");
    }

    @Override
    protected void onDeny() {
        // Do nothing, just close the inventory
    }

}


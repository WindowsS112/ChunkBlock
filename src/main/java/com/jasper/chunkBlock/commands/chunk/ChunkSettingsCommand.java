package com.jasper.chunkBlock.commands.chunk;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.gui.chunk.ChunkSettingsGUI;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.entity.Player;


public class ChunkSettingsCommand extends SubCommand {

    private Team team;
    private TeamStorage teamStorage;
    public ChunkSettingsCommand(String name, String description, String syntax, Team team, TeamStorage teamStorage) {
        super(name, description, syntax);
        this.team = team;
        this.teamStorage = teamStorage;
    }

    @Override
    public void perform(Player player, String[] args) {
        if (teamStorage.isPlayerInAnyTeam(player.getUniqueId())) {
            Team team1 = teamStorage.getTeamFromPlayer(player.getUniqueId());
            new ChunkSettingsGUI().open(player, team1);
        } else {
            player.sendMessage("You are not in a team");
        }
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

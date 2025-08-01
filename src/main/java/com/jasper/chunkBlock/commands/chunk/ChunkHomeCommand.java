package com.jasper.chunkBlock.commands.chunk;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.chunk.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ChunkHomeCommand extends SubCommand {

    private Team team;
    private BorderStorage borderStorage;
    private TeamStorage teamStorage;

    public ChunkHomeCommand(String name, String description, String syntax,Team team, BorderStorage borderStorage, TeamStorage teamStorage) {
        super(name, description, syntax);
        this.borderStorage = borderStorage;
        this.teamStorage = teamStorage;
        this.team = team;
    }

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public String getDescription() {
        return "Teleports to chunk home";
    }

    @Override
    public String getSyntax() {
        return "/c home";
    }

    @Override
    public void perform(Player player, String[] args) {
        Team playerTeam = teamStorage.getTeamFromPlayer(player.getUniqueId());
        if (teamStorage.checkTeamExist(playerTeam)) {
            Location home = borderStorage.getHomeLocation(playerTeam);
            if (home != null) {
                player.teleport(home);
            } else {
                player.sendMessage("Your team does not have a home set.");
            }
        } else {
            player.sendMessage("Team does not exist");
        }
    }
}

package com.jasper.chunkBlock.commands.chunk;

import com.jasper.chunkBlock.chunk.ChunkStorage;
import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.chunk.Team;
import com.jasper.chunkBlock.util.MessageUtils;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ChunkSetHomeCommand extends SubCommand {

    private Team team;
    private BorderStorage borderStorage;
    private TeamStorage teamStorage;
    private YamlConfiguration borderData;

    public ChunkSetHomeCommand(String name, String description, String syntax, Team team, ChunkStorage chunkStorage, TeamStorage teamStorage) {
        super(name, description, syntax);
        this.teamStorage = teamStorage;
        this.team = team;
    }

    @Override
    public void perform(Player player, String[] args) {
        Team playerTeam = teamStorage.getTeamFromPlayer(player.getUniqueId());
        if (teamStorage.checkTeamExist(playerTeam)) {
            Location home = player.getLocation().clone();
//            ClaimedChunk claimedChunk = ChunkStorage.getChunk();
//            claimedChunk.setHome(home);
//            Border border = borderStorage.getBorder(playerTeam);
//            border.setHome(home);
            MessageUtils.sendSuccess(player,"Succesfully set new home");
        } else {
            MessageUtils.sendError(player,"&cYou don't have a team");
        }
    }

    @Override
    public String getName() {
        return "sethome";
    }

    @Override
    public String getDescription() {
        return "Sets a chunk home";
    }

    @Override
    public String getSyntax() {
        return "/c sethome";
    }
}

package com.jasper.chunkBlock.commands.chunk;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.Border;
import com.jasper.chunkBlock.util.BorderStorage;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ChunkSetHomeCommand extends SubCommand {

    private Team team;
    private BorderStorage borderStorage;
    private TeamStorage teamStorage;
    private YamlConfiguration borderData;

    public ChunkSetHomeCommand(String name, String description, String syntax,Team team, BorderStorage borderStorage, TeamStorage teamStorage) {
        super(name, description, syntax);
        this.borderStorage = borderStorage;
        this.teamStorage = teamStorage;
        this.team = team;
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

    @Override
    public void perform(Player player, String[] args) {
        Team playerTeam = teamStorage.getTeamFromPlayer(player.getUniqueId());
        if (teamStorage.checkTeamExist(playerTeam)) {
            Location home = player.getLocation().clone();
            Border border = borderStorage.getBorder(playerTeam);
            border.setHome(home);
        } else {
            player.sendMessage("Team does not exist");
        }
    }
}

package com.jasper.chunkBlock.commands.util;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.chunk.Team;
import com.jasper.chunkBlock.util.MessageUtils;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.entity.Player;

import java.util.Map;

public class ShowTeams extends SubCommand {

    TeamStorage teamStorage;

    public ShowTeams(String name, String description, String syntax, TeamStorage teamStorage) {
        super(name, description, syntax);
        this.teamStorage = teamStorage;
    }

    @Override
    public void perform(Player player, String[] args) {
        int i = 0;
        for (Map.Entry<String, Team> entry : teamStorage.getTeams().entrySet()) {
            String teamName = entry.getKey();
            Team team = entry.getValue();

            MessageUtils.sendInfo(player, teamName + ", &7" + team.getMembersOfTeam().size());
            i++;
        }
        MessageUtils.sendSuccess(player, "Total amount of teams: &7" + i);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Displays a list of all the teams";
    }

    @Override
    public String getSyntax() {
        return "/c list";
    }
}

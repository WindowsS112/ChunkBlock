package com.jasper.chunkBlock.commands.team;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class JoinTeamCommand extends SubCommand {

    private Team team;
    private TeamStorage teamStorage;

    public JoinTeamCommand(String name, String description, String syntax, Team team, TeamStorage teamStorage) {
        super(name, description, syntax);
        this.team = team;
        this.teamStorage = teamStorage;
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Joins a team";
    }

    @Override
    public String getSyntax() {
        return "/c join <teamname>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1) {
            String teamName = args[1];
            Team team = teamStorage.getTeamByName(teamName);


            if (!teamStorage.isPlayerInAnyTeam(player.getUniqueId())) {
                if (teamStorage.checkTeamExist(team)) {
                    teamStorage.addMemberToTeam(teamName, player.getUniqueId());
                }
            } else {
                player.sendMessage(ChatColor.RED + "You are already in a team, leave that first");
            }
        }
    }
}

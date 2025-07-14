package com.jasper.chunkBlock.commands.team;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.BorderStorage;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DisbandTeamCommand extends SubCommand {
    private Team team;
    private BorderStorage borderStorage;
    private TeamStorage teamStorage;


    public DisbandTeamCommand(String name, String description, String syntax, Team team, TeamStorage teamStorage, BorderStorage borderStorage) {
        super(name, description, syntax);
        this.teamStorage = teamStorage;
        this.team = team;
        this.borderStorage = borderStorage;
    }

    @Override
    public String getName() {
        return "disband";
    }

    @Override
    public String getDescription() {
        return "Deletes a team";
    }

    @Override
    public String getSyntax() {
        return "/c disband <teamname>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1) {
            String teamName = args[1];
            Team team = teamStorage.getTeamByName(teamName); // Get the team by name

            if (team == null) {
                player.sendMessage(ChatColor.RED + "Team does not exist.");
                return;
            }

            if (teamStorage.isPlayerInAnyTeam(player.getUniqueId())) {
                if (teamStorage.checkTeamExist(team) && team.getOwner().equals(player.getUniqueId())) {
                    teamStorage.removeTeam(team, player);
                    borderStorage.removeBorder(team);
                } else if (!team.getOwner().equals(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You are not the owner of this team");
                } else {
                    player.sendMessage(ChatColor.RED + "Team does not exist.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You don't have a team to leave");
            }
        }
    }
}

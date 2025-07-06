package com.jasper.chunkBlock.commands.team;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LeaveTeamCommand extends SubCommand {

    private Team team;
    private TeamStorage teamStorage;

    public LeaveTeamCommand(String name, String description, String syntax, Team team, TeamStorage teamStorage) {
        super(name, description, syntax);
        this.team = team;
        this.teamStorage = teamStorage;
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Leaves a team";
    }

    @Override
    public String getSyntax() {
        return "/c leave";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1) {
            String teamName = args[1];
            Team team = teamStorage.getTeamByName(teamName);

            if (teamStorage.isPlayerInAnyTeam(player.getUniqueId())) {
                if (teamStorage.checkTeamExist(team) && !team.getOwner().equals(player.getUniqueId())) {
                    teamStorage.removeMemberFromTeam(teamName,player.getUniqueId());
                } else if (team.getOwner().equals(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You are the owner of this team, do /c disband to delete the team");
                } else {
                    player.sendMessage(ChatColor.RED + "Team does not exist.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You don't have a team to leave");
            }
        }
    }
}

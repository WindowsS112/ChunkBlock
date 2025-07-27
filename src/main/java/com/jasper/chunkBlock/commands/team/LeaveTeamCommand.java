package com.jasper.chunkBlock.commands.team;

import com.jasper.chunkBlock.chunk.Team;
import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LeaveTeamCommand extends SubCommand {

    private Team team;
    private TeamStorage teamStorage;
    private BorderStorage borderStorage;

    public LeaveTeamCommand(String name, String description, String syntax, Team team, TeamStorage teamStorage, BorderStorage borderStorage) {
        super(name, description, syntax);
        this.team = team;
        this.teamStorage = teamStorage;
        this.borderStorage = borderStorage;
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
            Team team = teamStorage.getTeamByName(teamName); // Always fetch the team by name

            if (team == null) {
                player.sendMessage(ChatColor.RED + "Team does not exist.");
                return;
            }

            if (!team.getMembersOfTeam().contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You are not a member of this team.");
                return;
            }

            if (team.getOwner().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You are the owner. Use /c disband to delete the team.");
                return;
            }

            teamStorage.removeMemberFromTeam(teamName, player.getUniqueId());
            borderStorage.removeBorder(player);
            player.sendMessage(ChatColor.GREEN + "You have left the team " + teamName + ".");
        } else {
            player.sendMessage(ChatColor.RED + "Please specify a team name.");
        }
    }
}

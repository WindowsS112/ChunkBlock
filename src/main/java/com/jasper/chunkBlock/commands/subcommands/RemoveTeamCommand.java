package com.jasper.chunkBlock.commands.subcommands;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RemoveTeamCommand extends SubCommand {

    private Team team;
    private TeamStorage teamStorage;
    private String teamName;

    public RemoveTeamCommand(String name, String description, String syntax, String teamName, Team team, TeamStorage teamStorage) {
        super(name, description, syntax);
        this.team = team;
        this.teamStorage = teamStorage;
        this.teamName = teamName;
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Removes a team";
    }

    @Override
    public String getSyntax() {
        return "/c remove";
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1) {
            String teamName = args[1];
            Team team = teamStorage.getTeams().get(teamName);

            if (team == null) {
                player.sendMessage(ChatColor.RED + "Team " + teamName + " does not exist.");
                return;
            }

            if (!teamStorage.isPlayerInAnyTeam(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You are not in a team. Create one with: /c create <name>");
                return;
            }

            if (!team.getOwner().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You are not the owner of team: " + team.getTeamName());
                return;
            }

            // Remove the team since the player is the owner
            teamStorage.removeTeam(team);
            player.sendMessage(ChatColor.GREEN + "Team " + team.getTeamName() + " has been removed.");

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /c removeteam <teamName>");
        }
    }

}

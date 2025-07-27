package com.jasper.chunkBlock.commands.team;

import com.jasper.chunkBlock.chunk.Team;
import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class JoinTeamCommand extends SubCommand {

    private Team team;
    private TeamStorage teamStorage;
    private BorderStorage borderStorage;

    public JoinTeamCommand(String name, String description, String syntax, Team team, TeamStorage teamStorage, BorderStorage borderStorage) {
        super(name, description, syntax);
        this.team = team;
        this.teamStorage = teamStorage;
        this.borderStorage = borderStorage;
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
            if (teamStorage.checkTeamExist(team)) {
                if (!teamStorage.isPlayerInAnyTeam(player.getUniqueId())) {
                    teamStorage.addMemberToTeam(team, player.getUniqueId());
                    borderStorage.loadBorder(team);
                    player.teleport(borderStorage.getBorder(team).getDefaultHome());
                } else {
                    player.sendMessage(ChatColor.RED + "You are already in a team, leave " + teamName + " first");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Team " + args[1] +" does not exist!");
            }
        }
    }
}

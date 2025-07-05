package com.jasper.chunkBlock.commands.subcommands;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CreateTeamCommand extends SubCommand {

    private String teamName;
    private final JavaPlugin plugin;
    private final TeamStorage teamStorage;
    private String owner;

    public CreateTeamCommand(String name, String description, String syntax, JavaPlugin plugin, String teamName, TeamStorage teamStorage) {
        super(name, description, syntax);
        this.plugin = plugin;
        this.teamName = teamName;
        this.teamStorage = teamStorage;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Creates a chunk team";
    }

    @Override
    public String getSyntax() {
        return "/c create";
    }

    public String getTeamName() {
        return teamName;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1) {
            teamName = args[1];
            owner = player.getUniqueId().toString();


            teamStorage.check(player.getUniqueId());

            if (!teamStorage.isPlayerInAnyTeam(player.getUniqueId())) {
                Team team = new Team(player.getUniqueId().toString(),player.getUniqueId(),teamName);
                teamStorage.addTeam(team);
                team.joinTeam(player.getUniqueId(),teamStorage);
            } else {
                player.sendMessage(ChatColor.RED + "You are already in a chunkparty, leave this one first!");
            }

        }
    }




}

package com.jasper.chunkBlock.commands.team;

import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.TeamManager;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.UUID;

public class CreateTeamCommand extends SubCommand {

    private String teamName;
    private final JavaPlugin plugin;
    private final TeamStorage teamStorage;
    private UUID owner;
    private TeamManager teamManager;

    public CreateTeamCommand(String name, String description, String syntax, JavaPlugin plugin, String teamName, TeamStorage teamStorage, TeamManager teamManager) {
        super(name, description, syntax);
        this.plugin = plugin;
        this.teamName = teamName;
        this.teamStorage = teamStorage;
        this.teamManager = teamManager;
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

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1) {
            teamName = args[1];
            owner = player.getUniqueId();

            if (!teamStorage.isPlayerInAnyTeam(owner)) {
                Team team = teamManager.createTeam(teamName, player);
                player.sendMessage(ChatColor.GREEN + "Succesfully created: " + team.getTeamName() + "!");
            } else {
                player.sendMessage(ChatColor.RED + "You are already in a chunkparty, leave this one first!");
            }

        }
    }




}

package com.jasper.chunkBlock.commands;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.border.BorderBypassCommand;
import com.jasper.chunkBlock.commands.chunk.ChunkMainCommand;
import com.jasper.chunkBlock.commands.chunk.ChunkSettingsCommand;
import com.jasper.chunkBlock.commands.chunk.ChunkHomeCommand;
import com.jasper.chunkBlock.commands.chunk.ChunkSetHomeCommand;
import com.jasper.chunkBlock.commands.team.*;
import com.jasper.chunkBlock.commands.util.HulpCommand;
import com.jasper.chunkBlock.commands.util.ShowTeams;
import com.jasper.chunkBlock.util.BorderStorage;
import com.jasper.chunkBlock.commands.team.Team;
import com.jasper.chunkBlock.util.TeamManager;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private ArrayList<SubCommand> subcommands = new ArrayList<>();
    private BorderStorage borderStorage;
    private double cSize;
    private File borderData;
    private ChunkBlock plugin;
    private TeamManager teamManager;
    private TeamStorage teamStorage;

    public CommandManager(BorderStorage borderStorage, double cSize, File borderData, ChunkBlock plugin, TeamStorage teamStorage, Team team,TeamManager teamManager) {
        this.borderStorage = borderStorage;
        this.cSize = cSize;
        this.borderData = borderData;
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.teamStorage = teamStorage;
        subcommands.add(new BorderBypassCommand("", "", "", this.borderStorage));
        subcommands.add(new HulpCommand("", "", "", this));
        subcommands.add(new CreateTeamCommand("", "", "",plugin,"",teamStorage,teamManager));
        subcommands.add(new LeaveTeamCommand("","","",team, teamStorage,borderStorage));
        subcommands.add(new JoinTeamCommand("","","",team,teamStorage,borderStorage));
        subcommands.add(new DisbandTeamCommand("","","",team,teamStorage,borderStorage));
        subcommands.add(new ChunkHomeCommand("","","",team, borderStorage, teamStorage));
        subcommands.add(new ChunkSetHomeCommand("","","",team,borderStorage, teamStorage));
        subcommands.add(new ChunkSettingsCommand("","","",team,teamStorage));
        subcommands.add(new ShowTeams("","","",teamStorage));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;

        if (label.equalsIgnoreCase("c")) {
            if (args.length == 0) {
                if (teamStorage.isPlayerInAnyTeam(p.getUniqueId())) {
                    ChunkMainCommand c = new ChunkMainCommand(teamStorage);
                    c.open(p, teamStorage);
                } else {
                    showHelp(p);
                }
                return true;
            } else {
                for (SubCommand sub : this.getSubCommands()) {
                    if (args[0].equalsIgnoreCase(sub.getName())) {
                        sub.perform(p, args);
                        return true;
                    }
                }
                showHelp(p);
                return true;
            }
        }
        return true;
    }

    public void showHelp(Player p) {
        p.sendMessage(ChatColor.BLUE + "ChunkBlock");

        for (int i = 0; i < getSubCommands().size(); i++) {
            p.sendMessage(ChatColor.AQUA + subcommands.get(i).getSyntax() + ChatColor.GRAY +  " -> " + getSubCommands().get(i).getDescription());
        }
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subcommands;
    }

}

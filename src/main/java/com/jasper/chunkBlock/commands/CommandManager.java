package com.jasper.chunkBlock.commands;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.border.BorderBypassCommand;
import com.jasper.chunkBlock.commands.chunk.ChunkSettingsCommand;
import com.jasper.chunkBlock.commands.chunk.ClaimCommand;
import com.jasper.chunkBlock.commands.chunk.ChunkHomeCommand;
import com.jasper.chunkBlock.commands.chunk.ChunkSetHomeCommand;
import com.jasper.chunkBlock.commands.team.*;
import com.jasper.chunkBlock.commands.util.HulpCommand;
import com.jasper.chunkBlock.gui.BaseGui;
import com.jasper.chunkBlock.util.BorderStorage;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
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

    public CommandManager(BorderStorage borderStorage, double cSize, File borderData, ChunkBlock plugin, TeamStorage teamStorage, Team team) {
        this.borderStorage = borderStorage;
        this.cSize = cSize;
        this.borderData = borderData;
        this.plugin = plugin;
        subcommands.add(new BorderBypassCommand("", "", "", this.borderStorage));
        subcommands.add(new HulpCommand("", "", "", this));
        subcommands.add(new CreateTeamCommand("", "", "",plugin,"",teamStorage));
        subcommands.add(new RemoveTeamCommand("", "", "", "", team, teamStorage));
        subcommands.add(new LeaveTeamCommand("","","",team, teamStorage));
        subcommands.add(new JoinTeamCommand("","","",team,teamStorage,borderStorage));
        subcommands.add(new DisbandTeamCommand("","","",team,teamStorage,borderStorage));
        subcommands.add(new ClaimCommand("","","",team,teamStorage, plugin,borderStorage));
        subcommands.add(new ChunkHomeCommand("","","",team, borderStorage, teamStorage));
        subcommands.add(new ChunkSetHomeCommand("","","",team,borderStorage, teamStorage));
        subcommands.add(new ChunkSettingsCommand("","","",team,teamStorage));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                for (SubCommand sub : this.getSubCommands()) {
                    if (args[0].equalsIgnoreCase(sub.getName())) {
                        sub.perform(p, args);
                        return true;
                    }
                }
                showHelp(p);
            } else {
                // No arguments: call the ChunkGUI command
                for (SubCommand sub : this.getSubCommands()) {
                    if (sub.getName().equalsIgnoreCase("chunk")) {
                        sub.perform(p, args);
                        return true;
                    }
                }
                showHelp(p);
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

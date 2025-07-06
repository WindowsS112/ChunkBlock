package com.jasper.chunkBlock.commands;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.subcommands.*;
import com.jasper.chunkBlock.util.BorderStorage;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private ArrayList<SubCommand> subcommands = new ArrayList<>();
    private BorderStorage borderStorage;
    private double cSize;
    private File borderData;
    private ChunkBlock plugin;
    private TeamStorage teamStorage;
    private Team team;


    public CommandManager(BorderStorage borderStorage, double cSize, File borderData, ChunkBlock plugin, TeamStorage teamStorage, Team team ) {
        this.borderStorage = borderStorage;
        this.cSize = cSize;
        this.borderData = borderData;
        this.plugin = plugin;
        subcommands.add(new BypassCommand("", "", "", this.borderStorage));
        subcommands.add(new CreateCommand("", "", "", cSize, borderData, plugin));
        subcommands.add(new HulpCommand("", "", "", this));
        subcommands.add(new CreateTeamCommand("", "", "",plugin,"",teamStorage));
        subcommands.add(new RemoveTeamCommand("", "", "", "", team, teamStorage));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
             if (args.length > 0) {
                for (int i = 0; i < this.getSubCommands().size(); i++ ) {
                    if (args[0].equalsIgnoreCase(this.getSubCommands().get(i).getName())) {
                        this.getSubCommands().get(i).perform(p, args);

                        return true;
                    }
                }
            } else if (args.length == 0 ) {
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

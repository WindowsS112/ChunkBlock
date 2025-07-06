package com.jasper.chunkBlock.commands.border;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.BorderStorage;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class ClaimCommand extends SubCommand {

    private Team team;
    private TeamStorage teamStorage;
    private ChunkBlock plugin;
    private BorderStorage borderStorage;

    public ClaimCommand(String name, String description, String syntax, Team team, TeamStorage teamStorage, ChunkBlock plugin, BorderStorage borderStorage) {
        super(name, description, syntax);
        this.teamStorage = teamStorage;
        this.team = team;
        this.plugin = plugin;
        this.borderStorage = borderStorage;
    }

    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public String getDescription() {
        return "Claims a chunk for the team";
    }

    @Override
    public String getSyntax() {
        return "/c claim";
    }

    public void createBorder() {

    }

    @Override
    public void perform(Player player, String[] args) {
        UUID playerUUID = player.getUniqueId();

        Team team = teamStorage.getTeamFromPlayer(playerUUID);
        if (team == null) {
            player.sendMessage(ChatColor.RED + "Je zit niet in een team.");
            return;
        }

        if (!playerUUID.equals(team.getOwner())) {
            player.sendMessage(ChatColor.RED + "Alleen de team owner kan een chunk claimen.");
            return;
        }

        int radius = plugin.getConfig().getInt("defaultChunkSize", 50); // fallback waarde als config key ontbreekt
        Border border = new Border(player.getLocation(), radius);
        team.setBorder(border);

        // Optioneel: opslaan naar bestand
        borderStorage.saveChunk(team, border);
        borderStorage.loadChunk(team);

//        // Optioneel: worldborder instellen voor alle teamleden
//        border.applyToTeam(team);

        player.sendMessage(ChatColor.GREEN + "Je team heeft nu een border geclaimd op deze locatie met radius " + radius + ".");
    }


    public JavaPlugin getPlugin() {
        return plugin;
    }
}

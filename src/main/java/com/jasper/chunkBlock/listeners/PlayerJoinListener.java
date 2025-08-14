package com.jasper.chunkBlock.listeners;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.chunk.ClaimedChunk;
import com.jasper.chunkBlock.database.Database;
import com.jasper.chunkBlock.team.Team;
import com.jasper.chunkBlock.team.TeamService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerJoinListener implements Listener {

    private final ChunkBlock plugin;
    private final TeamService teamService;
    private final Database database;

    public PlayerJoinListener(ChunkBlock plugin, TeamService teamService, Database database) {
        this.plugin = plugin;
        this.teamService = teamService;
        this.database = database;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Team team = teamService.getTeamByPlayer(player.getUniqueId());

        if (team == null) {
            Bukkit.getLogger().info("Player heeft geen team.");
            return;
        }


        ClaimedChunk chunk = database.getChunkByOwner(player.getUniqueId());

        Listener moveListener = new Listener() {
            @EventHandler
            public void onPlayerMove(PlayerMoveEvent moveEvent) {
                if (teamService.isPlayerInAnyTeam(player.getUniqueId())) {
                    if (moveEvent.getPlayer().equals(player)) {
                        teamService.applyBorderForPlayer(player, chunk);

                        HandlerList.unregisterAll(this);
                    }
                } else {
                    HandlerList.unregisterAll(this);
                }
            }
        };

        plugin.getServer().getPluginManager().registerEvents(moveListener, plugin);
    }
}

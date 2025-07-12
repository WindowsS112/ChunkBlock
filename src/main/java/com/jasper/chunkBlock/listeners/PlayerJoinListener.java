package com.jasper.chunkBlock.listeners;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.util.BorderStorage;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerJoinListener implements Listener {

    private final BorderStorage borderStorage;
    private final ChunkBlock plugin;
    private TeamStorage teamStorage;

    public PlayerJoinListener(BorderStorage borderStorage, ChunkBlock plugin, TeamStorage teamStorage) {
        this.borderStorage = borderStorage;
        this.plugin = plugin;
        this.teamStorage = teamStorage;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Team team = teamStorage.getTeamFromPlayer(player.getUniqueId());

        Listener moveListener = new Listener() {
            @EventHandler
            public void onPlayerMove(PlayerMoveEvent moveEvent) {
                if (teamStorage.isPlayerInAnyTeam(player.getUniqueId())) {
                    if (moveEvent.getPlayer().equals(player)) {
                        borderStorage.loadBorder(team);

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

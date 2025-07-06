package com.jasper.chunkBlock.listeners;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.util.BorderStorage;
import com.jasper.chunkBlock.util.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerJoinListener implements Listener {

    private final BorderStorage borderStorage;
    private final ChunkBlock plugin;
    private Team team;

    public PlayerJoinListener(BorderStorage borderStorage, ChunkBlock plugin, Team team) {
        this.borderStorage = borderStorage;
        this.plugin = plugin;
        this.team = team;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Listener moveListener = new Listener() {
            @EventHandler
            public void onPlayerMove(PlayerMoveEvent moveEvent) {
                if (moveEvent.getPlayer().equals(player)) {
                    borderStorage.loadChunk(team);

                    HandlerList.unregisterAll(this);
                }
            }
        };

        plugin.getServer().getPluginManager().registerEvents(moveListener, plugin);
    }
}

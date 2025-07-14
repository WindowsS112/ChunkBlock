package com.jasper.chunkBlock.listeners;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.gui.chunk.settings.SettingType;
import com.jasper.chunkBlock.util.Border;
import com.jasper.chunkBlock.util.Team;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ChunkBorderListener implements Listener {

    private final ChunkBlock plugin;

    public ChunkBorderListener(ChunkBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        Team team = plugin.getTeamStorage().getTeamFromPlayer(player.getUniqueId());
        if (team == null) return;

        Border border = plugin.getBorderStorage().getBorder(team);
        if (border == null) return;

        if (SettingType.EXPLOSIONS.shouldCancel(player, block, border)) {
            event.setCancelled(true);
            player.sendMessage("§cJe kunt hier geen blokken breken.");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player damager = (Player) event.getDamager();

        Team team = plugin.getTeamStorage().getTeamFromPlayer(damager.getUniqueId());
        if (team == null) return;

        Border border = plugin.getBorderStorage().getBorder(team);
        if (border == null) return;

        if (SettingType.PVP.shouldCancel(damager, null, border)) {
            event.setCancelled(true);
            damager.sendMessage("§cPvP is uitgeschakeld in dit gebied.");
        }
    }
}

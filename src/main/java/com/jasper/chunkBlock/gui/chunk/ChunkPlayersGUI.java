package com.jasper.chunkBlock.gui.chunk;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.jasper.chunkBlock.chunk.ClaimedChunk;
import com.jasper.chunkBlock.chunk.Team;
import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.util.MessageUtils;
import com.jasper.chunkBlock.util.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;


public class ChunkPlayersGUI {

    private final Player player;
    private final Team team;
    private final ClaimedChunk claimedChunk;
    private TeamManager teamManager;

    public ChunkPlayersGUI(Player player, Team team, ClaimedChunk claimedChunk) {
        this.player = player;
        this.team = team;
        this.claimedChunk = claimedChunk;
        this.teamManager = ChunkBlock.getInstance().getTeamManager();
        if (claimedChunk == null) {
            throw new IllegalStateException("Chunk not found for: " + team.getTeamName());
        }
    }

    public void open() {

        teamManager = ChunkBlock.getInstance().getTeamManager();
        ChestGui gui = new ChestGui(4, "Chunk - Players");

        PaginatedPane pages = new PaginatedPane(0, 0, 9, 3);
        OutlinePane settingsPane = new OutlinePane(0, 0, 9, 3);

        for (UUID memberUUID : team.getMembersOfTeam()) {
            OfflinePlayer member = Bukkit.getOfflinePlayer(memberUUID);

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(member);

            String rank;

            if (team.getOwner().equals(member)) {
                rank = "owner";
            } else {
                rank = "member";
            }

            meta.setDisplayName("§e" + member.getName() + " " + rank);
            meta.setLore(List.of("§7UUID: " + memberUUID.toString()));
            skull.setItemMeta(meta);

            GuiItem guiItem = new GuiItem(skull, event -> {
                event.setCancelled(true);

                teamManager.removeMember(team.getTeamName(), memberUUID);
                MessageUtils.sendSuccess(player,  team.getOwner().toString());
            });

            settingsPane.addItem(guiItem);
        }


        pages.addPane(0, settingsPane); // Voeg settings toe aan pagina 0
        gui.addPane(pages);

        OutlinePane background = new OutlinePane(0, 3, 9, 1);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        gui.addPane(background);

        StaticPane navigation = new StaticPane(0, 3, 9, 1);
        navigation.addItem(new GuiItem(new ItemStack(Material.RED_WOOL), event -> {
            if (pages.getPage() > 0) {
                pages.setPage(pages.getPage() - 1);

                gui.update();
            }
        }), 0, 0);

        navigation.addItem(new GuiItem(new ItemStack(Material.GREEN_WOOL), event -> {
            if (pages.getPage() < pages.getPages() - 1) {
                pages.setPage(pages.getPage() + 1);

                gui.update();
            }
        }), 8, 0);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta meta = barrier.getItemMeta();
        meta.setDisplayName("§cBack");
        barrier.setItemMeta(meta);

        navigation.addItem(new GuiItem(barrier, event -> {
            ChunkMainGUI ch = new ChunkMainGUI(player, team, claimedChunk);
            ch.open();
        }), 4, 0);

        gui.addPane(navigation);

        gui.show(player);
    }


}

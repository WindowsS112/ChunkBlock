package com.jasper.chunkBlock.gui.chunk;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.border.Border;
import com.jasper.chunkBlock.commands.team.Team;
import com.jasper.chunkBlock.util.MessageUtils;
import com.jasper.chunkBlock.util.TeamManager;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;


public class ChunkMainGUI {

    private Player player;
    private final Team team;
    private final Border border;
    private TeamStorage teamStorage;

    public ChunkMainGUI(Player player, Team team) {
        this.player = player;
        this.team = team;
        this.border = ChunkBlock.getInstance().getBorderStorage().getBorder(team);
        teamStorage = ChunkBlock.getInstance().getTeamStorage();
        if (this.border == null) {
            throw new IllegalStateException("Border niet d voor team: " + team.getTeamName());
        }
    }

    public void open() {
        TeamManager teamManager = ChunkBlock.getInstance().getTeamManager();

        ChestGui gui = new ChestGui(5, "Chunk - Menu");
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        PatternPane paneel = new PatternPane(0, 0, 9, 5, patroon);


        // FILLER PANE
        OutlinePane background = new OutlinePane(0, 0, 9, 5, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLUE_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        OutlinePane navigationPane = new OutlinePane(1, 1, 3, 3);
        OutlinePane circleCenter = new OutlinePane(6,2,1,1, Pane.Priority.HIGH);

        // HOME BUTTON
        ItemStack shop = new ItemStack(Material.OAK_DOOR);
        ItemMeta shopMeta = shop.getItemMeta();
        shopMeta.setDisplayName("Chunk - Home");
        shop.setItemMeta(shopMeta);
        navigationPane.addItem(new GuiItem(shop, event -> {
            player.teleport(border.getDefaultHome());
            MessageUtils.sendSuccess(player, "Teleported to your chunk home");
        }));

        // SETTINGS BUTTON
        ItemStack settings = new ItemStack(Material.BOOK);
        ItemMeta settingsMeta = settings.getItemMeta();
        settingsMeta.setDisplayName("Chunk - Settings");
        settings.setItemMeta(settingsMeta);
        navigationPane.addItem(new GuiItem(settings, event -> {
            ChunkSettingsGUI ch = new ChunkSettingsGUI(player, team);
            ch.open();
        }));

        // BORDER BUTTON
        ItemStack border = new ItemStack(Material.BARRIER);
        ItemMeta borderMeta = border.getItemMeta();
        borderMeta.setDisplayName("Chunk - Border ");
        border.setItemMeta(borderMeta);
        navigationPane.addItem(new GuiItem(border, event -> {
            //navigate to home
        }));

        // CHUNK PLAYERS
        ItemStack players = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta  playerMeta = players.getItemMeta();
        playerMeta.setDisplayName("Chunk - Players");
        players.setItemMeta(playerMeta);

        navigationPane.addItem(new GuiItem(players, event -> {
            ChunkPlayersGUI chunkPlayersGUI = new ChunkPlayersGUI(player,team);
            chunkPlayersGUI.open();
        }));

        // CHUNK TOP
        ItemStack chunkTop = new ItemStack(Material.ACACIA_HANGING_SIGN);
        ItemMeta  chunkTopMeta = chunkTop.getItemMeta();
        chunkTopMeta.setDisplayName("Chunk - Top");
        chunkTop.setItemMeta(chunkTopMeta);
        navigationPane.addItem(new GuiItem(chunkTop, event -> {
            ChunkTopGUI chunkTopGUI = new ChunkTopGUI(player,team);
            chunkTopGUI.open();
        }));

        // CHUNK UPGRADE
        ItemStack upgrade = new ItemStack(Material.CHEST);
        ItemMeta  upgradeMeta = upgrade.getItemMeta();
        upgradeMeta.setDisplayName("Chunk - Upgrade");
        upgrade.setItemMeta(upgradeMeta);
        navigationPane.addItem(new GuiItem(upgrade, event -> {
            ChunkUpgradeGUI upgradeGUI = new ChunkUpgradeGUI(player,team,teamStorage);
            upgradeGUI.open();
        }));

        // CHUNK CIRCLE STATS
        ItemStack chunk = new ItemStack(Material.BEACON);
        ItemMeta  chunkMeta = chunk.getItemMeta();
        chunkMeta.setDisplayName("Chunk - " + team.getTeamName());
        chunkMeta.setLore();

        chunk.setItemMeta(chunkMeta);
        circleCenter.addItem(new GuiItem(chunk, event -> {
            //logic
        }));

        // FILLER CIRCLE
        paneel.bindItem('1', new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));

        gui.addPane(background);
        gui.addPane(navigationPane);
        gui.addPane(paneel);
        gui.addPane(circleCenter);
        gui.show(player);
    }

    // FILLER LIGHT BLUE
    Pattern patroon = new Pattern(
            "000000000",
            "000001110",
            "000001010",
            "000001110",
            "000000000"
    );
}

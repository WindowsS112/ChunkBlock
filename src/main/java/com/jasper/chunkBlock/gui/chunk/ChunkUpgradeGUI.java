package com.jasper.chunkBlock.gui.chunk;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import com.jasper.chunkBlock.commands.team.Team;
import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.util.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;


public class ChunkUpgradeGUI {

    private final Player player;
    private final Team team;
    private TeamManager teamManager;

    private int teamLevel;

    public ChunkUpgradeGUI(Player player, Team team) {
        this.player = player;
        this.team = team;
        this.teamLevel = team.getLevel();
    }

    public void open() {
        teamManager = ChunkBlock.getInstance().getTeamManager();
        ChestGui gui = new ChestGui(5, "Chunk - Upgrade");
        StaticPane pane = new StaticPane(0, 0, 9, 4);
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        PatternPane paneel = new PatternPane(0, 0, 9, 5, patroon);

        ///////////////
        /// LEVELS ///
        //////////////

        // Level 1
        ItemStack levelOne = new ItemStack(Material.GRAY_WOOL);
        ItemMeta levelOneMeta = levelOne.getItemMeta();
        levelOneMeta.setDisplayName("Level 1 - " + (teamLevel >= 1 ? ChatColor.GREEN + "Unlocked" : ChatColor.RED + "Locked"));
        levelOne.setItemMeta(levelOneMeta);
        pane.addItem(new GuiItem(levelOne, event -> {
            // Logic
        }), 1, 2);

        // Level 2
        ItemStack levelTwo = new ItemStack(Material.GREEN_WOOL);
        ItemMeta levelTwoMeta = levelTwo.getItemMeta();
        levelTwoMeta.setDisplayName("Level 2 - " + (teamLevel >= 2 ? ChatColor.GREEN + "Unlocked" : ChatColor.RED + "Locked"));
        levelTwoMeta.setLore(
                List.of(ChatColor.GRAY + "Cost: 10,000 Coins",
                        ChatColor.GRAY + "Unlocks 5 features",
                        " ",
                        ChatColor.WHITE + "Click to upgrade!"
        ));
        levelTwo.setItemMeta(levelTwoMeta);
        pane.addItem(new GuiItem(levelTwo, event -> {
            // Logic
        }), 4, 2);

        // Level 3
        ItemStack levelThree = new ItemStack(Material.ORANGE_WOOL);
        ItemMeta levelThreeMeta = levelThree.getItemMeta();
        levelThreeMeta.setDisplayName("Level 3 - " + (teamLevel >= 3 ? ChatColor.GREEN + "Unlocked" : ChatColor.RED + "Locked"));
        levelThree.setItemMeta(levelThreeMeta);
        pane.addItem(new GuiItem(levelThree, event -> {
            // Logic
        }), 7, 2);










        // BEACON LEVEL
        ItemStack level = new ItemStack(Material.BEACON);
        ItemMeta levelMeta = level.getItemMeta();
        levelMeta.setDisplayName(team.getLevel() + " - " + team.getTeamName() + " Chunk");
        level.setItemMeta(levelMeta);
        pane.addItem(new GuiItem(level, event -> {
            // Logic
        }), 4, 0);

        // FILLER BLUE
        OutlinePane background = new OutlinePane(0, 0, 9, 5, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLUE_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        // FILLER CIRCLE
        paneel.bindItem('1', new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        paneel.bindItem('2', new GuiItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE)));
        paneel.bindItem('3', new GuiItem(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE)));

        gui.addPane(pane);
        gui.addPane(paneel);
        gui.addPane(background);
        gui.show(player);
    }


    // FILLER LIGHT BLUE
    Pattern patroon = new Pattern(
            "111222333",
            "111222333",
            "111222333",
            "111222333",
            "000000000"
    );


}

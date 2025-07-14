package com.jasper.chunkBlock.gui.chunk;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.jasper.chunkBlock.commands.team.Team;
import com.jasper.chunkBlock.gui.chunk.settings.SettingType;
import com.jasper.chunkBlock.commands.border.Border;
import com.jasper.chunkBlock.ChunkBlock;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


public class ChunkSettingsGUI {

    private final Player player;
    private final Team team;
    private final Border border;

    public ChunkSettingsGUI(Player player, Team team) {
        this.player = player;
        this.team = team;

        this.border = ChunkBlock.getInstance().getBorderStorage().getBorder(team);
        if (this.border == null) {
            throw new IllegalStateException("Border not found: " + team.getTeamName());
        }
    }

    public void open() {
        ChestGui gui = new ChestGui(4, "Chunk - Settings");

        PaginatedPane pages = new PaginatedPane(0, 0, 9, 3);
        OutlinePane settingsPane = new OutlinePane(0, 0, 9, 3);

        RegionManager manager = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(BukkitAdapter.adapt(border.getCenter().getWorld()));

        if (manager == null) return;

        String regionId = "team_" + border.getOwner().getTeamName(); // <-- HIER de juiste ID bepalen
        ProtectedRegion region = manager.getRegion(regionId);

        if (region != null) {
            for (SettingType setting : SettingType.values()) {
                StateFlag flag = setting.getFlag();
                if (flag == null) continue;

                StateFlag.State state = region.getFlag(flag);
                boolean enabled = (state == StateFlag.State.ALLOW);

                GuiItem guiItem = getGuiItem(setting, enabled);

                GuiItem clickableItem = new GuiItem(guiItem.getItem(), event -> {
                    event.setCancelled(true);

                    boolean newEnabled = !enabled;
                    region.setFlag(flag, newEnabled ? StateFlag.State.ALLOW : StateFlag.State.DENY);

                    manager.addRegion(region);
                    gui.update();
                });

                settingsPane.addItem(clickableItem);
            }

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
            ChunkMainGUI ch = new ChunkMainGUI(player, team);
            ch.open();
        }), 4, 0);

        gui.addPane(navigation);
        gui.show(player);
    }

    private @NotNull GuiItem getGuiItem(SettingType setting, boolean state) {
        ItemStack item = new ItemStack(setting.getIcon());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e" + setting.getDisplayName() + ": " + (state ? "§aTRUE" : "§cFALSE"));
        item.setItemMeta(meta);

        // Wrap in GuiItem en voeg klikactie toe
        GuiItem guiItem = new GuiItem(item, event -> {
            event.setCancelled(true); // voorkom dat item wordt verplaatst

            // Toggle setting
            border.toggleSetting(setting);

            // Herlaad GUI
            new ChunkSettingsGUI(player, team).open();
        });
        return guiItem;
    }
}

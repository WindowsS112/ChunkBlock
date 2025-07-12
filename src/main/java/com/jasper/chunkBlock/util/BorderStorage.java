package com.jasper.chunkBlock.util;

import com.jasper.chunkBlock.ChunkBlock;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BorderStorage {

    private final File borderData;
    private final ChunkBlock plugin;
    private final YamlConfiguration borderstoragefile;
    private final TeamStorage teamStorage;

    // Optioneel: cache alle borders in‑memory
    private final Map<String, Border> bordersMap = new HashMap<>();

    public BorderStorage(File borderData,
                         ChunkBlock plugin,
                         YamlConfiguration borderstoragefile,
                         TeamStorage teamStorage) {
        this.borderData = borderData;
        this.plugin = plugin;
        this.borderstoragefile = borderstoragefile;
        this.teamStorage = teamStorage;
    }

    /**
     * Sla één border op onder Borders.<teamName>
     */
    public void saveBorder(Team team, Border border) {
        String path = "Borders." + team.getTeamName();

        borderstoragefile.set(path + ".world", border.getWorldName());
        borderstoragefile.set(path + ".center.x", border.getX());
        borderstoragefile.set(path + ".center.z", border.getZ());
        borderstoragefile.set(path + ".radius", border.getRadius());

        saveConfig();

        // Update cache
        bordersMap.put(team.getTeamName(), border);
    }

    /**
     * Laad één border uit file en pas ‘m toe voor alle teamleden.
     */
    public boolean loadBorder(Team team) {
        String teamName = team.getTeamName();
        String path = "Borders." + teamName;

        if (!borderstoragefile.contains(path)) {
            plugin.getLogger().info("No border found for team " + teamName);
            return false;
        }

        // Uit file lezen
        String worldName = borderstoragefile.getString(path + ".world");
        int locx       = borderstoragefile.getInt(path + ".center.x");
        int locz       = borderstoragefile.getInt(path + ".center.z");
        double radius  = borderstoragefile.getDouble(path + ".radius");

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            plugin.getLogger().warning("World not found: " + worldName);
            return false;
        }

        // Maak en cache de Border
        Border border = new Border(locx, locz, worldName, team, radius);
        bordersMap.put(teamName, border);

        // Pas per‑player WorldBorder toe
        WorldBorder wb = Bukkit.createWorldBorder();
        wb.setCenter(locx + 0.5, locz + 0.5);
        wb.setSize(radius);
        wb.setWarningDistance(5);
        wb.setWarningTime(10);

        for (UUID uuid : team.getMembersOfTeam()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.setWorldBorder(wb);
                player.sendMessage("§aBorder applied for team " + teamName);
            }
        }
        return true;
    }

    /**
     * Laad en cache alle borders bij startup. Roep dit aan in onEnable() ná teamStorage.loadTeams().
     */
    public void loadAllBorders() {
        if (!borderstoragefile.contains("Borders")) return;

        ConfigurationSection section = borderstoragefile.getConfigurationSection("Borders");
        for (String teamName : section.getKeys(false)) {
            // Haal team op
            Team team = teamStorage.getTeam(teamName).orElse(null);
            if (team == null) continue;

            // Lees coords & radius
            String worldName = section.getString(teamName + ".world");
            int x         = section.getInt(teamName + ".center.x");
            int z         = section.getInt(teamName + ".center.z");
            double radius = section.getDouble(teamName + ".radius");

            // Maak en cache
            Border border = new Border(x, z, worldName, team, radius);
            bordersMap.put(teamName, border);
        }
    }

    /**
     * Verwijder de border voor dit team, zowel uit cache als uit file.
     */
    public void removeBorder(Team team) {
        String teamName = team.getTeamName();
        String path = "Borders." + teamName;

        borderstoragefile.set(path, null);
        saveConfig();

        bordersMap.remove(teamName);

        // Reset worldborder voor alle leden
        for (UUID uuid : team.getMembersOfTeam()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.setWorldBorder(null);
            }
        }
    }

    private void saveConfig() {
        try {
            borderstoragefile.save(borderData);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save borders.yml!");
            e.printStackTrace();
        }
    }


    public Border getBorder(Team team) {
        return bordersMap.get(team.getTeamName());
    }

}

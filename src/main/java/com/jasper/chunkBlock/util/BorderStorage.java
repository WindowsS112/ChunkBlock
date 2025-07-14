package com.jasper.chunkBlock.util;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.border.Border;
import com.jasper.chunkBlock.commands.team.Team;
import com.jasper.chunkBlock.gui.chunk.settings.SettingType;
import org.bukkit.*;
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

    private final Map<Team, Border> bordersMap = new HashMap<>();

    public BorderStorage(File borderData,
                         ChunkBlock plugin,
                         YamlConfiguration borderstoragefile,
                         TeamStorage teamStorage) {
        this.borderData = borderData;
        this.plugin = plugin;
        this.borderstoragefile = borderstoragefile;
        this.teamStorage = teamStorage;
    }

    public void createBorder(int x, int z, String world, Team team, double radius, Location location) {
        Border border = new Border(x, z, world, team, radius,location);
        bordersMap.put(team, border); // <- DIT MOET JE TOEVOEGEN
        saveBorder(team);
        border.applyToTeam();
        border.setHome(location);
        RegionSynchronizer.syncRegionFromBorder(team, border);
    }

    /**
     * Sla één border op onder Borders.<teamName>
     */
    public void saveBorder(Team team) {
        Border border = bordersMap.get(team);
        if (border == null) return;

        String path = "Borders." + team.getTeamName();
        String settingsPath = path + ".Settings";

        borderstoragefile.set(path + ".world", border.getWorldName());
        borderstoragefile.set(path + ".center.x", border.getX());
        borderstoragefile.set(path + ".center.z", border.getZ());
        borderstoragefile.set(path + ".radius", border.getRadius());
        borderstoragefile.set(path + ".home.x", border.getDefaultHome().getX());
        borderstoragefile.set(path + ".home.y", border.getDefaultHome().getY());
        borderstoragefile.set(path + ".home.z", border.getDefaultHome().getZ());
        borderstoragefile.set(path + ".home.yaw", border.getDefaultHome().getYaw());
        borderstoragefile.set(path + ".home.pitch", border.getDefaultHome().getPitch());

        for (SettingType type : SettingType.values()) {
            boolean enabled = border.isSettingEnabled(type);
            borderstoragefile.set(settingsPath + "." + type.getDisplayName(), border.isSettingEnabled(type));
        }
        saveConfig();

        // Update cache
        bordersMap.put(team, border);
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

        // Read home location
        double homeX = borderstoragefile.getDouble(path + ".home.x");
        double homeY = borderstoragefile.getDouble(path + ".home.y");
        double homeZ = borderstoragefile.getDouble(path + ".home.z");
        float homeYaw = (float) borderstoragefile.getDouble(path + ".home.yaw");
        float homePitch = (float) borderstoragefile.getDouble(path + ".home.pitch");
        World world = Bukkit.getWorld(worldName);
        Location defaultHome = new Location(world, homeX, homeY, homeZ, homeYaw, homePitch);

        if (world == null) {
            plugin.getLogger().warning("World not found: " + worldName);
            return false;
        }

        // Maak en cache de Border


        Border border = new Border(locx, locz, worldName, team, radius, defaultHome);
        bordersMap.put(team, border);

        ConfigurationSection settingsSection = borderstoragefile.getConfigurationSection(path + ".Settings");
        if (settingsSection != null) {
            for (SettingType type : SettingType.values()) {
                boolean enabled = settingsSection.getBoolean(type.getDisplayName(), false);
                border.setSetting(type, enabled);
            }
        }

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
            }
        }
        RegionSynchronizer.syncRegionFromBorder(team, border);

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

            // Home
            double homeX = section.getDouble(teamName + ".home.x");
            double homeY = section.getDouble(teamName + ".home.y");
            double homeZ = section.getDouble(teamName + ".home.z");
            float homeYaw = (float) section.getDouble(teamName + ".home.yaw");
            float homePitch = (float) section.getDouble(teamName + ".home.pitch");
            World world = Bukkit.getWorld(worldName);
            Location defaultHome = new Location(world, homeX, homeY, homeZ, homeYaw, homePitch);
            // Maak en cache
            Border border = new Border(x, z, worldName, team, radius,defaultHome);
            bordersMap.put(team, border);
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

        bordersMap.remove(team);

        // Reset worldborder voor alle leden
        for (UUID uuid : team.getMembersOfTeam()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.setWorldBorder(null);
            }
        }
    }

    public void removeBorder(Player player) {
        UUID uuid = player.getUniqueId();
        if (player != null && player.isOnline()) {
            player.setWorldBorder(null);
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
        return bordersMap.get(team);
    }

    public Location getHomeLocation(Team team) {
        return getBorder(team).getDefaultHome();
    }

}

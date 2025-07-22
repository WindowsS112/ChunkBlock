package com.jasper.chunkBlock.util;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TeamStorage {

    private final File teamsData;
    private final ChunkBlock plugin;
    private final YamlConfiguration teamsstoragefile;
    private Map<String, Team> teams = new HashMap<>();

    public TeamStorage(File teamsData, ChunkBlock plugin, YamlConfiguration teamsstoragefile) {
        this.teamsData = teamsData;
        this.plugin = plugin;
        this.teamsstoragefile = teamsstoragefile;
    }

    public void loadTeams() {
        ConfigurationSection teamsSection = teamsstoragefile.getConfigurationSection("Teams");

        if (teamsSection == null) {
            Bukkit.getLogger().warning("Teams section is null!");
            return;
        }

        for (String teamName : teamsSection.getKeys(false)) {
            ConfigurationSection teamSection = teamsSection.getConfigurationSection(teamName);

            String ownerString = teamSection.getString("owner");
            if (ownerString == null) {
                Bukkit.getLogger().warning("Owner UUID string is null for team " + teamName);
                continue;
            }

            int level = teamSection.getInt("level");

            UUID ownerUUID;
            try {
                ownerUUID = UUID.fromString(ownerString);
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("Owner UUID string is niet geldig voor team " + teamName + ": " + ownerString);
                continue;
            }

            List<String> memberStrings = teamSection.getStringList("members");
            Set<UUID> members = new HashSet<>();
            for (String memberStr : memberStrings) {
                try {
                    UUID memberUUID = UUID.fromString(memberStr);
                    members.add(memberUUID);
                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().warning("Ongeldige member UUID in team " + teamName + ": " + memberStr);
                }
            }

            Team team = new Team(ownerUUID.toString(), ownerUUID, teamName,members,level);
            for (UUID memberUUID : members) {
                if (!memberUUID.equals(ownerUUID)) {
                    team.joinTeam(memberUUID, this);
                }
            }

            // Add to storage
            addTeam(team);
            saveConfig();
        }

    }

    public void addTeam(Team team) {
        teams.put(team.getTeamName(), team);

        List<String> memberUUIDStrings = team.getMembersOfTeam().stream()
                .map(UUID::toString)
                .collect(Collectors.toList());

        teamsstoragefile.set("Teams." + team.getTeamName() + ".level", team.getLevel());
        teamsstoragefile.set("Teams." + team.getTeamName() + ".owner", team.getOwner().toString());
        teamsstoragefile.set("Teams." + team.getTeamName() + ".members", memberUUIDStrings);

        try {
            teamsstoragefile.save(teamsData);
        } catch (IOException e) {
            Bukkit.getLogger().info("Couldn't save teams file.");
        }
    }

    public boolean isPlayerInAnyTeam(UUID playerUUID) {
        for (Team team : teams.values()) {
            for (UUID member : team.getMembersOfTeam()) {
                if (member.equals(playerUUID)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeTeam(Team team, Player player) {
        teams.remove(team.getTeamName());
        teamsstoragefile.set("Teams." + team.getTeamName(), null);
        saveConfig();
        loadTeams();

        player.sendMessage(ChatColor.GREEN + "Team " + team.getTeamName() + " succesfully deleted");
    }

    public void addMemberToTeam(Team team, UUID playerUUID) {
        team.joinTeam(playerUUID,this);
        List<String> memberUUIDs = team.getMembersAsStringList();
        teamsstoragefile.set("Teams." + team.getTeamName() + ".members", memberUUIDs);
        saveConfig();

        team.onJoin(Bukkit.getPlayer(playerUUID));
    }

    public void removeMemberFromTeam(String teamName, UUID playerUUID) {
        Team team = teams.get(teamName.toLowerCase());
        if (team == null) {
            Bukkit.getLogger().info("Team not found: " + teamName);
            return;
        }

        team.leaveTeam(playerUUID,this);
        List<String> memberUUIDs = team.getMembersAsStringList();
        teamsstoragefile.set("Teams." + teamName + ".members", memberUUIDs);
        saveConfig();
    }

    public void upgrade(Team team, int level) {
        team.upgrade(this, level);
        teamsstoragefile.set("Teams." + team.getTeamName() + ".level", level);
        for (UUID p : team.getMembersOfTeam()) {
            Player player = Bukkit.getPlayer(p);
            MessageUtils.sendSuccess(player, "&7" + team.getTeamName() + ",&f has now upgraded to level: " + team.getLevel());
        }
        saveConfig();
    }

    public void saveConfig() {
        try {
            teamsstoragefile.save(teamsData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkTeamExist(Team team) {
        return teams.containsValue(team);
    }
    public Map<String, Team> getTeams() {
        return teams;
    }
    public Team getTeamByName(String name) {
        return teams.get(name);
    }
    public Team getTeamFromPlayer(UUID playerUUID) {
        for (Team team : teams.values()) { // teams is je Map<String, Team>
            if (team.getMembersOfTeam().contains(playerUUID)) {
                return team;
            }
        }
        return null;
    }

    /**
     * Haal een team op (case‑insensitive) uit de in‑memory map.
     * @param name De teamnaam
     * @return Optional met Team of empty als hij niet bestaat
     */
    public Optional<Team> getTeam(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(teams.get(name.toLowerCase()));
    }


    public JavaPlugin getPlugin() {
        return plugin;
    }


}

package com.jasper.chunkBlock.util;

import com.jasper.chunkBlock.ChunkBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
        if (teamsstoragefile.contains("Teams")) {
            ConfigurationSection teamsSection = teamsstoragefile.getConfigurationSection("Teams");
            if (teamsSection == null) {
                Bukkit.getLogger().warning("Teams section is null!");
                return;
            }

            for (String teamName : teamsSection.getKeys(false)) {
                ConfigurationSection teamSection = teamsSection.getConfigurationSection(teamName);
                if (teamSection == null) {
                    Bukkit.getLogger().warning("Team section for " + teamName + " is null!");
                    continue;
                }

                String ownerString = teamSection.getString("owner");
                if (ownerString == null) {
                    Bukkit.getLogger().warning("Owner UUID string is null for team " + teamName);
                    continue;
                }

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

                // maak team object aan, of roep setter aan, afhankelijk van jouw Team-class
                Team team = new Team(ownerUUID.toString(), ownerUUID, teamName);
                for (UUID memberUUID : members) {
                    team.joinTeam(memberUUID,this);
                }

                // voeg toe aan opslag
                addTeam(team);
                saveConfig();
                Bukkit.getLogger().info("Loaded team: " + team.getTeamName() + " with members: " + team.getMembersOfTeam());

            }
        }
    }

    public void addTeam(Team team) {
        teams.put(team.getTeamName(), team);

        List<String> memberUUIDStrings = team.getMembersOfTeam().stream()
                .map(UUID::toString)
                .collect(Collectors.toList());

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
        player.sendMessage(ChatColor.GREEN + "Team " + team.getTeamName() + " succesfully deleted");
    }

    public void addMemberToTeam(String teamName, UUID playerUUID) {
        Team team = teams.get(teamName.toLowerCase());
        if (team == null) {
            System.out.println("Team not found: " + teamName);
            return;
        }

        team.joinTeam(playerUUID,this);
        List<String> memberUUIDs = team.getMembersAsStringList();
        teamsstoragefile.set("Teams." + teamName + ".members", memberUUIDs);
        saveConfig();
    }

    public void removeMemberFromTeam(String teamName, UUID playerUUID) {
        Team team = teams.get(teamName.toLowerCase());
        if (team == null) {
            System.out.println("Team not found: " + teamName);
            return;
        }

        team.leaveTeam(playerUUID,this);
        List<String> memberUUIDs = team.getMembersAsStringList();
        teamsstoragefile.set("Teams." + teamName + ".members", memberUUIDs);
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
        return teams.containsKey(team.getTeamName());
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




//    public Team getTeamByPlayer(UUID playerId) { ... }

}

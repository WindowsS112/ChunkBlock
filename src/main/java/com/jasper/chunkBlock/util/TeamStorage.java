package com.jasper.chunkBlock.util;

import com.jasper.chunkBlock.ChunkBlock;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

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

    public void addTeam(Team team) {
        List<String> memberUUIDStrings = team.getMembersOfTeam().stream()
                .map(UUID::toString)
                .collect(Collectors.toList());

        teamsstoragefile.set("Teams." + team.getTeamName() + ".owner", team.getOwner().toString());
        teamsstoragefile.set("Teams." + team.getTeamName() + ".members", memberUUIDStrings);

        try {
            teamsstoragefile.save(teamsData);
        } catch (IOException e) {
            Bukkit.getLogger().info("Couldn't save borderData file.");
            return;
        }
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
            }
        }
    }

    public boolean isPlayerInAnyTeam(UUID playerUUID) {
        Bukkit.getLogger().info("Checking if player is in any team: " + playerUUID);

        for (Team team : getTeams().values()) {
            Bukkit.getLogger().info("Checking team: " + team.getTeamName());
            Bukkit.getLogger().info("Team members: " + team.getMembersOfTeam());

            if (team.getMembersOfTeam().contains(playerUUID)) {
                Bukkit.getLogger().info("Player found in team: " + team.getTeamName());
                return true;
            }
        }

        Bukkit.getLogger().info("Player not found in any team.");
        return false;
    }


    public Map<String, Team> getTeams() {
        return teams;
    }

//
//    public Team getTeamByPlayer(UUID playerId) { ... }
//
//    public void removeTeam(String id) { ... }
//
//    public void saveAll() { ... }
//
//    public void loadAll() { ... }

}

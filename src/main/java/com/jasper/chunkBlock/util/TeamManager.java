package com.jasper.chunkBlock.util;

import com.jasper.chunkBlock.commands.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    private final TeamStorage teamStorage;
    private final BorderStorage borderStorage;
    Set<UUID> members = new HashSet<>();


    public TeamManager(TeamStorage teamStorage, BorderStorage borderStorage) {
        this.teamStorage = teamStorage;
        this.borderStorage = borderStorage;
    }

    public Team createTeam(String name, Player owner) {
        // 1. Maak het Team-object en sla het op
        Team team = new Team(name, owner.getUniqueId(), name, members);
        teamStorage.addTeam(team); // laadt in geheugen én schrijft weg in teams.yml

        // 2. Voeg de owner direct toe als member
        team.joinTeam(owner.getUniqueId(), teamStorage);

        // 3. Bepaal de chunk-coördinaten en wereld
        int x = owner.getLocation().getBlockX();
        int z = owner.getLocation().getBlockZ();
        String world = owner.getWorld().getName();
        double radius = teamStorage.getPlugin().getConfig().getDouble("defaultBorderRadius", 50.0);

        Location defaultHome = owner.getLocation();
        borderStorage.createBorder(x,z,world,team,radius,defaultHome);
        return team;
    }


    /**
     * Verwijder een team, inclusief al zijn borders/chunk‑claims.
     * Alleen de owner mag dit doen.
     */
    public boolean deleteTeam(String name, Player actor) {
        Team team = teamStorage.getTeams().get(name.toLowerCase());
        if (team == null) return false;

        if (!team.getOwner().equals(actor.getUniqueId())) {
            // actor is niet eigenaar
            return false;
        }

        // Verwijder team uit opslag
        teamStorage.getTeams().remove(name.toLowerCase());
        teamStorage.saveConfig();  // implementeer in TeamStorage

        return true;
    }

    /**
     * Voeg een member toe aan een bestaand team.
     */
    public boolean addMember(String teamName, UUID newMember) {
        Team team = teamStorage.getTeams().get(teamName.toLowerCase());
        if (team == null) return false;

        team.joinTeam(newMember, teamStorage);
        return true;
    }

    /**
     * Verwijder een member uit een team.
     */
    public boolean removeMember(String teamName, UUID member) {
        Team team = teamStorage.getTeams().get(teamName.toLowerCase());

        UUID uuid = UUID.fromString(member.toString());
        Player player = Bukkit.getPlayer(uuid);

        if (team == null) return false;

        // Zorg dat je niet de owner verwijdert
        if (team.getOwner().equals(member)) {
            return false;
        } else if (player != null) {
            borderStorage.removeBorder(player);
        }

        team.leaveTeam(member, teamStorage);  // implementeer in Team
        return true;
    }

    /**
     * Haal een team op op naam.
     */
    public Optional<Team> getTeam(String name) {
        return Optional.ofNullable(teamStorage.getTeams().get(name.toLowerCase()));
    }

    /**
     * Zoek het team waar deze speler in zit (owner of member).
     */
    public Optional<Team> getTeamOfPlayer(UUID playerUUID) {
        return teamStorage.getTeams().values().stream()
                .filter(team -> team.getOwner().equals(playerUUID)
                        || team.getMembersOfTeam().contains(playerUUID))
                .findFirst();
    }

    /**
     * Retourneer alle teams.
     */
    public Collection<Team> listTeams() {
        return Collections.unmodifiableCollection(teamStorage.getTeams().values());
    }

}

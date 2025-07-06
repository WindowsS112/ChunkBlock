package com.jasper.chunkBlock.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Team {

    private final String id;
    private String teamName;
    private Set<UUID> members = new HashSet<>();
    private UUID owner;

    public Team(String id, UUID owner, String teamName) {
        this.id = id;
        this.owner = owner;
        this.teamName = teamName;
        this.members.add(owner);
    }

    public void joinTeam(UUID uuid, TeamStorage teamStorage) {
        Player player = Bukkit.getPlayer(uuid);

        if (!teamStorage.isPlayerInAnyTeam(uuid)) {
            members.add(uuid);
            if (player != null) {
                player.sendMessage(ChatColor.GREEN + "You have joined the team " + getTeamName() + "!");
                members.add(uuid);
                teamStorage.saveConfig();

                for (UUID memberUuid : members) {
                    Player member = Bukkit.getPlayer(memberUuid);
                    if (member != null) {
                        Bukkit.getLogger().info("Team member: " + member.getName());
                    } else {
                        Bukkit.getLogger().info("Team member UUID: " + memberUuid);
                    }
                }

            }
        } else {
            if (player != null) {
                player.sendMessage(ChatColor.RED + "You are already in a chunkparty! " + getTeamName());
            }
        }
    }

    public void leaveTeam(UUID uuid, TeamStorage teamStorage) {
        Player player = Bukkit.getPlayer(uuid);

        if (teamStorage.isPlayerInAnyTeam(uuid)) {
            members.remove(uuid);
            player.sendMessage(ChatColor.GREEN + "Left: " + getTeamName() + "!");

        } else {
            if (player != null) {
                player.sendMessage(ChatColor.RED + "You don't have a team!");
            }
        }
    }

    public List<String> getMembersAsStringList() {
        return members.stream()
                .map(UUID::toString)
                .collect(Collectors.toList());
    }


    public String getTeamName() { return teamName; }

    public UUID getOwner() {
        return this.owner;
    }

    public void setMembers(Set<UUID> members) {
        this.members = members;
    }

    public Set<UUID> getMembersOfTeam() {
        return members;
    }

}

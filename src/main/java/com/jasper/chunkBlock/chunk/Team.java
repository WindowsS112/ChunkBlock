package com.jasper.chunkBlock.chunk;

import com.jasper.chunkBlock.util.MessageUtils;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Team {

    private String teamName;
    private Set<UUID> members = new HashSet<>();
    private UUID owner;
    private int level;

    public Team(String id, UUID owner, String teamName, Set<UUID> members, int level) {
        this.owner = owner;
        this.teamName = teamName;
        this.members = new HashSet<>(members);
        this.members.add(owner);
        this.level = level;
    }

    public void joinTeam(UUID uuid, TeamStorage teamStorage) {
        Player player = Bukkit.getPlayer(uuid);

        if (!teamStorage.isPlayerInAnyTeam(uuid) && player != null) {
            MessageUtils.sendSuccess(player, "You have joined the team " + getTeamName() + "!");
            members.add(uuid);
            teamStorage.saveConfig();
        } else {
            if (player != null && player.getUniqueId() != getOwner()) {
                MessageUtils.sendError(player, "You are already in a chunkparty! " + getTeamName());
            }
        }
    }

    public void leaveTeam(UUID uuid, TeamStorage teamStorage) {
        Player player = Bukkit.getPlayer(uuid);

        if (teamStorage.isPlayerInAnyTeam(uuid)) {
            members.remove(uuid);
            teamStorage.saveConfig();
            MessageUtils.sendSuccess(player, "You have left: " + getTeamName() + "!");
        } else {
            if (player != null) {
                MessageUtils.sendError(player,  "You don't have a team!");
            }
        }
    }

    public List<String> getMembersAsStringList() {
        return members.stream()
                .map(UUID::toString)
                .collect(Collectors.toList());
    }

    public void onJoin(Player player) {
        if (player == null) return;
        for (UUID uuid : members) {
            Player member = Bukkit.getPlayer(uuid);
            if (member != null && !member.getUniqueId().equals(player.getUniqueId())) {
                member.sendMessage(player.getName() + " just joined " + teamName);
                MessageUtils.sendInfo(member, "&f" + player.getName() +" &7Just joined party: " + teamName );
            }
        }
    }

    public void upgrade(TeamStorage teamStorage, int upgradedLevel) {
        level = upgradedLevel;
        teamStorage.saveConfig();
    }

    public int getLevel() {
        return level;
    }
    public String getTeamName() { return teamName; }
    public UUID getOwner() { return this.owner; }
    public Set<UUID> getMembersOfTeam() { return members; }

}

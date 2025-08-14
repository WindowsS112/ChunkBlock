package com.jasper.chunkBlock.team;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.chunk.ChunkStorage;
import com.jasper.chunkBlock.chunk.ClaimedChunk;
import com.jasper.chunkBlock.database.Database;
import com.jasper.chunkBlock.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.security.SecureRandom;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class TeamService {

    private final Database database;
    private final ChunkStorage chunkStorage;
    private Map<String, Team> teamsById = new HashMap<>();
    private Map<UUID, Team> teamsByPlayer = new HashMap<>();
    FileConfiguration config = ChunkBlock.getInstance().getConfig();


    public TeamService(Database database, ChunkStorage chunkStorage) {
        this.database = database;
        this.chunkStorage = chunkStorage;
    }

    public Team createTeam(String name, Player player) {
        String teamId = IdGenerator.generateId();
        String chunkId = IdGenerator.generateId();
        int level = 1;
        Set<UUID> members = new HashSet<>();
        members.add(player.getUniqueId());
        Team team = new Team(teamId, player.getUniqueId(),name);
        teamsById.put(teamId, team);
        teamsByPlayer.put(player.getUniqueId(), team);
        World world = player.getWorld();
        ClaimedChunk chunk = new ClaimedChunk(world.getName(), config.getInt("defaultChunkSize"), (int) player.getX(), (int) player.getZ(),teamId, chunkId);
        chunk.setHome(player.getLocation());

        chunk.createBorder(player);
        database.addTeam(team);
        database.addChunk(chunk);
        addMember(teamId,player);

        return team;
    }

    public void loadAllTeams() {
        try (PreparedStatement stmt = database.getConnectionF().prepareStatement("SELECT * FROM teams")) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String teamId = rs.getString("teamid");
                String name = rs.getString("teamname");
                UUID ownerUuid = UUID.fromString(rs.getString("owner"));

                Team team = new Team(teamId, ownerUuid, name);
                addTeam(team);

                // Chunk direct ophalen via een DB-query
                ClaimedChunk claimedChunk = loadChunkByTeamId(teamId); // Zorg dat je deze methode hebt
                if (claimedChunk != null) {
                    chunkStorage.addClaimedChunk(teamId, claimedChunk);
                } else {
                    Bukkit.getLogger().warning("[ChunkBlock] Geen chunk gevonden voor team " + teamId);
                }

                try (PreparedStatement memberStmt = database.getConnectionF().prepareStatement(
                        "SELECT member_uuid FROM team_members WHERE teamid = ?")) {
                    memberStmt.setString(1, teamId);
                    ResultSet memberRs = memberStmt.executeQuery();

                    while (memberRs.next()) {
                        UUID memberId = UUID.fromString(memberRs.getString("member_uuid"));
                        team.addMember(memberId);
                        this.teamsByPlayer.put(memberId, team);
                    }
                }

                this.teamsById.put(teamId, team);
            }

            Bukkit.getLogger().info("[ChunkBlock] Alle teams en leden geladen uit database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ClaimedChunk loadChunkByTeamId(String teamId) {
        String sql = "SELECT * FROM chunks WHERE teamid = ?";
        try (PreparedStatement stmt = database.getConnectionF().prepareStatement(sql)) {
            stmt.setString(1, teamId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ClaimedChunk(
                        rs.getString("chunkid"),
                        rs.getString("teamid"),
                        (rs.getString("owner_uuid")),
                        rs.getInt("level"),
                        rs.getDouble("levelxp"),
                        rs.getString("world"),
                        rs.getInt("center_x"),
                        rs.getInt("center_z"),
                        rs.getInt("border_radius")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public ClaimedChunk getClaimedChunkByTeamId(String teamId) {
        try (PreparedStatement stmt = database.getConnectionF().prepareStatement("SELECT * FROM chunks WHERE teamid = ?")) {
            stmt.setString(1, teamId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String chunkid = rs.getString("chunkid");
                rs.getString("owner_uuid");
                int level = rs.getInt("level");
                double levelxp = rs.getDouble("levelxp");
                String world = rs.getString("world");
                int x = rs.getInt("center_x");
                int z = rs.getInt("center_z");
                int radius = rs.getInt("border_radius");

                return new ClaimedChunk(world,radius,x,z,teamId,chunkid); // constructor moet hiermee matchen
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void applyBorders(Team team) {
        ClaimedChunk claimedChunk = chunkStorage.getChunkByTeamId(team.getTeamId());
        if (claimedChunk == null) return;

        for (UUID uuid : team.getMembersOfTeam()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                applyBorderForPlayer(player,claimedChunk);
            }
        }
    }

    public boolean deleteTeam(UUID uuid) {
        database.deleteTeam(getTeamByPlayer(uuid).getTeamId());
        getChunkByPlayer(uuid).removeBorder();
        return true;
    }

    public void removeMemberFromTeam(Player player) {
        Team team = getTeamByPlayer(player.getUniqueId());
        ClaimedChunk claimedChunk = getChunkByPlayer(player.getUniqueId());
        if (team == null) {
            MessageUtils.sendError(player, "&f" + team.getTeamName() + "&7 does not exist.");
            return;
        }
        database.removeMember(player);
        applyBorderForPlayer(player,claimedChunk);
    }

    public ClaimedChunk getChunkByPlayer(UUID playerUuid) {
        ClaimedChunk claimedChunk = chunkStorage.getChunkByTeamId(getTeamByPlayer(playerUuid).getTeamId());
        return claimedChunk;
    }

    public ClaimedChunk getChunkByTeam(Team team) {
        ClaimedChunk claimedChunk = chunkStorage.getChunkByTeamId(team.getTeamId());
        return claimedChunk;
    }


    public void applyBorderForPlayer(Player player, ClaimedChunk chunk) {
        World world = Bukkit.getWorld(chunk.getWorld());
        if (world == null) {
            player.sendMessage("§cDe wereld '" + chunk.getWorld() + "' bestaat niet!");
            return;
        }

        int centerX = chunk.getX();
        int centerZ = chunk.getZ();
        int centerY = world.getHighestBlockYAt(centerX, centerZ);

        Location center = new Location(world, centerX, centerY, centerZ);

        player.sendMessage("§7[DEBUG] Border center op: " + centerX + ", " + centerZ + " in wereld '" + world.getName() + "'");
        player.sendMessage("§7[DEBUG] Spelerlocatie: " + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ());

        WorldBorder border = Bukkit.createWorldBorder();
        border.setCenter(center);

        int radius = chunk.getClaimRadius(); // bijv. 1
        int level = chunk.getLevel();        // bijv. 1
        double size = radius; // diameter in blokken

        player.sendMessage("§7[DEBUG] Border size (diameter): " + size);

        border.setSize(size);
        border.setWarningDistance(5);
        border.setDamageAmount(0.5);
        border.setDamageBuffer(1);

        player.setWorldBorder(border);

        player.sendMessage("§a[DEBUG] Border succesvol toegepast!");
    }


    public boolean addMember(String teamId, Player player) {
        Team team = getTeamById(teamId);
        if (team == null) return false;

        database.addMember(teamId,player);
        return true;
    }
//
//    /**
//     * Verwijder een member uit een team.
//     */
//    public boolean removeMember(String teamName, UUID member) {
//        Team team = teamName;
//
//        UUID uuid = UUID.fromString(member.toString());
//        Player player = Bukkit.getPlayer(uuid);
//
//        if (team == null) return false;
//
//        // Zorg dat je niet de owner verwijdert
//        if (team.getOwner().equals(member)) {
//            return false;
//        } else if (player != null) {
////            borderStorage.removeBorder(player);
//        }
//
//        team.leaveTeam(member, teamStorage);  // implementeer in Team
//        return true;
//    }
//
//    /**
//     * Zoek het team waar deze speler in zit (owner of member).
//     */
//    public Team getTeamFromPlayer(UUID playerUUID) {
//        for (Team team : teamStorage.getTeams().values()) { // teams is je Map<String, Team>
//            if (team.getMembersOfTeam().contains(playerUUID)) {
//                return team;
//            }
//        }
//        return null;
//    }

    public void addTeam(Team team) {
        teamsById.put(team.getTeamName(), team);

        List<String> memberUUIDStrings = team.getMembersOfTeam().stream()
                .map(UUID::toString)
                .collect(Collectors.toList());
    }

    public boolean isPlayerInAnyTeam(UUID playerUUID) {
        for (Team team : teamsByPlayer.values()) {
            for (UUID member : team.getMembersOfTeam()) {
                if (member.equals(playerUUID)) {
                    return true;
                }
            }
        }
        return false;
    }
//
//    public void removeTeam(Team team, Player player) {
//        teams.remove(team.getTeamName());
//        loadTeams();
//        player.sendMessage(ChatColor.GREEN + "Team " + team.getTeamName() + " succesfully deleted");
//    }
//
//    public void addMemberToTeam(Team team, UUID playerUUID) {
//        team.joinTeam(playerUUID,this);
//        List<String> memberUUIDs = team.getMembersAsStringList();
//
//        team.onJoin(Bukkit.getPlayer(playerUUID));
//    }
//
//
//    public void upgrade(Team team, int level) {
////        team.upgrade( level);
//        for (UUID p : team.getMembersOfTeam()) {
//            Player player = Bukkit.getPlayer(p);
//            MessageUtils.sendSuccess(player, "&7" + team.getTeamName() + ",&f has now upgraded to level: " + team.getLevel());
//        }
//    }
//
    public boolean checkTeamExist(Team team) {
        return teamsById.containsValue(team);
    }

    public Map<String, Team> getTeams() {
        return teamsById;
    }

    public Team getTeamByPlayer(UUID uuid) {
        return teamsByPlayer.get(uuid);
    }

    public Team getTeamById(String teamId) {
        return teamsById.get(teamId);
    }

    public class IdGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ID_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    public static String generateId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}


}

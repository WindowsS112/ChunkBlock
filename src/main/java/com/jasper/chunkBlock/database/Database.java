package com.jasper.chunkBlock.database;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.chunk.ClaimedChunk;
import com.jasper.chunkBlock.team.Team;
import com.jasper.chunkBlock.team.TeamService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

import static java.sql.DriverManager.getConnection;

public class Database {

    private final Connection connection;

    public Database(String path) throws SQLException {
        connection = getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
        CREATE TABLE IF NOT EXISTS teams (
            teamid TEXT PRIMARY KEY,
            owner TEXT,
            teamname TEXT NOT NULL
        );
    """);
            statement.execute("""
        CREATE TABLE IF NOT EXISTS team_members (
            teamid TEXT NOT NULL,
            member_uuid TEXT NOT NULL,
            PRIMARY KEY (teamid, member_uuid)
        );
    """);
            statement.execute("""
        CREATE TABLE IF NOT EXISTS chunks (
            chunkid TEXT PRIMARY KEY,
            teamid TEXT NOT NULL,
            owner_uuid TEXT,
            level INT,
            levelxp DOUBLE,
            world TEXT,
            center_x INTEGER,
            center_z INTEGER,
            border_radius INTEGER
        );
    """);
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void addChunk(ClaimedChunk claimedChunk) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chunks (chunkid, teamid, owner_uuid, level, levelxp, world,center_x, center_z, border_radius) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, claimedChunk.getChunkId());
            preparedStatement.setString(2, claimedChunk.getTeamId());
            preparedStatement.setString(3, claimedChunk.getOwner().toString());
            preparedStatement.setInt(4, claimedChunk.getLevel());
            preparedStatement.setDouble(5, claimedChunk.getXp());
            preparedStatement.setString(6, claimedChunk.getWorld());
            preparedStatement.setInt(7, claimedChunk.getX());
            preparedStatement.setInt(8, claimedChunk.getZ());
            preparedStatement.setInt(9, claimedChunk.getClaimRadius());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ClaimedChunk getChunkByOwner(UUID ownerUuid) {
        TeamService teamService = ChunkBlock.getInstance().getTeamService();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM chunks WHERE owner_uuid = ?")) {
            ps.setString(1, ownerUuid.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Team team = teamService.getTeamById(rs.getString("teamid"));
                return new ClaimedChunk(
                        rs.getString("chunkid"),
                        rs.getString("teamid"),
                        rs.getString("owner_uuid"),
                        rs.getInt("level"),
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

    public void addTeam(Team team) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO teams (teamid, owner, teamname ) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, team.getTeamId());
            preparedStatement.setString(2, team.getOwner().toString());
            preparedStatement.setString(3, team.getTeamName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteTeam(String teamId) {
        try {
            // Remove team members
            try (PreparedStatement psMembers = connection.prepareStatement(
                    "DELETE FROM team_members WHERE teamid = ?")) {
                psMembers.setString(1, teamId);
                psMembers.executeUpdate();
            }
            // Remove team
            try (PreparedStatement psTeam = connection.prepareStatement(
                    "DELETE FROM teams WHERE teamid = ?")) {
                psTeam.setString(1, teamId);
                int affected = psTeam.executeUpdate();
                return affected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addMember(String teamId, Player player) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO team_members (teamid, member_uuid) VALUES (?, ?)")) {
            preparedStatement.setString(1, teamId);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeMember(Player player) {
        try {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM team_members WHERE member_uuid = ?")) {
                ps.setString(1, player.getUniqueId().toString());
                int affected = ps.executeUpdate();
                return affected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean teamIdExists(String teamId) {
        String sql = "SELECT 1 FROM teams WHERE teamid = ?";
        try (Connection conn = connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teamId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnectionF() {
        return this.connection;
    }

    public void updateChunkLevel(ClaimedChunk chunk, int newLevel) {
        // Gebruik async als dit kan, hieronder voorbeeld sync
        String sql = "UPDATE chunks SET level = ? WHERE world = ? AND center_x = ? AND center_z = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newLevel);
            stmt.setString(2, chunk.getWorld()); // Zorg dat je deze getter hebt in ClaimedChunk
            stmt.setInt(3, chunk.getX());       // idem
            stmt.setInt(4, chunk.getZ());       // idem

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Log de fout netjes en/of geef feedback
        }
    }

    public void updateChunkLevelAsync(ClaimedChunk chunk, int newLevel) {
        Bukkit.getScheduler().runTaskAsynchronously(ChunkBlock.getInstance(), () -> {
            updateChunkLevel(chunk, newLevel);  // sync DB update, b.v. via JDBC
        });
    }



//    public void addChunk(ClaimedChunk claimedChunk) {
//        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chunks (chunkid, owner) VALUES (?, ?)")) {
//            preparedStatement.setString(1, );
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }




}

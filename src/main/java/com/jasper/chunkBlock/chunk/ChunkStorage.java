package com.jasper.chunkBlock.chunk;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.chunk.levels.LevelConfig;
import com.jasper.chunkBlock.database.Database;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ChunkStorage {

    private final Map<String, ClaimedChunk> chunksByTeamId = new HashMap<>();
    private Database database = ChunkBlock.getInstance().getDatabase();

    public ClaimedChunk loadChunk(String chunkId) {
        String sql = "SELECT chunkid, teamid, owner_uuid, level, levelxp, world, center_x, center_z, border_radius FROM chunks WHERE chunkid = ?";
        try (PreparedStatement stmt = database.getConnectionF().prepareStatement(sql)) {
            stmt.setString(1, chunkId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String teamId = rs.getString("teamid");
                UUID owner = UUID.fromString(rs.getString("owner_uuid"));
                int level = rs.getInt("level");
                double xp = rs.getDouble("levelxp");
                String world = rs.getString("world");
                int centerX = rs.getInt("center_x");
                int centerZ = rs.getInt("center_z");
                int borderRadius = rs.getInt("border_radius");

                return new ClaimedChunk(chunkId, teamId, owner.toString(), level, xp, world, centerX, centerZ, borderRadius);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




    public void addClaimedChunk(String teamId, ClaimedChunk claimedChunk) {
        chunksByTeamId.put(teamId,claimedChunk);
    }

    public ClaimedChunk getChunkByTeamId(String teamId) {
        return chunksByTeamId.get(teamId);
    }

}

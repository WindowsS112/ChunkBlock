package com.jasper.chunkBlock.database;

import com.jasper.chunkBlock.chunk.ClaimedChunk;

import java.sql.*;

public class ChunkDatabase {

    private final Connection connection;

    public ChunkDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS chunks (
                    chunkid TEXT PRIMARY KEY,
                    world, TEXT NOT NULL,
                    teamid TEXT NOT NULL,
                    owner TEXT NOT NULL,
                    levelxp INTEGER NOT NULL DEFAULT 0);
            """);
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void addChunk(ClaimedChunk claimedChunk) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chunks (chunkid, owner) VALUES (?, ?)")) {
            preparedStatement.setString(1, );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}

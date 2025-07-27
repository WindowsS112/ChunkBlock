package com.jasper.chunkBlock;

import com.jasper.chunkBlock.commands.CommandManager;
import com.jasper.chunkBlock.database.ChunkDatabase;
import com.jasper.chunkBlock.listeners.PlayerJoinListener;
import com.jasper.chunkBlock.chunk.Team;
import com.jasper.chunkBlock.util.TeamManager;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public final class ChunkBlock extends JavaPlugin {

    private BorderStorage borderStorage;
    private TeamManager teamManager;
    private TeamStorage teamStorage;
    private Chunk chunk;
    private static ChunkBlock instance;


    private File configFile = new File(getDataFolder(), "config.yml");
    private File borderData = new File(getDataFolder(), "borders.yml");
    private File teamsData = new File(getDataFolder(), "teams.yml");

    private YamlConfiguration teamsFile;
    YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(borderData);
    private double cSize;
    private Team team;
    private FileConfiguration config;
    private ChunkDatabase chunkDatabase;


    @Override
    public void onEnable() {
        instance = this;
        if (!configFile.exists()) saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(configFile);

        try {
            if (!borderData.exists()) borderData.createNewFile();
            if (!teamsData.exists()) teamsData.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        teamsFile = YamlConfiguration.loadConfiguration(teamsData);

        this.teamStorage = new TeamStorage(teamsData, this, teamsFile);
        this.teamStorage.loadTeams();

        this.borderStorage = new BorderStorage(borderData, this, modifyFile,teamStorage);

        TeamManager teamManager = new TeamManager(teamStorage,borderStorage);

        teamStorage.loadTeams();
        borderStorage.loadAllBorders();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(borderStorage, this,teamStorage), this);

        getCommand("c").setExecutor(new CommandManager(borderStorage, cSize, borderData,this, teamStorage, team,teamManager));

        try {
            if(!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            chunkDatabase = new ChunkDatabase(getDataFolder().getAbsolutePath() + "/ChunkBlock.db");
        } catch(SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to connect to the database! " + ex.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }


        Bukkit.getLogger().info("[ChunkBlock] -> Has Been Started!");
        Bukkit.getLogger().info("[ChunkBlock] -> Version: " + getDescription().getVersion());
        Bukkit.broadcastMessage("§a[ChunkBlock] Plugin is enabled!");
    }

    public void onDisable(){
        if (chunkDatabase != null) {
            try {
                chunkDatabase.closeConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        instance = null;
    }
    public static ChunkBlock getInstance() {
        return instance;
    }
    public BorderStorage getBorderStorage() {
        return this.borderStorage;
    }
    public FileConfiguration getCustomConfig() {
        return config;
    }
    public TeamStorage getTeamStorage() {
        return this.teamStorage;
    }
    public TeamManager getTeamManager() { return this.teamManager; }
}

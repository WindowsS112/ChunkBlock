package com.jasper.chunkBlock;

import com.jasper.chunkBlock.commands.CommandManager;
import com.jasper.chunkBlock.listeners.PlayerJoinListener;
import com.jasper.chunkBlock.util.BorderStorage;
import com.jasper.chunkBlock.util.Team;
import com.jasper.chunkBlock.util.TeamStorage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.io.IOException;

public final class ChunkBlock extends JavaPlugin {


    private File configFile = new File(getDataFolder(), "config.yml");
    private File borderData = new File(getDataFolder(), "borders.yml");
    private File teamsData = new File(getDataFolder(), "teams.yml");

    private BorderStorage borderStorage;

    private YamlConfiguration teamsFile;
    YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(borderData);

    private double cSize;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        if (!configFile.exists()) {
            saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        cSize = getConfig().getDouble("defaultChunkSize");

        if (!borderData.exists()) {
            try {
                borderData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!teamsData.exists()) {
            try {
                teamsData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        teamsFile = YamlConfiguration.loadConfiguration(teamsData);
        TeamStorage teamStorage = new TeamStorage(teamsData,this,teamsFile);
        teamStorage.loadTeams();

        this.borderStorage = new BorderStorage(borderData,this, modifyFile);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(borderStorage, this), this);
        CommandManager commandManager = new CommandManager(borderStorage,cSize,borderData,this,teamStorage);
        getCommand("c").setExecutor(commandManager);

        Bukkit.getLogger().info("[ChunkBlock] -> Has Been Started!");
    }

    public BorderStorage getBorderStorage() {
        return this.borderStorage;
    }

    public FileConfiguration getCustomConfig() {
        return config;
    }

}

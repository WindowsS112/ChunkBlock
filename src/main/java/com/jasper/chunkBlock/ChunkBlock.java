package com.jasper.chunkBlock;

import com.jasper.chunkBlock.commands.BypassCommand;
import com.jasper.chunkBlock.commands.CreateCommand;
import com.jasper.chunkBlock.listeners.PlayerJoinListener;
import com.jasper.chunkBlock.util.BorderStorage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ChunkBlock extends JavaPlugin {


    private File configFile = new File(getDataFolder(), "config.yml");
    private File borderData = new File(getDataFolder(), "borders.yml");

    private BorderStorage borderStorage;  // veld
    YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(borderData);

    private double cSize;
    private FileConfiguration config;


    @Override
    public void onEnable() {
        if (!configFile.exists()) {
            saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        cSize = getConfig().getInt("defaultChunkSize");

        if (!borderData.exists()) {
            try {
                borderData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.borderStorage = new BorderStorage(borderData,this, modifyFile);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(borderStorage, this), this);

        getCommand("chunk").setExecutor(new CreateCommand(cSize, borderData,this));
        getCommand("bypass").setExecutor(new BypassCommand(borderStorage));

        Bukkit.getLogger().info("[ChunkBlock] -> Has Been Started!");
    }

    public BorderStorage getBorderStorage() {
        return this.borderStorage;
    }

    public FileConfiguration getCustomConfig() {
        return config;
    }

}

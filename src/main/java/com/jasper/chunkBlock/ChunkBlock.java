package com.jasper.chunkBlock;

import com.github.yannicklamprecht.worldborder.api.WorldBorderApi;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ChunkBlock extends JavaPlugin {

    private File configFile = new File(getDataFolder(), "config.yml");
    private File borderData = new File(getDataFolder(), "borders.yml");

    private double cSize;
    private WorldBorderApi worldBorderApi;

    @Override
    public void onEnable() {
        if (!configFile.exists()) {
            saveDefaultConfig();
        } else {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().info("Can't load file! Error.");
                return;
            }
            cSize = getConfig().getInt("defaultChunkSize");
        }

        YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(borderData);

        RegisteredServiceProvider<WorldBorderApi> provider = getServer().getServicesManager().getRegistration(WorldBorderApi.class);

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        if (provider == null) {
            getLogger().warning("WorldBorder API niet gevonden! Plugin wordt uitgeschakeld.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        WorldBorderApi worldBorderApi = provider.getProvider();

        getCommand("chunk").setExecutor(new CreateChunk(worldBorderApi, cSize, borderData, modifyFile));


        Bukkit.getLogger().info("[ChunkBlock] -> Has Been Started!");
    }

}

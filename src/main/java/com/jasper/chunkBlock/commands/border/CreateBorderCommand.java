package com.jasper.chunkBlock.commands.border;

import com.jasper.chunkBlock.ChunkBlock;
import com.jasper.chunkBlock.commands.SubCommand;
import com.jasper.chunkBlock.util.BorderStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.io.File;


public class CreateBorderCommand extends SubCommand {

    private double cSize;
    private final File borderData;
    private final ChunkBlock plugin;
    private final BorderStorage storage;


    public CreateBorderCommand(String name, String description, String syntax, double cSize, File borderData1, ChunkBlock plugin) {
        super(name,description,syntax);
        this.cSize = cSize;
        this.borderData = borderData1;
        this.plugin = plugin;
        this.storage = plugin.getBorderStorage();
    }

    public void createChunk(Player player, double cSize, Location location) {
        try {
            WorldBorder border = Bukkit.createWorldBorder();
            border.setCenter(location);
            border.setSize(cSize);

            player.setWorldBorder(border);

            storage.saveChunk(player, cSize);

            player.sendMessage(ChatColor.GREEN + "Successfully created a chunk!");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public double getcSize() { return cSize; }

    @Override
    public String getName() { return "createchunk"; }

    @Override
    public String getDescription() { return "creates a chunk"; }

    @Override
    public String getSyntax() { return "/c createchunk"; }

    @Override
    public void perform(Player player, String[] args) {
        createChunk(player, cSize, player.getLocation());
    }
}

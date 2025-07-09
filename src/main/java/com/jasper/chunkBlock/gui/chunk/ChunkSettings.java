package com.jasper.chunkBlock.gui.chunk;

import com.jasper.chunkBlock.gui.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChunkSettings {

    public void open(Player player) {
        Gui gui = new BaseGui("§aChunk opties", 3)
                .addItem(13, Material.GRASS_BLOCK, "§aClaim deze chunk", p -> {
                    p.sendMessage("§aJe hebt deze chunk geclaimd!");
                })
                .build();

        gui.open(player);
    }
}

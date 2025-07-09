package com.jasper.chunkBlock.gui.chunk;

import com.jasper.chunkBlock.gui.BaseGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ChunkMain extends BaseGui {

    public ChunkMain(String title, int rows) {
        super(title, rows);
    }

    @Override
    public BaseGui addItem(int slot, @NotNull Material material, @NotNull String name, @NotNull Consumer<Player> clickAction) {


        return super.addItem(slot, material, name, clickAction);
    }


}

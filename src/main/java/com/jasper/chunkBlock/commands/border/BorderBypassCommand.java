package com.jasper.chunkBlock.commands.border;

import com.jasper.chunkBlock.commands.SubCommand;
import org.bukkit.entity.Player;

public class BorderBypassCommand extends SubCommand {

    private boolean hasBypass = false;
    private BorderStorage borderStorage;

    public BorderBypassCommand(String name, String description, String syntax, BorderStorage borderStorage) {
        super(name,description,syntax);
        this.borderStorage = borderStorage;
    }

    @Override
    public String getName() {
        return "bypass";
    }

    @Override
    public String getDescription() {
        return "Bypasses every border, toggable";
    }

    @Override
    public String getSyntax() {
        return "/c bypass <name>";
    }

    @Override
    public void perform(Player player, String[] args) {
        toggleBypass(player);
    }

    public void toggleBypass(Player player) {
        hasBypass = !hasBypass;

        if (hasBypass) {
            player.setWorldBorder(null);
            player.sendMessage("§aBypass activated.");
            System.out.println("Bypass activated.");
        } else {
//            borderStorage.loadBorder(player);
            player.sendMessage("§cBypass deactivated.");
            System.out.println("Bypass deactivated.");
        }
    }

}


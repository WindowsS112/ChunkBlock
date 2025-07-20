package com.jasper.chunkBlock.commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {
    private String name;
    private String description;
    private String syntax;


    public SubCommand(String name, String description, String syntax) {
        this.name = name;
        this.description = description;
        this.syntax = syntax;
    }

    public abstract void perform(Player player, String args[]);

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();







}

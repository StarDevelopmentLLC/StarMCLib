package com.stardevllc.starmclib.command;

import org.bukkit.plugin.java.JavaPlugin;

public interface ICommand<T extends JavaPlugin> {
    T getPlugin();
    
    String getName();
    
    String[] getAliases();
    
    String getDescription();
    
    String getPermission();
    
    default boolean isPlayerOnly() {
        return false;
    }
    
    default boolean isConsoleOnly() {
        return false;
    }
}
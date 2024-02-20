package com.stardevllc.starmclib.actor;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginActor extends Actor {
    
    private JavaPlugin plugin;

    protected PluginActor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public boolean isPlugin() {
        return true;
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof JavaPlugin other) {
            return this.plugin.getName().equalsIgnoreCase(other.getName());
        }
        
        if (object instanceof String str) {
            return this.plugin.getName().equalsIgnoreCase(str);
        }
        
        return false;
    }

    @Override
    public void sendMessage(String message) {
        plugin.getLogger().info("[StarChat Error]" + message);
    }

    @Override
    public String getName() {
        return plugin.getName();
    }
}

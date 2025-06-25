package com.stardevllc.starmclib.actors;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginActor extends Actor {
    
    private JavaPlugin plugin;

    protected PluginActor(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean canSee(Actor actor) {
        return true;
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
    public int hashcode() {
        return plugin.getName().hashCode();
    }

    @Override
    public void sendMessage(String message) {
        plugin.getLogger().info(message);
    }

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public String getConfigString() {
        return this.getName();
    }
}

package com.stardevllc.starmclib.actors;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class DefaultActorFactory implements ActorFactory {
    
    private final Map<Object, Actor> CACHE = new HashMap<>();
    
    public Actor create(Object object) {
        Actor actor = CACHE.get(object);
        if (actor != null) {
            return actor;
        }
        
        if (object instanceof Player player) {
            actor = of(player);
        } else if (object instanceof UUID uniqueId) {
            actor = of(uniqueId);
        } else if (object instanceof JavaPlugin plugin) {
            actor = of(plugin);
        } else if (object instanceof ConsoleCommandSender) {
            actor = getServerActor();
        } else if (object instanceof String str) {
            if (str.equalsIgnoreCase("console") || str.equalsIgnoreCase("server")) {
                actor = getServerActor();
            }
            
            try {
                return of(UUID.fromString(str));
            } catch (Exception e) {
            }
            
            Player player = Bukkit.getPlayer(str);
            if (player != null) {
                return of(player);
            }
            
            Plugin plugin = Bukkit.getPluginManager().getPlugin(str);
            if (plugin != null) {
                return of((JavaPlugin) plugin);
            }
        }
        
        if (!(object instanceof Player)) {
            CACHE.put(object, actor);
        }
        return actor;
    }
    
    public PlayerActor of(Player player) {
        return new PlayerActor(player);
    }
    
    public Actor of(UUID uniqueId) {
        if (uniqueId.equals(ServerActor.serverUUID)) {
            return getServerActor();
        }
        
        return new PlayerActor(uniqueId);
    }
    
    public PluginActor of(JavaPlugin plugin) {
        return new PluginActor(plugin);
    }
    
    public ServerActor getServerActor() {
        return ServerActor.instance;
    }
}

package com.stardevllc.starmclib.actors;

import com.stardevllc.starlib.observable.collections.ObservableHashMap;
import com.stardevllc.starlib.observable.collections.ObservableMap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Function;

public final class Actors {
    
    static {
        new ActorStringConverter();
    }
    
    private static final ObservableMap<Object, Actor> CACHE = new ObservableHashMap<>();
    
    private static Function<String, String> COLOR_FUNCTION = text -> ChatColor.translateAlternateColorCodes('&', text);
    
    public static Function<String, String> getColorFunction() {
        return COLOR_FUNCTION;
    }
    
    public static ObservableMap<Object, Actor> getActors() {
        return CACHE;
    }
    
    public static void setColorFunction(Function<String, String> colorFunction) {
        if (colorFunction == null) {
            return;
        }
        
        COLOR_FUNCTION = colorFunction;
    }
    
    public static Actor create(Object object) {
        if (object instanceof Player player) {
            Actor actor = CACHE.get(player.getUniqueId());
            if (actor != null) {
                return actor;
            }
            
            return of(player);
        } else if (object instanceof UUID uniqueId) {
            return of(uniqueId);
        } else if (object instanceof JavaPlugin plugin) {
            return of(plugin);
        } else if (object instanceof ConsoleCommandSender) {
            return getServerActor();
        } else if (object instanceof String str) {
            if (str.equalsIgnoreCase("console") || str.equalsIgnoreCase("server")) {
                return getServerActor();
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
        
        return null;
    }
    
    public static PlayerActor of(Player player) {
        if (CACHE.containsKey(player.getUniqueId())) {
            return (PlayerActor) CACHE.get(player.getUniqueId());
        }
        
        PlayerActor playerActor = new PlayerActor(player);
        CACHE.put(player.getUniqueId(), playerActor);
        return playerActor;
    }
    
    public static Actor of(UUID uniqueId) {
        if (CACHE.containsKey(uniqueId)) {
            return CACHE.get(uniqueId);
        }
        
        if (uniqueId.equals(ServerActor.serverUUID)) {
            return getServerActor();
        }
        
        PlayerActor playerActor = new PlayerActor(uniqueId);
        CACHE.put(playerActor.getUniqueId(), playerActor);
        return playerActor;
    }
    
    public static PluginActor of(JavaPlugin plugin) {
        if (CACHE.containsKey(plugin.getName())) {
            return (PluginActor) CACHE.get(plugin.getName());
        }
        
        PluginActor pluginActor = new PluginActor(plugin);
        CACHE.put(plugin.getName(), pluginActor);
        return pluginActor;
    }
    
    public static ServerActor getServerActor() {
        return ServerActor.instance;
    }
}

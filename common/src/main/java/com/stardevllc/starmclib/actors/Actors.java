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
    
    public static PlayerActor of(Player player) {
        return new PlayerActor(player);
    }
    
    public static Actor of(UUID uniqueId) {
        if (uniqueId.equals(ServerActor.serverUUID)) {
            return getServerActor();
        }
        
        return new PlayerActor(uniqueId);
    }
    
    public static PluginActor of(JavaPlugin plugin) {
        return new PluginActor(plugin);
    }
    
    public static ServerActor getServerActor() {
        return ServerActor.instance;
    }
}

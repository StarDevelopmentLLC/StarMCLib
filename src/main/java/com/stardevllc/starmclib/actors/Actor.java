package com.stardevllc.starmclib.actors;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Function;

public abstract class Actor {
    
    static {
        new ActorStringConverter();
    }
    
    private static ActorFactory actorFactory;
    
    public static final Map<Object, Actor> CACHE = new HashMap<>();

    private static Function<String, String> COLOR_FUNCTION = text -> ChatColor.translateAlternateColorCodes('&', text);

    public static Function<String, String> getColorFunction() {
        return COLOR_FUNCTION;
    }

    public static void setColorFunction(Function<String, String> colorFunction) {
        if (colorFunction == null) {
            return;
        }

        COLOR_FUNCTION = colorFunction;
    }
    
    public abstract boolean canSee(Actor actor);
    
    public abstract boolean equals(Object object);

    public abstract int hashcode();

    public abstract void sendMessage(String message);

    public void sendColoredMessage(String message) {
        sendMessage(Actor.getColorFunction().apply(message));
    }

    public abstract String getName();
    
    public abstract boolean hasPermission(String permission);
    
    public abstract String getConfigString();

    public boolean isPlayer() {
        return false;
    }

    public boolean isServer() {
        return false;
    }

    public boolean isPlugin() {
        return false;
    }

    public boolean isOnline() {
        return false;
    }
    
    public static void setActorFactory(ActorFactory actorFactory) {
        ActorFactory old = Actor.actorFactory;
        Actor.actorFactory = actorFactory;
        
//        if (old == null) {
//            StarCoreAPI.getAPI().getLogger().info("ActorFactory was set to " + Actor.actorFactory.getClass().getName());
//        } else {
//            StarCoreAPI.getAPI().getLogger().info("The ActorFactory instance was changed from " + old.getClass().getName() + " to " + Actor.actorFactory.getClass().getName());
//        }
    }
    
    private static ActorFactory getActorFactory() {
        if (actorFactory == null) {
            setActorFactory(new DefaultActorFactory());
        }
        
        return actorFactory;
    }
    
    public static Actor create(Object object) {
        return getActorFactory().create(object);
    }

    public static PlayerActor of(Player player) {
        return getActorFactory().of(player);
    }

    public static Actor of(UUID uniqueId) {
        return getActorFactory().of(uniqueId);
    }

    public static PluginActor of(JavaPlugin plugin) {
        return getActorFactory().of(plugin);
    }

    public static ServerActor getServerActor() {
        return getActorFactory().getServerActor();
    }
}
package com.stardevllc.starmclib.actors;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public interface ActorFactory {
    Actor create(Object object);
    
    PlayerActor of(Player player);
    
    Actor of(UUID uniqueId);
    
    PluginActor of(JavaPlugin plugin);
    
    ServerActor getServerActor();
}

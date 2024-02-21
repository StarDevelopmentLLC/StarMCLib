package com.stardevllc.starmclib.actor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerActor extends Actor {
    
    private UUID uniqueId;

    protected PlayerActor(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    public PlayerActor(Player player) {
        this.uniqueId = player.getUniqueId();
    }

    public UUID getUniqueId() {
        return uniqueId;
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }
    
    public boolean isOnline() {
        return getPlayer() != null;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        
        if (object instanceof UUID uuid) {
            return this.uniqueId.equals(uuid);
        }
        
        if (object instanceof Player player) {
            return this.uniqueId.equals(player.getUniqueId());
        }
        
        if (object instanceof PlayerActor other) {
            return this.uniqueId.equals(other.getUniqueId());
        }
        
        return false;
    }

    @Override
    public int hashcode() {
        return this.uniqueId.hashCode();
    }

    @Override
    public void sendMessage(String message) {
        Player player = getPlayer();
        if (player != null) {
            player.sendMessage(message);
        }
    }

    @Override
    public String getName() {
        Player player = getPlayer();
        if (player != null) {
            return player.getName();
        }
        return this.uniqueId.toString();
    }
}

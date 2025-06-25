package com.stardevllc.starmclib.actors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerActor extends Actor {
    
    private UUID uniqueId;

    protected PlayerActor(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    @Override
    public boolean canSee(Actor actor) {
        if (actor instanceof ServerActor || actor instanceof PluginActor) {
            return true;
        } else if (actor instanceof PlayerActor playerActor) {
            return getPlayer().canSee(playerActor.getPlayer());
        }
        
        return false;
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
    public boolean hasPermission(String permission) {
        Player player = getPlayer();
        if (player == null) {
            return false;
        }
        
        return player.hasPermission(permission);
    }

    @Override
    public String getConfigString() {
        return this.uniqueId.toString();
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
        
        return switch (object) {
            case UUID uuid -> this.uniqueId.equals(uuid);
            case Player player -> this.uniqueId.equals(player.getUniqueId());
            case PlayerActor other -> this.uniqueId.equals(other.getUniqueId());
            default -> false;
        };

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

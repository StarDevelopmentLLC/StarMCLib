package com.stardevllc.starmclib.actors;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.util.UUID;

public class ServerActor implements Actor {
    public static final ServerActor instance = new ServerActor();
    
    public static UUID serverUUID;

    private ServerActor() {}

    @Override
    public boolean isServer() {
        return true;
    }
    
    @Override
    public boolean canSee(Actor actor) {
        return true;
    }
    
    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ServerActor) {
            return true;
        }
        
        if (object instanceof UUID uuid) {
            if (serverUUID != null) {
                return serverUUID.equals(uuid);
            }
        }

        return object instanceof ConsoleCommandSender;
    }

    @Override
    public int hashcode() {
        return 1;
    }

    @Override
    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public String getConfigString() {
        return "console";
    }
}

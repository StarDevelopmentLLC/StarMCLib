package com.stardevllc.starmclib.actor;

import com.stardevllc.starchat.StarChat;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.util.UUID;

public class ServerActor extends Actor {
    public static final ServerActor instance = new ServerActor();

    private ServerActor() {}

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ServerActor) {
            return true;
        }
        
        if (object instanceof ConsoleCommandSender) {
            return true;
        }
        
        if (object instanceof UUID uuid) {
            return uuid.equals(StarChat.consoleUniqueId);
        }
        
        return false;
    }

    @Override
    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    @Override
    public String getName() {
        return "Console";
    }
}

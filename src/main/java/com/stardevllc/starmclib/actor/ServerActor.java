package com.stardevllc.starmclib.actor;

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
    public boolean isOnline() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ServerActor) {
            return true;
        }

        return object instanceof ConsoleCommandSender;
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

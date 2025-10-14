package com.stardevllc.starmclib.actors;

public interface Actor {
    
    boolean canSee(Actor actor);
    
    boolean equals(Object object);

    int hashcode();

    void sendMessage(String message);

    default void sendColoredMessage(String message) {
        sendMessage(Actors.getColorFunction().apply(message));
    }

    String getName();
    
    boolean hasPermission(String permission);
    
    String getConfigString();

    default boolean isPlayer() {
        return false;
    }
    
    default boolean isServer() {
        return false;
    }
    
    default boolean isPlugin() {
        return false;
    }
    
    default boolean isOnline() {
        return false;
    }
}
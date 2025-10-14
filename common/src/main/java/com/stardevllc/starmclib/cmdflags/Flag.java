package com.stardevllc.starmclib.cmdflags;

public interface Flag {
    String id();
    FlagType type();
    String name();
    boolean equals(Object other);

    //Complex flag
    default Object defaultValue() {
        return null;
    }

    //Presence flag
    default Object valueIfPresent() {
        return true;
    }

    default Object valueIfNotPresent() {
        return false;
    }
    
    static boolean equals(Flag flag, Object other) {
        if (other instanceof Flag otherFlag) {
            if (flag.type() == otherFlag.type()) {
                return flag.name().equalsIgnoreCase(otherFlag.name());
            }
        }
        
        return false;
    }
}

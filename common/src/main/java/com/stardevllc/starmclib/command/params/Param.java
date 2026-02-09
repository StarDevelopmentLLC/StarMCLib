package com.stardevllc.starmclib.command.params;

public record Param(String id, String name, Object defaultValue) {
    public Param(String id, String name) {
        this(id, name, null);
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof Param otherFlag) {
            return id().equalsIgnoreCase(otherFlag.name());
        }
        
        return false;
    }
}
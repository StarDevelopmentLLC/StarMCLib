package com.stardevllc.starmclib.command.params;

public record Param<T>(String id, String name, Class<T> type, T defaultValue) {
    public Param(String id, String name, Class<T> type, T defaultValue) {
        this.id = id;
        this.name = name;
        this.type = type;
        if (defaultValue == null) {
            throw new IllegalArgumentException("Default value cannot be null");
        }
        this.defaultValue = defaultValue;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof Param<?> otherFlag) {
            return id().equalsIgnoreCase(otherFlag.name());
        }
        
        return false;
    }
}
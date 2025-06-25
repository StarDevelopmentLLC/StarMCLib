package com.stardevllc.starmclib.cmdflags;

import java.util.Map;

public record FlagResult(String[] args, Map<Flag, Object> flagValues) {
    public boolean isPresent(Flag flag) {
        if (flag.type() == FlagType.COMPLEX) {
            return flagValues.containsKey(flag);
        } else {
            Object flagValue = flagValues.get(flag);
            if (flagValue != null) {
                return (boolean) flagValue;
            }
        }
        
        return false;
    }
    
    public void addFrom(FlagResult otherResults) {
        flagValues.putAll(otherResults.flagValues);
    }
    
    public Object getValue(Flag flag) {
        return flagValues.get(flag);
    }
}

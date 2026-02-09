package com.stardevllc.starmclib.command.params;

import java.util.Map;

public record ParamResult(String[] args, Map<Param, Object> flagValues) {
    public boolean isPresent(Param flag) {
        return flagValues.containsKey(flag);
    }
    
    public void addFrom(ParamResult otherResults) {
        flagValues.putAll(otherResults.flagValues);
    }
    
    public Object getValue(Param flag) {
        return flagValues.get(flag);
    }
}

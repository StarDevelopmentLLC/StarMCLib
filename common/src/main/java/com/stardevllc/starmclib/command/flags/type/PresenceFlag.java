package com.stardevllc.starmclib.command.flags.type;

import com.stardevllc.starmclib.command.flags.FlagType;

public class PresenceFlag extends AbstractFlag {
    public PresenceFlag(String id, String name) {
        super(id, name);
    }

    @Override
    public Object defaultValue() {
        return false;
    }

    @Override
    public FlagType type() {
        return FlagType.PRESENCE;
    }
}

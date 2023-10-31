package com.stardevllc.starmclib.objecttester.codex;

import com.stardevllc.starmclib.objecttester.TypeCodex;

import java.util.UUID;

public class UUIDCodex extends TypeCodex {
    public UUIDCodex() {
        super("uuid", UUID.class);
    }

    @Override
    public String serialize(Object object) {
        if (isValidType(object)) {
            return object.toString();
        }
        return null;
    }

    @Override
    public Object deserialize(String serialized) {
        return UUID.fromString(serialized);
    }
}

package com.stardevllc.starmclib.objecttester.codex;

import com.stardevllc.starmclib.objecttester.TypeCodex;

public class LongCodex extends TypeCodex {
    public LongCodex() {
        super("long", Long.class, long.class);
    }

    @Override
    public String serialize(Object object) {
        if (isValidType(object)) {
            return object + "";
        }
        return null;
    }

    @Override
    public Object deserialize(String serialized) {
        return Long.parseLong(serialized);
    }
}

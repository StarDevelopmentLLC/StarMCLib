package com.stardevllc.starmclib.objecttester.codex;

import com.stardevllc.starmclib.objecttester.TypeCodex;

public class IntegerCodex extends TypeCodex {
    public IntegerCodex() {
        super("int", Integer.class, int.class);
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
        return Integer.parseInt(serialized);
    }
}

package com.stardevllc.starmclib.objecttester.codex;

import com.stardevllc.starmclib.objecttester.TypeCodex;

public class ByteCodex extends TypeCodex {
    public ByteCodex() {
        super("byte", Byte.class, byte.class);
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
        return Byte.parseByte(serialized);
    }
}

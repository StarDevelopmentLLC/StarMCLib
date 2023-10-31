package com.stardevllc.starmclib.objecttester.codex;

import com.stardevllc.starmclib.objecttester.TypeCodex;

public class FloatCodex extends TypeCodex {
    public FloatCodex() {
        super("float", Float.class, float.class);
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
        return Float.parseFloat(serialized);
    }
}

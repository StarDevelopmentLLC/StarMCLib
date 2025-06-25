package com.stardevllc.starmclib.cmdflags.type;

import com.stardevllc.starmclib.cmdflags.Flag;

public abstract class AbstractFlag implements Flag {
    
    protected final String id, name;

    public AbstractFlag(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
       return Flag.equals(this, o);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

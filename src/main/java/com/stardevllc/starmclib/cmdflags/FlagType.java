package com.stardevllc.starmclib.cmdflags;

import com.stardevllc.starlib.converter.string.EnumStringConverter;
import com.stardevllc.starlib.converter.string.StringConverters;

public enum FlagType {
    PRESENCE, COMPLEX; 
    
    static {
        StringConverters.addConverter(FlagType.class, new EnumStringConverter<>(FlagType.class));
    }
}

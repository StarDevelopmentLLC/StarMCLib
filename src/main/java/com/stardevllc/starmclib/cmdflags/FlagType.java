package com.stardevllc.starmclib.cmdflags;

import com.stardevllc.converter.string.EnumStringConverter;
import com.stardevllc.converter.string.StringConverters;

public enum FlagType {
    PRESENCE, COMPLEX; 
    
    static {
        StringConverters.addConverter(FlagType.class, new EnumStringConverter<>(FlagType.class));
    }
}

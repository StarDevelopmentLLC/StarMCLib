package com.stardevllc.starmclib.actors;

import com.stardevllc.starlib.converter.string.StringConverter;
import com.stardevllc.starlib.converter.string.StringConverters;

public class ActorStringConverter implements StringConverter<Actor> {
    
    protected ActorStringConverter() {
        StringConverters.addConverter(Actor.class, this);
        StringConverters.addConverter(PlayerActor.class, this);
        StringConverters.addConverter(PluginActor.class, this);
        StringConverters.addConverter(ServerActor.class, this);
    }
    
    @Override
    public String convertFrom(Object fromObject) {
        if (fromObject instanceof Actor actor) {
            return actor.getName();
        }
        
        return "";
    }
    
    @Override
    public Actor convertTo(String toObject) {
        return Actors.create(toObject);
    }
}

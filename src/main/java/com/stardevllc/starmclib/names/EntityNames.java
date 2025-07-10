package com.stardevllc.starmclib.names;

import com.stardevllc.helper.StringHelper;
import org.bukkit.entity.EntityType;

import java.util.*;

public class EntityNames {
    public final Map<EntityType, String> entityNames = new EnumMap<>(EntityType.class);
    private static final EntityNames instance = new EntityNames() {
        @Override
        public void setName(EntityType entityType, String name) {
            throw new RuntimeException("Cannot set the entity name using the default instance");
        }
    };
    
    public static EntityNames createInstance() {
        return new EntityNames();
    }
    
    public static EntityNames getInstance() {
        return instance;
    }

    private EntityNames() {
        for (EntityType entityType : EntityType.values()) {
            entityNames.put(entityType, StringHelper.titlize(entityType.name()));
        }
    }
    
    public static String getDefaultName(EntityType entityType) {
        return getInstance().getName(entityType);
    }
    
    public String getName(EntityType entityType) {
        return entityNames.get(entityType);
    }
    
    public void setName(EntityType entityType, String name) {
        entityNames.put(entityType, name);
    }
}
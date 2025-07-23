package com.stardevllc.starmclib.names;

import com.stardevllc.starlib.helper.StringHelper;
import org.bukkit.Material;

import java.util.*;

public class MaterialNames {
    private final Map<Material, String> materialNames = new EnumMap<>(Material.class);
    private static final MaterialNames instance = new MaterialNames() {
        @Override
        public void setName(Material material, String name) {
            throw new RuntimeException("Could not set the material name using the default instance.");
        }
    };
    
    public static MaterialNames getInstance() {
        return instance;
    }
    
    public static MaterialNames createInstance() {
        return new MaterialNames();
    }

    private MaterialNames() {
        for (Material material : Material.values()) {
            materialNames.put(material, StringHelper.titlize(material.name()));
        }
    }
    
    public String getName(Material material) {
        if (material == null) {
            return "None";
        }
        return materialNames.getOrDefault(material, "None");
    }
    
    public static String getDefaultName(Material material) {
        return getInstance().getName(material);
    }
    
    public void setName(Material material, String name) {
        this.materialNames.put(material, name);
    }
}

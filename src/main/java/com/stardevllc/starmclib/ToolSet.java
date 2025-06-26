package com.stardevllc.starmclib;

import com.stardevllc.converter.string.EnumStringConverter;
import com.stardevllc.converter.string.StringConverters;

public enum ToolSet {
    
    WOOD(XMaterial.WOODEN_SWORD, XMaterial.WOODEN_PICKAXE, XMaterial.WOODEN_AXE, XMaterial.WOODEN_SHOVEL, XMaterial.WOODEN_HOE),
    STONE(XMaterial.STONE_SWORD, XMaterial.STONE_PICKAXE, XMaterial.STONE_AXE, XMaterial.STONE_SHOVEL, XMaterial.STONE_HOE),
    GOLD(XMaterial.GOLDEN_SWORD, XMaterial.GOLDEN_PICKAXE, XMaterial.GOLDEN_AXE, XMaterial.GOLDEN_SHOVEL, XMaterial.GOLDEN_HOE),
    IRON(XMaterial.IRON_SWORD, XMaterial.IRON_PICKAXE, XMaterial.IRON_AXE, XMaterial.IRON_SHOVEL, XMaterial.IRON_HOE),
    DIAMOND(XMaterial.DIAMOND_SWORD, XMaterial.DIAMOND_PICKAXE, XMaterial.DIAMOND_AXE, XMaterial.DIAMOND_SHOVEL, XMaterial.DIAMOND_HOE),
    NETHERITE(XMaterial.NETHERITE_SWORD, XMaterial.NETHERITE_PICKAXE, XMaterial.NETHERITE_AXE, XMaterial.NETHERITE_SHOVEL, XMaterial.NETHERITE_HOE);
    
    static {
        StringConverters.addConverter(ToolSet.class, new EnumStringConverter<>(ToolSet.class));
    }
    
    private final XMaterial sword, pickaxe, axe, shovel, hoe;

    ToolSet(XMaterial sword, XMaterial pickaxe, XMaterial axe, XMaterial shovel, XMaterial hoe) {
        this.sword = sword;
        this.pickaxe = pickaxe;
        this.axe = axe;
        this.shovel = shovel;
        this.hoe = hoe;
    }

    public XMaterial getPickaxe() {
        return pickaxe;
    }

    public XMaterial getAxe() {
        return axe;
    }

    public XMaterial getShovel() {
        return shovel;
    }

    public XMaterial getHoe() {
        return hoe;
    }

    public XMaterial getSword() {
        return sword;
    }
}
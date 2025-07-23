package com.stardevllc.starmclib.materialset;

import com.stardevllc.starlib.converter.string.EnumStringConverter;
import com.stardevllc.starlib.converter.string.StringConverters;
import com.stardevllc.starmclib.XMaterial;

public enum ArmorSet {

    NONE(XMaterial.AIR, XMaterial.AIR, XMaterial.AIR, XMaterial.AIR),
    LEATHER(XMaterial.LEATHER_HELMET, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_BOOTS),
    CHAINMAIL(XMaterial.CHAINMAIL_HELMET, XMaterial.CHAINMAIL_CHESTPLATE, XMaterial.CHAINMAIL_LEGGINGS, XMaterial.CHAINMAIL_BOOTS),
    GOLD(XMaterial.GOLDEN_HELMET, XMaterial.GOLDEN_CHESTPLATE, XMaterial.GOLDEN_LEGGINGS, XMaterial.GOLDEN_BOOTS),
    IRON(XMaterial.IRON_HELMET, XMaterial.IRON_CHESTPLATE, XMaterial.IRON_LEGGINGS, XMaterial.IRON_BOOTS),
    DIAMOND(XMaterial.DIAMOND_HELMET, XMaterial.DIAMOND_CHESTPLATE, XMaterial.DIAMOND_LEGGINGS, XMaterial.DIAMOND_BOOTS), 
    NETHERITE(XMaterial.NETHERITE_HELMET, XMaterial.NETHERITE_CHESTPLATE, XMaterial.NETHERITE_LEGGINGS, XMaterial.NETHERITE_BOOTS);
    
    static {
        StringConverters.addConverter(ArmorSet.class, new EnumStringConverter<>(ArmorSet.class));
    }

    private final XMaterial helmet, chestplate, leggings, boots;

    ArmorSet(XMaterial helmet, XMaterial chestplate, XMaterial leggings, XMaterial boots) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public XMaterial getHelmet() {
        return helmet;
    }

    public XMaterial getChestplate() {
        return chestplate;
    }

    public XMaterial getLeggings() {
        return leggings;
    }

    public XMaterial getBoots() {
        return boots;
    }
}

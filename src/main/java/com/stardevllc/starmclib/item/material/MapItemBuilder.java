package com.stardevllc.starmclib.item.material;

import com.cryptomorin.xseries.XMaterial;
import com.stardevllc.starmclib.item.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.List;

public class MapItemBuilder extends ItemBuilder {
    
    private Color color;
    private MapView mapView;
    private boolean scaling;
    
    public MapItemBuilder() {
        super(XMaterial.MAP);
    }
    
    protected static MapItemBuilder createFromItemStack(ItemStack itemStack) {
        MapItemBuilder builder = new MapItemBuilder();
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        builder.color(meta.getColor()).mapView(meta.getMapView()).scaling(meta.isScaling());
        return builder;
    }

    protected static MapItemBuilder createFromConfig(ConfigurationSection section) {
        return new MapItemBuilder();
    }

    @Override
    public void saveToConfig(ConfigurationSection section) {
        super.saveToConfig(section);
    }
    
    public MapItemBuilder color(Color color) {
        this.color = color;
        return this;
    }
    
    public MapItemBuilder mapView(MapView mapView) {
        this.mapView = mapView;
        return this;
    }
    
    public MapItemBuilder scaling(boolean scaling) {
        this.scaling = scaling;
        return this;
    }
    
    @Override
    public MapItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        super.addAttributeModifier(attribute, name, amount, operation, slot);
        return this;
    }

    @Override
    public MapItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        super.addAttributeModifier(attribute, name, amount, operation);
        return this;
    }

    @Override
    public MapItemBuilder addEnchant(Enchantment enchantment, int level) {
        super.addEnchant(enchantment, level);
        return this;
    }

    @Override
    public MapItemBuilder addItemFlags(ItemFlag... itemFlags) {
        super.addItemFlags(itemFlags);
        return this;
    }

    @Override
    public MapItemBuilder setLore(List<String> lore) {
        super.setLore(lore);
        return this;
    }

    @Override
    public MapItemBuilder addLoreLine(String line) {
        super.addLoreLine(line);
        return this;
    }

    @Override
    public MapItemBuilder material(XMaterial material) {
        super.material(material);
        return this;
    }

    @Override
    public MapItemBuilder amount(int amount) {
        super.amount(amount);
        return this;
    }

    @Override
    public MapItemBuilder displayName(String displayName) {
        super.displayName(displayName);
        return this;
    }

    @Override
    public MapItemBuilder unbreakable(boolean unbreakable) {
        super.unbreakable(unbreakable);
        return this;
    }

    @Override
    protected MapMeta createItemMeta() {
        MapMeta itemMeta = (MapMeta) super.createItemMeta();
        itemMeta.setColor(this.color);
        itemMeta.setMapView(this.mapView);
        itemMeta.setScaling(this.scaling);
        return itemMeta;
    }

    @Override
    public MapItemBuilder clone() {
        MapItemBuilder clone = (MapItemBuilder) super.clone();
        clone.color = this.color;
        clone.mapView = this.mapView;
        clone.scaling = this.scaling;
        return clone;
    }
}

package com.stardevllc.starmclib.item.material;

import com.cryptomorin.xseries.XMaterial;
import com.stardevllc.starmclib.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.List;

public class CompassItemBuilder extends ItemBuilder {
    
    private Location lodestone;
    private boolean tracked;
    
    public CompassItemBuilder() {
        super(XMaterial.COMPASS);
    }
    
    protected static CompassItemBuilder createFromItemStack(ItemStack itemStack) {
        CompassItemBuilder itemBuilder = new CompassItemBuilder();
        CompassMeta itemMeta = (CompassMeta) itemStack.getItemMeta();
        itemBuilder.lodestone(itemMeta.getLodestone().clone()).tracked(itemMeta.isLodestoneTracked());
        return itemBuilder;
    }

    protected static CompassItemBuilder createFromConfig(ConfigurationSection section) {
        CompassItemBuilder builder = new CompassItemBuilder();
        builder.tracked(section.getBoolean("tracked"));
        if (section.contains("lodestone")) {
            World world = Bukkit.getWorld(section.getString("lodestone.world"));
            int x = section.getInt("lodestone.x");
            int y = section.getInt("lodestone.y");
            int z = section.getInt("lodestone.z");
            builder.lodestone(new Location(world, x, y, z));
        }
        return builder;
    }

    @Override
    public void saveToConfig(ConfigurationSection section) {
        super.saveToConfig(section);
        section.set("tracked", this.tracked);
        if (this.lodestone != null) {
            section.set("lodestone.x", this.lodestone.getBlockX());
            section.set("lodestone.y", this.lodestone.getBlockY());
            section.set("lodestone.z", this.lodestone.getBlockZ());
            section.set("lodestone.world", this.lodestone.getWorld().getName());
        }
    }
    
    public CompassItemBuilder lodestone(Location location) {
        this.lodestone = location;
        return this;
    }
    
    public CompassItemBuilder tracked(boolean tracked) {
        this.tracked = tracked;
        return this;
    }
    
    @Override
    public CompassItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        super.addAttributeModifier(attribute, name, amount, operation, slot);
        return this;
    }

    @Override
    public CompassItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        super.addAttributeModifier(attribute, name, amount, operation);
        return this;
    }

    @Override
    public CompassItemBuilder addEnchant(Enchantment enchantment, int level) {
        super.addEnchant(enchantment, level);
        return this;
    }

    @Override
    public CompassItemBuilder addItemFlags(ItemFlag... itemFlags) {
        super.addItemFlags(itemFlags);
        return this;
    }

    @Override
    public CompassItemBuilder setLore(List<String> lore) {
        super.setLore(lore);
        return this;
    }

    @Override
    public CompassItemBuilder addLoreLine(String line) {
        super.addLoreLine(line);
        return this;
    }

    @Override
    public CompassItemBuilder material(XMaterial material) {
        super.material(material);
        return this;
    }

    @Override
    public CompassItemBuilder amount(int amount) {
        super.amount(amount);
        return this;
    }

    @Override
    public CompassItemBuilder displayName(String displayName) {
        super.displayName(displayName);
        return this;
    }

    @Override
    public CompassItemBuilder unbreakable(boolean unbreakable) {
        super.unbreakable(unbreakable);
        return this;
    }

    @Override
    protected CompassMeta createItemMeta() {
        CompassMeta itemMeta = (CompassMeta) super.createItemMeta();
        itemMeta.setLodestone(this.lodestone);
        itemMeta.setLodestoneTracked(this.tracked);
        return itemMeta;
    }

    @Override
    public CompassItemBuilder clone() {
        CompassItemBuilder clone = (CompassItemBuilder) super.clone();
        if (this.lodestone != null) {
            clone.lodestone = this.lodestone.clone();
        }
        clone.tracked = this.tracked;
        return clone;
    }
}

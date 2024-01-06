package com.stardevllc.starmclib.item.material;

import com.cryptomorin.xseries.XMaterial;
import com.stardevllc.starmclib.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.List;

public class ArmorItemBuilder extends ItemBuilder {

    protected static ArmorItemBuilder createFromItemStack(ItemStack itemStack) {
        ArmorItemBuilder itemBuilder = new ArmorItemBuilder(XMaterial.matchXMaterial(itemStack));
        ArmorMeta itemMeta = (ArmorMeta) itemStack.getItemMeta();
        itemBuilder.trim(itemMeta.getTrim());
        if (itemMeta instanceof LeatherArmorMeta leatherArmor) {
            itemBuilder.color(leatherArmor.getColor());
        }
        return itemBuilder;
    }
    
    protected static ArmorItemBuilder createFromConfig(ConfigurationSection section) {
        ArmorItemBuilder builder = new ArmorItemBuilder();
        TrimMaterial tm = Bukkit.getRegistry(TrimMaterial.class).get(NamespacedKey.fromString(section.getString("trim.material")));
        TrimPattern tp = Bukkit.getRegistry(TrimPattern.class).get(NamespacedKey.fromString(section.getString("trim.pattern")));
        builder.trim(new ArmorTrim(tm, tp));
        return builder;
    }
    
    protected ArmorTrim trim;
    protected Color color;
    
    public ArmorItemBuilder(XMaterial material) {
        super(material);
    }
    
    protected ArmorItemBuilder() {}

    @Override
    public void saveToConfig(ConfigurationSection section) {
        super.saveToConfig(section);
        section.set("trim.material", trim.getMaterial().getKey().toString());
        section.set("trim.pattern", trim.getPattern().getKey().toString());
        if (color != null) {
            section.set("color", color.asRGB());
        }
    }

    public ArmorItemBuilder trim(ArmorTrim trim) {
        this.trim = trim;
        return this;
    }
    
    public ArmorItemBuilder color(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public ArmorItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        super.addAttributeModifier(attribute, name, amount, operation, slot);
        return this;
    }
    
    public ArmorItemBuilder setArmorModifier(String id, double amount, EquipmentSlot slot) {
        super.addAttributeModifier(Attribute.GENERIC_ARMOR, id, amount, AttributeModifier.Operation.ADD_NUMBER, slot);
        return this;
    }

    public ArmorItemBuilder setToughnessModifier(String id, double amount, EquipmentSlot slot) {
        super.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, id, amount, AttributeModifier.Operation.ADD_NUMBER, slot);
        return this;
    }

    public ArmorItemBuilder setKnockbackResistanceModifier(String id, double amount, EquipmentSlot slot) {
        super.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, id, amount, AttributeModifier.Operation.ADD_NUMBER, slot);
        return this;
    }

    @Override
    public ArmorItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        super.addAttributeModifier(attribute, name, amount, operation);
        return this;
    }

    public ArmorItemBuilder setArmorModifier(String id, double amount) {
        super.addAttributeModifier(Attribute.GENERIC_ARMOR, id, amount, AttributeModifier.Operation.ADD_NUMBER);
        return this;
    }

    public ArmorItemBuilder setToughnessModifier(String id, double amount) {
        super.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, id, amount, AttributeModifier.Operation.ADD_NUMBER);
        return this;
    }

    public ArmorItemBuilder setKnockbackResistanceModifier(String id, double amount) {
        super.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, id, amount, AttributeModifier.Operation.ADD_NUMBER);
        return this;
    }

    @Override
    public ArmorItemBuilder addEnchant(Enchantment enchantment, int level) {
        super.addEnchant(enchantment, level);
        return this;
    }

    @Override
    public ArmorItemBuilder addItemFlags(ItemFlag... itemFlags) {
        super.addItemFlags(itemFlags);
        return this;
    }

    @Override
    public ArmorItemBuilder setLore(List<String> lore) {
        super.setLore(lore);
        return this;
    }

    @Override
    public ArmorItemBuilder addLoreLine(String line) {
        super.addLoreLine(line);
        return this;
    }

    @Override
    public ArmorItemBuilder material(XMaterial material) {
        super.material(material);
        return this;
    }

    @Override
    public ArmorItemBuilder amount(int amount) {
        super.amount(amount);
        return this;
    }

    @Override
    public ArmorItemBuilder displayName(String displayName) {
        super.displayName(displayName);
        return this;
    }

    @Override
    public ArmorItemBuilder unbreakable(boolean unbreakable) {
        super.unbreakable(unbreakable);
        return this;
    }

    @Override
    protected ArmorMeta createItemMeta() {
        ArmorMeta itemMeta = (ArmorMeta) super.createItemMeta();
        itemMeta.setTrim(this.trim);
        if (itemMeta instanceof LeatherArmorMeta colorableArmorMeta) {
            colorableArmorMeta.setColor(this.color);
        }
        return itemMeta;
    }

    @Override
    public ArmorItemBuilder clone() {
        ArmorItemBuilder clone = (ArmorItemBuilder) super.clone();
        clone.trim = this.trim;
        clone.color = this.color;
        return clone;
    }
}

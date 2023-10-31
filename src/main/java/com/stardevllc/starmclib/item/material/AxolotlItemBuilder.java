package com.stardevllc.starmclib.item.material;

import com.cryptomorin.xseries.XMaterial;
import com.stardevllc.starmclib.item.ItemBuilder;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;

import java.util.List;

public class AxolotlItemBuilder extends ItemBuilder {
    
    private Axolotl.Variant variant;
    
    public AxolotlItemBuilder() {
        super(XMaterial.AXOLOTL_BUCKET);
    }
    
    protected static AxolotlItemBuilder createFromItemStack(ItemStack itemStack) {
        AxolotlItemBuilder itemBuilder = new AxolotlItemBuilder();
        AxolotlBucketMeta itemMeta = (AxolotlBucketMeta) itemStack.getItemMeta();
        itemBuilder.variant(itemMeta.getVariant());
        return itemBuilder;
    }
    
    protected static AxolotlItemBuilder createFromConfig(ConfigurationSection section) {
        AxolotlItemBuilder builder = new AxolotlItemBuilder();
        builder.variant(Axolotl.Variant.valueOf(section.getString("variant")));
        return builder;
    }

    @Override
    public void saveToConfig(ConfigurationSection section) {
        super.saveToConfig(section);
        section.set("variant", variant.name());
    }

    public AxolotlItemBuilder variant(Axolotl.Variant variant) {
        this.variant = variant;
        return this;
    }

    @Override
    public AxolotlItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        super.addAttributeModifier(attribute, name, amount, operation, slot);
        return this;
    }

    @Override
    public AxolotlItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        super.addAttributeModifier(attribute, name, amount, operation);
        return this;
    }

    @Override
    public AxolotlItemBuilder addEnchant(Enchantment enchantment, int level) {
        super.addEnchant(enchantment, level);
        return this;
    }

    @Override
    public AxolotlItemBuilder addItemFlags(ItemFlag... itemFlags) {
        super.addItemFlags(itemFlags);
        return this;
    }

    @Override
    public AxolotlItemBuilder setLore(List<String> lore) {
        super.setLore(lore);
        return this;
    }

    @Override
    public AxolotlItemBuilder addLoreLine(String line) {
        super.addLoreLine(line);
        return this;
    }

    @Override
    public AxolotlItemBuilder material(XMaterial material) {
        super.material(material);
        return this;
    }

    @Override
    public AxolotlItemBuilder amount(int amount) {
        super.amount(amount);
        return this;
    }

    @Override
    public AxolotlItemBuilder displayName(String displayName) {
        super.displayName(displayName);
        return this;
    }

    @Override
    public AxolotlItemBuilder unbreakable(boolean unbreakable) {
        super.unbreakable(unbreakable);
        return this;
    }

    @Override
    protected AxolotlBucketMeta createItemMeta() {
        AxolotlBucketMeta meta = (AxolotlBucketMeta) super.createItemMeta();
        meta.setVariant(this.variant);
        return meta;
    }

    @Override
    public AxolotlItemBuilder clone() {
        AxolotlItemBuilder clone = (AxolotlItemBuilder) super.clone();
        clone.variant = this.variant;
        return clone;
    }
}

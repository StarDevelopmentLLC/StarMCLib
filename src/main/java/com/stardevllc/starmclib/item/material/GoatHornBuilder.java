package com.stardevllc.starmclib.item.material;

import com.cryptomorin.xseries.XMaterial;
import com.stardevllc.starmclib.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MusicInstrumentMeta;

import java.util.List;

public class GoatHornBuilder extends ItemBuilder {
    
    private MusicInstrument instrument;
    
    public GoatHornBuilder() {
        super(XMaterial.GOAT_HORN);
    }
    
    protected static GoatHornBuilder createFromItemStack(ItemStack itemStack) {
        GoatHornBuilder builder = new GoatHornBuilder();
        MusicInstrumentMeta meta = (MusicInstrumentMeta) itemStack.getItemMeta();
        builder.instrument(meta.getInstrument());
        return builder;
    }

    protected static GoatHornBuilder createFromConfig(ConfigurationSection section) {
        GoatHornBuilder builder = new GoatHornBuilder();
        builder.instrument(Bukkit.getRegistry(MusicInstrument.class).get(NamespacedKey.fromString(section.getString("instrument"))));
        return builder;
    }

    @Override
    public void saveToConfig(ConfigurationSection section) {
        super.saveToConfig(section);
        section.set("instrument", this.instrument.getKey());
    }
    
    public GoatHornBuilder instrument(MusicInstrument instrument) {
        this.instrument = instrument;
        return this;
    }
    
    @Override
    public GoatHornBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        super.addAttributeModifier(attribute, name, amount, operation, slot);
        return this;
    }

    @Override
    public GoatHornBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        super.addAttributeModifier(attribute, name, amount, operation);
        return this;
    }

    @Override
    public GoatHornBuilder addEnchant(Enchantment enchantment, int level) {
        super.addEnchant(enchantment, level);
        return this;
    }

    @Override
    public GoatHornBuilder addItemFlags(ItemFlag... itemFlags) {
        super.addItemFlags(itemFlags);
        return this;
    }

    @Override
    public GoatHornBuilder setLore(List<String> lore) {
        super.setLore(lore);
        return this;
    }

    @Override
    public GoatHornBuilder addLoreLine(String line) {
        super.addLoreLine(line);
        return this;
    }

    @Override
    public GoatHornBuilder material(XMaterial material) {
        super.material(material);
        return this;
    }

    @Override
    public GoatHornBuilder amount(int amount) {
        super.amount(amount);
        return this;
    }

    @Override
    public GoatHornBuilder displayName(String displayName) {
        super.displayName(displayName);
        return this;
    }

    @Override
    public GoatHornBuilder unbreakable(boolean unbreakable) {
        super.unbreakable(unbreakable);
        return this;
    }

    @Override
    protected MusicInstrumentMeta createItemMeta() {
        MusicInstrumentMeta itemMeta = (MusicInstrumentMeta) super.createItemMeta();
        itemMeta.setInstrument(this.instrument);
        return itemMeta;
    }

    @Override
    public GoatHornBuilder clone() {
        GoatHornBuilder clone = (GoatHornBuilder) super.clone();
        clone.instrument = this.instrument;
        return clone;
    }
}

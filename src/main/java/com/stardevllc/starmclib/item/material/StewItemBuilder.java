package com.stardevllc.starmclib.item.material;

import com.cryptomorin.xseries.XMaterial;
import com.stardevllc.starmclib.item.ItemBuilder;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class StewItemBuilder extends ItemBuilder {
    
    private List<PotionEffect> effects = new ArrayList<>();
    
    public StewItemBuilder() {
        super(XMaterial.SUSPICIOUS_STEW);
    }
    
    protected static StewItemBuilder createFromItemStack(ItemStack itemStack) {
        StewItemBuilder builder = new StewItemBuilder();
        SuspiciousStewMeta meta = (SuspiciousStewMeta) itemStack.getItemMeta();
        return builder.setEffects(meta.getCustomEffects());
    }

    protected static StewItemBuilder createFromConfig(ConfigurationSection section) {
        StewItemBuilder builder = new StewItemBuilder();
        ConfigurationSection effectsSection = section.getConfigurationSection("effects");
        if (effectsSection != null) {
            for (String key : effectsSection.getKeys(false)) {
                builder.addEffect(effectsSection.getObject(key, PotionEffect.class));
            }
        }
        return builder;
    }

    @Override
    public void saveToConfig(ConfigurationSection section) {
        super.saveToConfig(section);
        for (int i = 0; i < effects.size(); i++) {
            section.set("effects." + i, effects.get(i));
        }
    }
    
    public StewItemBuilder addEffect(PotionEffect effect) {
        this.effects.add(effect);
        return this;
    }
    
    public StewItemBuilder setEffects(List<PotionEffect> effects) {
        this.effects.clear();
        this.effects.addAll(effects);
        return this;
    }
    
    @Override
    public StewItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        super.addAttributeModifier(attribute, name, amount, operation, slot);
        return this;
    }

    @Override
    public StewItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        super.addAttributeModifier(attribute, name, amount, operation);
        return this;
    }

    @Override
    public StewItemBuilder addEnchant(Enchantment enchantment, int level) {
        super.addEnchant(enchantment, level);
        return this;
    }

    @Override
    public StewItemBuilder addItemFlags(ItemFlag... itemFlags) {
        super.addItemFlags(itemFlags);
        return this;
    }

    @Override
    public StewItemBuilder setLore(List<String> lore) {
        super.setLore(lore);
        return this;
    }

    @Override
    public StewItemBuilder addLoreLine(String line) {
        super.addLoreLine(line);
        return this;
    }

    @Override
    public StewItemBuilder material(XMaterial material) {
        super.material(material);
        return this;
    }

    @Override
    public StewItemBuilder amount(int amount) {
        super.amount(amount);
        return this;
    }

    @Override
    public StewItemBuilder displayName(String displayName) {
        super.displayName(displayName);
        return this;
    }

    @Override
    public StewItemBuilder unbreakable(boolean unbreakable) {
        super.unbreakable(unbreakable);
        return this;
    }

    @Override
    protected SuspiciousStewMeta createItemMeta() {
        SuspiciousStewMeta itemMeta = (SuspiciousStewMeta) super.createItemMeta();
        this.effects.forEach(effect -> itemMeta.addCustomEffect(effect, true));
        return itemMeta;
    }

    @Override
    public StewItemBuilder clone() {
        StewItemBuilder clone = (StewItemBuilder) super.clone();
        clone.effects.addAll(this.effects);
        return clone;
    }
}

package com.stardevllc.starmclib.item.material;

import com.cryptomorin.xseries.XMaterial;
import com.stardevllc.starmclib.item.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;

import java.util.List;

public class FishBucketBuilder extends ItemBuilder {
    
    private DyeColor bodyColor = DyeColor.BLACK;
    private TropicalFish.Pattern pattern = TropicalFish.Pattern.BLOCKFISH;
    private DyeColor patternColor = DyeColor.WHITE;
    
    public FishBucketBuilder() {
        super(XMaterial.TROPICAL_FISH_BUCKET);
    }
    
    protected static FishBucketBuilder createFromItemStack(ItemStack itemStack) {
        FishBucketBuilder builder = new FishBucketBuilder();
        TropicalFishBucketMeta meta = (TropicalFishBucketMeta) itemStack.getItemMeta();
        builder.bodyColor(meta.getBodyColor()).pattern(meta.getPattern()).patternColor(meta.getPatternColor());
        return builder;
    }

    protected static FishBucketBuilder createFromConfig(ConfigurationSection section) {
        FishBucketBuilder builder = new FishBucketBuilder();
        builder.bodyColor(DyeColor.valueOf(section.getString("bodycolor")));
        builder.pattern(TropicalFish.Pattern.valueOf("pattern"));
        builder.patternColor(DyeColor.valueOf("patterncolor"));
        return builder;
    }

    @Override
    public void saveToConfig(ConfigurationSection section) {
        super.saveToConfig(section);
        section.set("bodycolor", this.bodyColor.name());
        section.set("pattern", this.pattern.name());
        section.set("patterncolor", this.patternColor.name());
    }
    
    public FishBucketBuilder bodyColor(DyeColor color) {
        this.bodyColor = color;
        return this;
    }
    
    public FishBucketBuilder pattern(TropicalFish.Pattern pattern) {
        this.pattern = pattern;
        return this;
    }
    
    public FishBucketBuilder patternColor(DyeColor color) {
        this.patternColor = color;
        return this;
    }
    
    @Override
    public FishBucketBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        super.addAttributeModifier(attribute, name, amount, operation, slot);
        return this;
    }

    @Override
    public FishBucketBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        super.addAttributeModifier(attribute, name, amount, operation);
        return this;
    }

    @Override
    public FishBucketBuilder addEnchant(Enchantment enchantment, int level) {
        super.addEnchant(enchantment, level);
        return this;
    }

    @Override
    public FishBucketBuilder addItemFlags(ItemFlag... itemFlags) {
        super.addItemFlags(itemFlags);
        return this;
    }

    @Override
    public FishBucketBuilder setLore(List<String> lore) {
        super.setLore(lore);
        return this;
    }

    @Override
    public FishBucketBuilder addLoreLine(String line) {
        super.addLoreLine(line);
        return this;
    }

    @Override
    public FishBucketBuilder material(XMaterial material) {
        super.material(material);
        return this;
    }

    @Override
    public FishBucketBuilder amount(int amount) {
        super.amount(amount);
        return this;
    }

    @Override
    public FishBucketBuilder displayName(String displayName) {
        super.displayName(displayName);
        return this;
    }

    @Override
    public FishBucketBuilder unbreakable(boolean unbreakable) {
        super.unbreakable(unbreakable);
        return this;
    }

    @Override
    protected TropicalFishBucketMeta createItemMeta() {
        TropicalFishBucketMeta itemMeta = (TropicalFishBucketMeta) super.createItemMeta();
        itemMeta.setBodyColor(this.bodyColor);
        itemMeta.setPattern(this.pattern);
        itemMeta.setPatternColor(this.patternColor);
        return itemMeta;
    }

    @Override
    public FishBucketBuilder clone() {
        FishBucketBuilder clone = (FishBucketBuilder) super.clone();
        clone.bodyColor = this.bodyColor;
        clone.pattern = this.pattern;
        clone.patternColor = this.patternColor;
        return clone;
    }
}

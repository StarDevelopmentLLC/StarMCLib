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
import org.bukkit.inventory.meta.CrossbowMeta;

import java.util.LinkedList;
import java.util.List;

public class CrossbowItemBuilder extends ItemBuilder {
    
    private List<ItemStack> projectiles = new LinkedList<>();
    
    public CrossbowItemBuilder() {
        super(XMaterial.CROSSBOW);
    }
    
    protected static CrossbowItemBuilder createFromItemStack(ItemStack itemStack) {
        CrossbowItemBuilder itemBuilder = new CrossbowItemBuilder();
        CrossbowMeta crossbowMeta = (CrossbowMeta) itemStack.getItemMeta();
        itemBuilder.setProjectiles(crossbowMeta.getChargedProjectiles());
        return itemBuilder;
    }

    protected static CrossbowItemBuilder createFromConfig(ConfigurationSection section) {
        CrossbowItemBuilder builder = new CrossbowItemBuilder();
        ConfigurationSection projectilesSection = section.getConfigurationSection("projectiles");
        if (projectilesSection != null) {
            for (String key : projectilesSection.getKeys(false)) {
                builder.addProjectile(projectilesSection.getItemStack(key));
            }
        }
        return builder;
    }

    @Override
    public void saveToConfig(ConfigurationSection section) {
        super.saveToConfig(section);
        for (int i = 0; i < projectiles.size(); i++) {
            section.set("projectiles." + i, projectiles.get(i));   
        }
    }
    
    public CrossbowItemBuilder addProjectile(ItemStack projectile) {
        this.projectiles.add(projectile);
        return this;
    }
    
    public CrossbowItemBuilder setProjectiles(List<ItemStack> projectiles) {
        this.projectiles.clear();
        this.projectiles.addAll(projectiles);
        return this;
    }
    
    @Override
    public CrossbowItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        super.addAttributeModifier(attribute, name, amount, operation, slot);
        return this;
    }

    @Override
    public CrossbowItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        super.addAttributeModifier(attribute, name, amount, operation);
        return this;
    }

    @Override
    public CrossbowItemBuilder addEnchant(Enchantment enchantment, int level) {
        super.addEnchant(enchantment, level);
        return this;
    }

    @Override
    public CrossbowItemBuilder addItemFlags(ItemFlag... itemFlags) {
        super.addItemFlags(itemFlags);
        return this;
    }

    @Override
    public CrossbowItemBuilder setLore(List<String> lore) {
        super.setLore(lore);
        return this;
    }

    @Override
    public CrossbowItemBuilder addLoreLine(String line) {
        super.addLoreLine(line);
        return this;
    }

    @Override
    public CrossbowItemBuilder material(XMaterial material) {
        super.material(material);
        return this;
    }

    @Override
    public CrossbowItemBuilder amount(int amount) {
        super.amount(amount);
        return this;
    }

    @Override
    public CrossbowItemBuilder displayName(String displayName) {
        super.displayName(displayName);
        return this;
    }

    @Override
    public CrossbowItemBuilder unbreakable(boolean unbreakable) {
        super.unbreakable(unbreakable);
        return this;
    }

    @Override
    protected CrossbowMeta createItemMeta() {
        CrossbowMeta itemMeta = (CrossbowMeta) super.createItemMeta();
        itemMeta.setChargedProjectiles(this.projectiles);
        return itemMeta;
    }

    @Override
    public CrossbowItemBuilder clone() {
        CrossbowItemBuilder clone = (CrossbowItemBuilder) super.clone();
        clone.projectiles.addAll(this.projectiles);
        return clone;
    }
}

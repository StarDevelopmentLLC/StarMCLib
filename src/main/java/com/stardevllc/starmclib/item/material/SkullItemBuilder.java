package com.stardevllc.starmclib.item.material;

import com.cryptomorin.xseries.XMaterial;
import com.stardevllc.starmclib.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.List;
import java.util.UUID;

public class SkullItemBuilder extends ItemBuilder {
    
    private UUID owner;
    private PlayerProfile profile;
    private NamespacedKey noteblockSound;
    
    public SkullItemBuilder() {
        super(XMaterial.PLAYER_HEAD);
    }
    
    protected static SkullItemBuilder createFromItemStack(ItemStack itemStack) {
        SkullItemBuilder builder = new SkullItemBuilder();
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        builder.profile(meta.getOwnerProfile());
        if (meta.getOwningPlayer() != null) {
            builder.owner(meta.getOwningPlayer().getUniqueId());
        }
        return builder;
    }

    protected static SkullItemBuilder createFromConfig(ConfigurationSection section) {
        SkullItemBuilder builder = new SkullItemBuilder();
        if (section.contains("noteblocksound")) {
            builder.noteblockSound(NamespacedKey.fromString(section.getString("noteblocksound")));
        }
        
        if (section.contains("owner")) {
            builder.owner(UUID.fromString(section.getString("owner")));
        }
        
        if (section.contains("profile")) {
            builder.profile(section.getObject("profile", PlayerProfile.class));
        }
        return builder;
    }

    @Override
    public void saveToConfig(ConfigurationSection section) {
        super.saveToConfig(section);
        if (noteblockSound != null) {
            section.set("noteblocksound", noteblockSound);
        }
        if (owner != null) {
            section.set("owner", owner.toString());
        }
        
        if (profile != null) {
            section.set("profile", profile);
        }
    }
    
    public SkullItemBuilder owner(UUID owner) {
        this.owner = owner;
        return this;
    }
    
    public SkullItemBuilder profile(PlayerProfile profile) {
        this.profile = profile;
        return this;
    }
    
    public SkullItemBuilder noteblockSound(NamespacedKey key) {
        this.noteblockSound = key;
        return this;
    }
    
    @Override
    public SkullItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        super.addAttributeModifier(attribute, name, amount, operation, slot);
        return this;
    }

    @Override
    public SkullItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        super.addAttributeModifier(attribute, name, amount, operation);
        return this;
    }

    @Override
    public SkullItemBuilder addEnchant(Enchantment enchantment, int level) {
        super.addEnchant(enchantment, level);
        return this;
    }

    @Override
    public SkullItemBuilder addItemFlags(ItemFlag... itemFlags) {
        super.addItemFlags(itemFlags);
        return this;
    }

    @Override
    public SkullItemBuilder setLore(List<String> lore) {
        super.setLore(lore);
        return this;
    }

    @Override
    public SkullItemBuilder addLoreLine(String line) {
        super.addLoreLine(line);
        return this;
    }

    @Override
    public SkullItemBuilder material(XMaterial material) {
        super.material(material);
        return this;
    }

    @Override
    public SkullItemBuilder amount(int amount) {
        super.amount(amount);
        return this;
    }

    @Override
    public SkullItemBuilder displayName(String displayName) {
        super.displayName(displayName);
        return this;
    }

    @Override
    public SkullItemBuilder unbreakable(boolean unbreakable) {
        super.unbreakable(unbreakable);
        return this;
    }

    @Override
    protected SkullMeta createItemMeta() {
        SkullMeta itemMeta = (SkullMeta) super.createItemMeta();
        if (this.owner != null) {
            itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(this.owner));
        }
        
        if (this.profile != null) {
            itemMeta.setOwnerProfile(this.profile);
        }
        
        if (this.noteblockSound != null) {
            itemMeta.setNoteBlockSound(this.noteblockSound);
        }
        return itemMeta;
    }

    @Override
    public SkullItemBuilder clone() {
        SkullItemBuilder clone = (SkullItemBuilder) super.clone();
        clone.owner = this.owner;
        clone.profile = this.profile;
        return clone;
    }
}

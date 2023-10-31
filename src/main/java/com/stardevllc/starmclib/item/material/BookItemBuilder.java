package com.stardevllc.starmclib.item.material;

import com.cryptomorin.xseries.XMaterial;
import com.stardevllc.starmclib.color.ColorUtils;
import com.stardevllc.starmclib.item.ItemBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.LinkedList;
import java.util.List;

public class BookItemBuilder extends ItemBuilder {
    
    private String author;
    private List<BaseComponent[]> pages = new LinkedList<>();
    private BookMeta.Generation generation = BookMeta.Generation.ORIGINAL;
    private String title;
    
    public BookItemBuilder(XMaterial material) {
        super(material);
    }
    
    protected BookItemBuilder() {
        
    }

    protected static BookItemBuilder createFromItemStack(ItemStack itemStack) {
        BookItemBuilder itemBuilder = new BookItemBuilder();
        BookMeta itemMeta = (BookMeta) itemStack.getItemMeta();
        itemBuilder.setPagesComponents(itemMeta.spigot().getPages()).author(itemMeta.getAuthor()).title(itemMeta.getTitle()).generation(itemMeta.getGeneration());
        return itemBuilder;
    }

    protected static BookItemBuilder createFromConfig(ConfigurationSection section) {
        BookItemBuilder builder = new BookItemBuilder();
        builder.title(section.getString("title"));
        builder.author(section.getString("author"));
        builder.generation(BookMeta.Generation.valueOf(section.getString("generation")));
        ConfigurationSection pagesSection = section.getConfigurationSection("pages");
        if (pagesSection != null) {
            for (String key : pagesSection.getKeys(false)) {
                builder.addPage(ComponentSerializer.parse(pagesSection.getString(key)));
            }
        }
        return builder;
    }

    @Override
    public void saveToConfig(ConfigurationSection section) {
        super.saveToConfig(section);
        section.set("author", this.author);
        section.set("title", this.title);
        section.set("generation", this.generation.name());
        for (int i = 0; i < pages.size(); i++) {
            section.set("pages." + i, ComponentSerializer.toString(pages.get(i)));
        }
    }
    
    public BookItemBuilder addPage(String page) {
        this.pages.add(TextComponent.fromLegacyText(ColorUtils.color(page)));
        return this;
    }
    
    public BookItemBuilder setPages(List<String> pages) {
        this.pages.clear();
        pages.forEach(page -> this.pages.add(TextComponent.fromLegacyText(ColorUtils.color(page))));
        return this;
    }
    
    public BookItemBuilder addPage(BaseComponent[] component) {
        this.pages.add(component);
        return this;
    }
    
    public BookItemBuilder setPagesComponents(List<BaseComponent[]> components) {
        this.pages.clear();
        this.pages.addAll(components);
        return this;
    }
    
    public BookItemBuilder author(String author) {
        this.author = author;
        return this;
    }
    
    public BookItemBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    public BookItemBuilder generation(BookMeta.Generation generation) {
        this.generation = generation;
        return this;
    }
    
    @Override
    public BookItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        super.addAttributeModifier(attribute, name, amount, operation, slot);
        return this;
    }

    @Override
    public BookItemBuilder addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        super.addAttributeModifier(attribute, name, amount, operation);
        return this;
    }

    @Override
    public BookItemBuilder addEnchant(Enchantment enchantment, int level) {
        super.addEnchant(enchantment, level);
        return this;
    }

    @Override
    public BookItemBuilder addItemFlags(ItemFlag... itemFlags) {
        super.addItemFlags(itemFlags);
        return this;
    }

    @Override
    public BookItemBuilder setLore(List<String> lore) {
        super.setLore(lore);
        return this;
    }

    @Override
    public BookItemBuilder addLoreLine(String line) {
        super.addLoreLine(line);
        return this;
    }

    @Override
    public BookItemBuilder material(XMaterial material) {
        super.material(material);
        return this;
    }

    @Override
    public BookItemBuilder amount(int amount) {
        super.amount(amount);
        return this;
    }

    @Override
    public BookItemBuilder displayName(String displayName) {
        super.displayName(displayName);
        return this;
    }

    @Override
    public BookItemBuilder unbreakable(boolean unbreakable) {
        super.unbreakable(unbreakable);
        return this;
    }

    @Override
    protected BookMeta createItemMeta() {
        BookMeta itemMeta = (BookMeta) super.createItemMeta();
        itemMeta.setTitle(ColorUtils.color(this.title));
        itemMeta.setAuthor(ColorUtils.color(this.author));
        itemMeta.setGeneration(generation);
        itemMeta.spigot().setPages(this.pages);
        return itemMeta;
    }

    @Override
    public BookItemBuilder clone() {
        BookItemBuilder clone = (BookItemBuilder) super.clone();
        clone.title = this.title;
        clone.author = this.author;
        clone.pages.addAll(this.pages);
        return clone;
    }
}

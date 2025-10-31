package com.stardevllc.starmclib;

import com.stardevllc.smcversion.MinecraftVersion;
import com.stardevllc.starlib.reflection.ReflectionHelper;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.CharacterAndFormat;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.Builder;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class StarColorsV2 {
    private JavaPlugin plugin;
    private BukkitAudiences audiences;
    private LegacyComponentSerializer ampersandLegacy;
    private LegacyComponentSerializer sectionLegacy;
    private MiniMessage miniMessage;
    
    private Map<CharacterAndFormat, String> formattingPermissions = new HashMap<>();
    
    public StarColorsV2(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void init() {
        this.audiences = BukkitAudiences.create(plugin);
        
        Builder ampersandBuilder = LegacyComponentSerializer.builder().character('&').extractUrls();
        if (MinecraftVersion.CURRENT_VERSION.ordinal() >= MinecraftVersion.v1_16.ordinal()) {
            ampersandBuilder.hexColors();
        }
        ampersandLegacy = ampersandBuilder.build();
        
        Builder sectionBuilder = LegacyComponentSerializer.builder().character('§').extractUrls();
        if (MinecraftVersion.CURRENT_VERSION.ordinal() >= MinecraftVersion.v1_16.ordinal()) {
            sectionBuilder.hexColors();
        }
        sectionLegacy = sectionBuilder.build();
        
        this.miniMessage = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.defaults())
                        .build()
                )
                .build();
        
        Map<String, Field> colorsFields = ReflectionHelper.getClassFields(CharacterAndFormat.class);
        for (Field colorsField : colorsFields.values()) {
            try {
                CharacterAndFormat value = (CharacterAndFormat) colorsField.get(null);
                String permission = "starcore.colors." + colorsField.getName().toLowerCase();
                this.formattingPermissions.put(value, permission);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Could not get color values", e);
            }
        }
    }
    
    public void coloredMessage(CommandSender sender, Component component) {
        Audience audience = audiences.sender(sender);
        audience.sendMessage(component);
    }
    
    public void coloredLegacy(CommandSender sender, String text) {
        Audience audience = audiences.sender(sender);
        audience.sendMessage(ampersandLegacy.deserialize(text)); 
    }
    
    public void coloredMini(CommandSender sender, String text) {
        Audience audience = audiences.sender(sender);
        audience.sendMessage(miniMessage.deserialize(text));
    }
    
    public String colorMini(String text) {
        return sectionLegacy.serialize(miniMessage.deserialize(text));
    }
    
    public String colorLegacy(String text) {
        return sectionLegacy.serialize(ampersandLegacy.deserialize(text));
    }
    
    public String colorLegacy(CommandSender sender, String text) {
        Builder ampersandLegacy = LegacyComponentSerializer.builder().character('&');
        List<CharacterAndFormat> allowedFormats = new ArrayList<>();
        this.formattingPermissions.forEach((color, perm) -> {
            if (sender.hasPermission(perm)) {
                allowedFormats.add(color);
            }
        });
        ampersandLegacy.formats(allowedFormats);
        
        return sectionLegacy.serialize(ampersandLegacy.build().deserialize(text));
    }
    
    public JavaPlugin getPlugin() {
        return plugin;
    }
    
    public BukkitAudiences getAudiences() {
        return audiences;
    }
    
    public LegacyComponentSerializer getAmpersandLegacy() {
        return ampersandLegacy;
    }
    
    public LegacyComponentSerializer getSectionLegacy() {
        return sectionLegacy;
    }
    
    public MiniMessage getMiniMessage() {
        return miniMessage;
    }
}
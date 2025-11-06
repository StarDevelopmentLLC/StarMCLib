package com.stardevllc.starmclib;

import com.stardevllc.smcversion.MinecraftVersion;
import com.stardevllc.starlib.reflection.ReflectionHelper;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.CharacterAndFormat;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.Builder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
    private BungeeComponentSerializer bungeeSerializer = BungeeComponentSerializer.get();
    
    private Map<CharacterAndFormat, String> formattingPermissions = new HashMap<>();
    
    public StarColorsV2(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void init() {
        /* TODO
            There is some issues with sending components through the Audience interface directly 
            Using the Bungee serializer to convert to that in order for it to actually work
         */
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
                String permission = plugin.getName().toLowerCase() + ".colors." + colorsField.getName().toLowerCase();
                this.formattingPermissions.put(value, permission);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Could not get color values", e);
            }
        }
    }
    
    public void send(CommandSender sender, Component component) {
        if (component == null) {
            return;
        }
        
        if (sender instanceof Player player) {
            player.spigot().sendMessage(bungeeSerializer.serialize(component));
        } else {
            sender.sendMessage(sectionLegacy.serialize(component));
        }
    }
    
    public void coloredMessage(CommandSender sender, Component component) {
        send(sender, component);
    }
    
    public void coloredLegacy(CommandSender sender, String text) {
        send(sender, colorLegacy(text));
    }
    
    public void coloredMini(CommandSender sender, String text) {
        send(sender, miniMessage.deserialize(text));
    }
    
    public Component colorMini(String text) {
        return miniMessage.deserialize(text);
    }
    
    public Component colorLegacy(String text) {
        return ampersandLegacy.deserialize(text);
    }
    
    public Component colorLegacy(CommandSender sender, String text) {
        Builder ampersandLegacy = LegacyComponentSerializer.builder().character('&');
        List<CharacterAndFormat> allowedFormats = new ArrayList<>();
        this.formattingPermissions.forEach((color, perm) -> {
            if (sender.hasPermission(perm)) {
                allowedFormats.add(color);
            }
        });
        ampersandLegacy.formats(allowedFormats);
        
        return ampersandLegacy.build().deserialize(text);
    }
    
    public JavaPlugin getPlugin() {
        return plugin;
    }
    
    public BukkitAudiences getAudiences() {
        return audiences;
    }
    
    public BungeeComponentSerializer getBungeeSerializer() {
        return bungeeSerializer;
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
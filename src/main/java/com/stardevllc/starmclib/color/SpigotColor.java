package com.stardevllc.starmclib.color;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

class SpigotColor extends CustomColor {
    public SpigotColor(JavaPlugin owner, ChatColor chatColor) {
        super(owner);
        this.symbol = '&';
        this.code = chatColor.getChar();
        this.permission = "starcore.color.spigot." + chatColor.name().toLowerCase();
        this.spigotColor = chatColor.asBungee();
        calculateFields();
    }

    @Override
    public CustomColor symbolCode(String symbolCode) {
        return this;
    }

    @Override
    public CustomColor hexValue(String hex) {
        return this;
    }

    @Override
    public CustomColor rgbValue(int red, int green, int blue) {
        return this;
    }

    @Override
    public CustomColor spigotColor(net.md_5.bungee.api.ChatColor color) {
        return this;
    }

    @Override
    public CustomColor awtColor(Color color) {
        return this;
    }

    @Override
    public CustomColor permission(String permission) {
        return this;
    }
}

package com.stardevllc.starmclib.color;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.stream.IntStream;

/**
 * This class represents a custom color value <br>
 * This is created using builder-style methods <br>
 * You only need to call one of the following {@link #awtColor(Color)}, {@link #hexValue(String)}, {@link #rgbValue(int, int, int)} or {@link #spigotColor(ChatColor)}. The other values will be populated automatically. <br>
 */
public class CustomColor {
    protected final JavaPlugin owner;
    protected char symbol, code;
    protected int red = -1, green = -1, blue = -1;
    protected String hex, permission = "";

    protected ChatColor spigotColor;
    protected Color awtColor;

    /**
     * Constructs a new color
     * @param owner The owner plugin.
     */
    public CustomColor(JavaPlugin owner) {
        this.owner = owner;
    }

    /**
     * Sets the symbol code for this color
     * @param symbolCode The 2 character code. See the {@link ColorUtils#COLOR_CODE_PATTERN} for valid values. Just like normal chat colors, but with custom characters.
     * @return Builder style instance
     */
    public CustomColor symbolCode(String symbolCode) {
        if (ColorUtils.isValidCode(symbolCode)) {
            this.symbol = symbolCode.charAt(0);
            this.code = symbolCode.charAt(1);
        }
        return this;
    }

    /**
     * Set the hex value. This will populate the other values stored as well
     * @param hex The color value in hex format.
     * @return Builder style instance
     */
    public CustomColor hexValue(String hex) {
        if (ColorUtils.isValidHex(hex)) {
            this.hex = hex;
            calculateFields();
        }
        return this;
    }

    /**
     * Sets the color value using RGB values
     * @param red The red value between 0 and 255
     * @param green The green value between 0 and 255
     * @param blue The blue value between 0 and 255
     * @return Builder style instance
     */
    public CustomColor rgbValue(int red, int green, int blue) {
        if (IntStream.of(red, green, blue).allMatch(ColorUtils::isRGBComponentInRange)) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            calculateFields();
        }

        return this;
    }

    /**
     * @return The user-friendly chat code of this color
     */
    public String getChatCode() {
        return this.symbol + "" + this.code;
    }

    /**
     * Sets the color value based on the BungeeCord ChatColor enum
     * @param color The color
     * @return Builder style instance
     */
    public CustomColor spigotColor(ChatColor color) {
        this.spigotColor = color;
        calculateFields();
        return this;
    }

    /**
     * Sets the color value based on the Java AWT color
     * @param color The awt color
     * @return Builder style instance
     */
    public CustomColor awtColor(Color color) {
        this.awtColor = color;
        calculateFields();
        return this;
    }

    /**
     * Sets the permission for this color (Only works with calls to the methods that take in a {@link CommandSender}
     * @param permission The permission
     * @return Builder style instance
     */
    public CustomColor permission(String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * @return The owning plugin
     */
    public JavaPlugin getOwner() {
        return owner;
    }

    /**
     * @return The RED value
     */
    public int getRed() {
        return red;
    }

    /**
     * @return The GREEN value
     */
    public int getGreen() {
        return green;
    }

    /**
     * @return The BLUE value
     */
    public int getBlue() {
        return blue;
    }

    /**
     * @return The HEX value
     */
    public String getHex() {
        return hex;
    }

    /**
     * @return The BungeeCord ChatColor value
     */
    public ChatColor getSpigotColor() {
        return spigotColor;
    }

    /**
     * @return The Java AWT color value
     */
    public Color getAwtColor() {
        return awtColor;
    }

    protected void calculateFields() {
        if (ColorUtils.isValidHex(this.hex) && ColorUtils.isHexSupported()) {
            this.awtColor = Color.decode(hex);
            assignRGB();
            this.spigotColor = ChatColor.of(awtColor);
        } else if (red != -1 && blue != -1 && green != -1) {
            this.awtColor = new Color(this.red, this.green, this.blue);
            this.spigotColor = ChatColor.of(awtColor);
        } else if (spigotColor != null) {
            this.awtColor = spigotColor.getColor();
            this.hex = ColorUtils.getHexCode(spigotColor);
            assignRGB();
        } else if (awtColor != null) {
            assignRGB();
            this.spigotColor = ChatColor.of(awtColor);
            this.hex = ColorUtils.getHexCode(spigotColor);
        }
    }

    /**
     * @return The prefix symbol
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * @return The code used after the prefix symbol
     */
    public char getCode() {
        return code;
    }

    /**
     * @return The permission needed for this color.
     */
    public String getPermission() {
        return permission;
    }

    private void assignRGB() {
        if (this.awtColor != null) {
            this.red = awtColor.getRed();
            this.green = awtColor.getGreen();
            this.blue = awtColor.getBlue();
        }
    }
}

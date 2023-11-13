package com.stardevllc.starmclib.color;

import com.stardevllc.starmclib.NMSVersion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A collection of color utilities to support adding custom color codes that can be used. <br>
 * Developers must use ColorUtils.color() method in order for custom color codes to be respected within this API
 */
public final class ColorUtils {
    private static final char[] COLOR_SYMBOLS = {'&', '~', '`', '!', '@', '$', '%', '^', '*', '?'};
    public static final Pattern COLOR_CODE_PATTERN = Pattern.compile("[&~`!@$%^*?][0-9A-Z]", Pattern.CASE_INSENSITIVE);
    public static final Pattern HEX_VALUE_PATTERN = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", Pattern.CASE_INSENSITIVE);

    private static Map<String, CustomColor> customColors = new HashMap<>();
    private static Map<String, SpigotColor> spigotColors = new HashMap<>();

    static {
        for (org.bukkit.ChatColor chatColor : org.bukkit.ChatColor.values()) {
            SpigotColor spigotColor = new SpigotColor(null, chatColor);
            spigotColors.put(spigotColor.getChatCode(), spigotColor);
        }
    }
    
    public static void coloredMessage(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    /**
     * Colors a string with all options set to true using the {@link #color(String, boolean, boolean, boolean)} method
     *
     * @param uncolored The uncolored text
     * @return The colored text
     */
    public static String color(String uncolored) {
        return color(null, uncolored);
    }

    /**
     * Colors the string with all options set to true using the {@link #color(CommandSender, String, boolean, boolean, boolean)} method and added permission checks
     *
     * @param sender    The sender for permission checks
     * @param uncolored The uncolored text
     * @return The colored text
     */
    public static String color(CommandSender sender, String uncolored) {
        return color(sender, uncolored, true, true, isHexSupported());
    }

    /**
     * Colors a String based on settings
     *
     * @param uncolored       The uncolored text
     * @param translateBukkit If Default Bukkit Color Codes are translated
     * @param translateCustom If custom color codes are translated
     * @param translateHex    If direct hex codes are translated (Note: Hex codes MUST be 6 characters long), will also prevent hex being translated if current Bukkit Version does not support Hex Codes
     * @return The colored text
     */
    public static String color(String uncolored, boolean translateBukkit, boolean translateCustom, boolean translateHex) {
        return color(null, uncolored, translateBukkit, translateCustom, isHexSupported() && translateHex);
    }

    /**
     * Translates default bukkit colors without fine-grained permissions
     *
     * @param uncolored The uncolored text
     * @return The colored text
     */
    public static String translateBukkit(String uncolored) {
        return translateBukkit(null, uncolored);
    }

    /**
     * Colors default bukkit text with fine-grain permissions
     *
     * @param sender    The sender (Can be null)
     * @param uncolored The uncolored text
     * @return The colored text
     */
    public static String translateBukkit(CommandSender sender, String uncolored) {
        if (sender == null || sender.isOp()) {
            return ChatColor.translateAlternateColorCodes('&', uncolored);
        }

        StringBuilder colored = new StringBuilder();
        for (int i = 0; i < uncolored.length(); i++) {
            char c = uncolored.charAt(i);
            if (c == '&') {
                if (uncolored.length() >= i + 1) {
                    String code = "&" + uncolored.charAt(i + 1);
                    SpigotColor spigotColor = spigotColors.get(code.toLowerCase());
                    if (spigotColor != null && hasPermission(sender, spigotColor.getPermission())) {
                        colored.append(ChatColor.COLOR_CHAR);
                    } else {
                        colored.append('&');
                    }
                }
            } else {
                colored.append(c);
            }
        }

        return colored.toString();
    }

    /**
     * Translates custom color codes
     *
     * @param uncolored The uncolored text
     * @return The colored text
     */
    public static String translateCustom(String uncolored) {
        return translateCustom(null, uncolored);
    }

    /**
     * Translates custom color codes
     * 
     * @param sender The sender
     * @param uncolored The uncolored text
     * @return The colored text
     */
    public static String translateCustom(CommandSender sender, String uncolored) {
        StringBuilder colored = new StringBuilder();
        for (int i = 0; i < uncolored.length(); i++) {
            char c = uncolored.charAt(i);
            if (ColorUtils.isValidSymbol(c)) {
                if (uncolored.length() > i + 1) {
                    String code = String.valueOf(c) + uncolored.charAt(i + 1);
                    CustomColor color = ColorUtils.getCustomColor(code);
                    if (color != null && hasPermission(sender, color.getPermission())) {
                        colored.append(color.getSpigotColor());
                        i++;
                        continue;
                    }
                }
            }
            colored.append(c);
        }

        return colored.toString();
    }

    /**
     * Translates HEX codes
     *
     * @param uncolored The uncolored text
     * @return The colored text
     */
    public static String translateHex(String uncolored) {
        return translateHex(null, uncolored);
    }
    
    public static boolean isHexSupported() {
        return NMSVersion.CURRENT_VERSION.ordinal() >= NMSVersion.v1_16_R1.ordinal();
    }
    
    /**
     * Translates HEX colors
     *
     * @param sender The sender
     * @param uncolored The uncolored text
     * @return The colored text
     */
    public static String translateHex(CommandSender sender, String uncolored) {
        StringBuilder colored = new StringBuilder();
        if (!isHexSupported()) {
            Bukkit.getLogger().warning("[StarMCLib] Hex Colors are not supported by this Minecraft Version, ignoring them.");
            return uncolored;
        }
        for (int i = 0; i < uncolored.length(); i++) {
            char c = uncolored.charAt(i);
            if (c == '#') {
                if (hasPermission(sender, "starmclib.color.hex")) {
                    if (uncolored.length() > i + 6) {
                        String colorCode = '#' + uncolored.substring(i + 1, i + 7);
                        ChatColor color = ChatColor.of(colorCode);
                        colored.append(color);
                        i += 6;
                        continue;
                    }
                }
            }
            colored.append(c);
        }
        return colored.toString();
    }

    /**
     * Colors a String based on settings and with permission checks if sender is not null
     *
     * @param sender          The sender to check for permission. This can be null though and the permission check is skipped
     * @param uncolored       The uncolored text
     * @param translateBukkit If Default Bukkit Color Codes are translated
     * @param translateCustom If custom color codes are translated
     * @param translateHex    If direct hex codes are translated (Note: Hex codes MUST be 6 characters long)
     * @return The colored text
     */
    public static String color(CommandSender sender, String uncolored, boolean translateBukkit, boolean translateCustom, boolean translateHex) {
        String text = uncolored;
        if (translateBukkit) {
            text = translateBukkit(sender, text);
        }

        if (translateCustom) {
            text = translateCustom(sender, text);
        }

        if (translateHex) {
            return translateHex(sender, text);
        }

        return text;
    }

    private static boolean hasPermission(CommandSender sender, String permission) {
        if (sender == null || permission == null || permission.isEmpty()) {
            return true;
        }

        return sender.hasPermission(permission);
    }

    /**
     * @param color The color
     * @return The hex code
     */
    public static String getHexCode(ChatColor color) {
        Color awtColor = color.getColor();
        if (awtColor != null) {
            return "#" + Integer.toHexString(awtColor.getRGB()).substring(2);
        }
        return "";
    }

    /**
     * Utility method to check to see if an RGB component is valid
     *
     * @param component The r g b value
     * @return If it is between 0 and 255
     */
    public static boolean isRGBComponentInRange(int component) {
        return component > -1 && component < 256;
    }

    /**
     * Utility method to check for if the code matches the pattern
     *
     * @param code The code (can be null)
     * @return If the code is valid according to the pattern
     */
    public static boolean isValidCode(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        return COLOR_CODE_PATTERN.matcher(code).matches();
    }

    /**
     * Utility method to check for valid HEX values
     *
     * @param str The string to check
     * @return If the string matches the HEX pattern
     */
    public static boolean isValidHex(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return HEX_VALUE_PATTERN.matcher(str).matches();
    }

    /**
     * Adds a custom color
     *
     * @param customColor The color to add
     */
    public static void addCustomColor(CustomColor customColor) {
        customColors.put(customColor.getChatCode(), customColor);
    }

    /**
     * Gets a custom color
     *
     * @param code The code as if it were to be used in chat
     * @return The CustomColor instance
     */
    public static CustomColor getCustomColor(String code) {
        return customColors.get(code);
    }

    /**
     * Removes a color
     *
     * @param code The code of the color to remove
     */
    public static void removeColor(String code) {
        customColors.remove(code);
    }

    /**
     * Utility method to check to see if the char is a valid prefix symbol
     *
     * @param c The character
     * @return if it is valid
     */
    public static boolean isValidSymbol(char c) {
        for (char colorChar : COLOR_SYMBOLS) {
            if (colorChar == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return All valid prefix symbols
     */
    public static char[] getPrefixSymbols() {
        return COLOR_SYMBOLS;
    }

    /**
     * @return A copy of the color registry
     */
    public static Map<String, CustomColor> getCustomColors() {
        return new HashMap<>(customColors);
    }

    /**
     * Strips colors from text
     *
     * @param text The text
     * @return The uncolored text
     */
    public static String stripColor(String text) {
        text = ChatColor.stripColor(text);
        text = HEX_VALUE_PATTERN.matcher(text).replaceAll("");
        return COLOR_CODE_PATTERN.matcher(text).replaceAll("");
    }

    public static boolean isSpigotColor(String code) {
        return spigotColors.containsKey(code.toLowerCase());
    }
}

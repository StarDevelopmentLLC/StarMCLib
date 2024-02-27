---
hide:
  - navigation
  - toc
---

# ColorUtils class
This class is a utility class that only has static methods.  
By default, you can translate normal spigot colors and Hex colors using the `ColorUtils.color(String)` method, You can also take advantage of permissions by passing in a `CommandSender` before the message argument.  
The permissions are `starmclib.color.spigot.<colorname>` and `starmclib.color.hex` for spigot and hex colors respectively. If you want more precise control over hex colors, you must take advantage of custom colors.  
The `coloredMessage` method will send the message to the sender provided after coloring it.

## Custom Colors
Custom colors are available on all versions that StarMCLib support, however only HEX colors are supported on 1.16 and higher.  
Custom colors allow for developers to define their own shortcuts to colors, and you aren't restricted to just the `&` symbol either. You can fnd the supported prefix symbols in the char array called `COLOR_SYMBOLS`.
```java
CustomColor customColor = new CustomColor(this).symbolCode("&s").hexValue("#FF00AA").permission("test.color.testcolor");
ColorUtils.addCustomColor(customColor);
```
You can omit the `permission` call if you do not want it to be permission controlled.  
So all you do is you create an instance of the CustomColor class providing your plugin instance, tell it what to look for, this is a two character code that is how it is used in chat or other messages, a value, in this case a hex value, but you can use Spigot Colors, or java.awt colors. The reason for the spigot color support is to allow remapping spigot colors into different codes. This is useful if you want to use a single code that doesn't change, and just change the color in one place. Or to just use a different prefix symbol.  
Then you just add it uysing the `addCustomColor` method and just continue using any of the `color` methods. 
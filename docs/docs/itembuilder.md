---
hide:
  - navigation
  - toc
---

# ItemBuilder class
The ItemBuilder class allows you to create items without having to use the annoying method that spigot makes you do. You can effectively do it all in one line if you so desire.  
I did split the different types of items into their own sub-classes to avoid clutter and promote strongly typing the items.  
The ItemBuilder class also allows supporting saving and loading from `ConfigurationSection`s as well as obtaining an ItemBuilder instance from an ItemStack.  
There are also static classes that exist within the main ItemBuilder class that allow you to quickly and easily create items of that type. These extend from their more specific types that actually provide the functionality.  
Please note, there isn't support for NBT data, but I plan on adding it using the NBTAPI in some future point.  
The ItemBuilder class can be used to create one-off items in a cleaner way, or be used as a template. Either way works just fine.  
Creating an ItemBuilder is as simple as passing in an XMaterial into the `of()` method, calling one of the static classes or instantiating a sub-class.  
In order to use this, you must have XMaterial as it uses it for cross-version support.
---
hide:
  - navigation
  - toc
---

# NMSVersion Enum
This enum is only used to detect the current NMS version between 1.8 and 1.20.2. The NMS version does not always match the Minecraft version, so watch out for that.  
This is used within this library to detect support for HEX Colors in the ColorUtils class. It will pull the current version using reflection when this class is loaded/referenced.  
You must use the `CURRENT_VERSION` constant and from there, you can use other Enum based methods to compare things. 
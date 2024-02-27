---
hide:
  - navigation
  - toc
---

# SpigotTaskFactory class
This class is an implementation of the StarLib `TaskFactory` class. The way to use it is to create an instance of it and pass in your plugin and then pass that into the places that require a TaskFactory. This uses the `BukkitScheduler` to actually do things and just wraps it.  
Use it like the normal BukkitScheduler, only you don't need to pass in your plugin instance to any of the methods as it will use the instance you passed into the constructor.
```java
TaskFactory factory = new SpigotTaskFactory(this);
factory.runTask(() -> getLogger().info("This is a test"));
```
There will be (Not created yet) a project that will register this to the Bukkit Services Manager, but that is currently not available yet. 
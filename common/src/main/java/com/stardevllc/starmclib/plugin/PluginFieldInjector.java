package com.stardevllc.starmclib.plugin;

import com.stardevllc.starlib.injector.SimpleFieldInjector;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginFieldInjector<P extends JavaPlugin> extends SimpleFieldInjector {
    protected P plugin;
    
    public PluginFieldInjector(P plugin) {
        this.plugin = plugin;
    }
    
    public P getPlugin() {
        return plugin;
    }
}
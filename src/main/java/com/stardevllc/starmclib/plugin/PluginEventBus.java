package com.stardevllc.starmclib.plugin;

import com.stardevllc.eventbus.impl.SimpleEventBus;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * An {@link com.stardevllc.eventbus.IEventBus} that allows any object to be called
 * @param <P> The plugin type
 */
public class PluginEventBus<P extends JavaPlugin> extends SimpleEventBus<Object> {
    protected final P plugin;
    
    public PluginEventBus(P plugin) {
        this.plugin = plugin;
    }
    
    public P getPlugin() {
        return plugin;
    }
}
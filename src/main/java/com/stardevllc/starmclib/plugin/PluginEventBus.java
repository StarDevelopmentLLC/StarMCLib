package com.stardevllc.starmclib.plugin;

import com.stardevllc.starlib.eventbus.IEventBus;
import com.stardevllc.starlib.eventbus.impl.SimpleEventBus;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * An {@link IEventBus} that allows any object to be called
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
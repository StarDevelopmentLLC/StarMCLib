package com.stardevllc.starmclib;

import com.stardevllc.smcversion.MinecraftVersion;
import com.stardevllc.starlib.eventbus.IEventBus;
import com.stardevllc.starlib.eventbus.impl.StarEventBus;
import com.stardevllc.starlib.injector.FieldInjector;
import com.stardevllc.starmclib.names.*;
import com.stardevllc.starmclib.plugin.PluginEventBus;
import com.stardevllc.starmclib.plugin.PluginFieldInjector;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;

public final class StarMCLib {
    public static final IEventBus<Object, Cancellable> GLOBAL_BUS = new StarEventBus<>();
    
    public static final FieldInjector GLOBAL_INJECTOR = FieldInjector.create();
    
    private static final Map<String, IEventBus<?, Cancellable>> pluginEventBusses = new HashMap<>();
    private static final Map<String, FieldInjector> pluginDependencyInjectors = new HashMap<>();
    
    private static JavaPlugin plugin;
    
    public static void init(JavaPlugin plugin) {
        if (StarMCLib.plugin != null) {
            plugin.getLogger().severe("StarMCLib has already been initialized by " + StarMCLib.plugin.getName());
            return;
        }
        
        StarMCLib.plugin = plugin;
        
        GLOBAL_INJECTOR.set(PluginManager.class, Bukkit.getServer().getPluginManager());
        GLOBAL_INJECTOR.set(ServicesManager.class, Bukkit.getServer().getServicesManager());
        GLOBAL_INJECTOR.set(BukkitScheduler.class, Bukkit.getServer().getScheduler());
        GLOBAL_INJECTOR.set(ScoreboardManager.class, Bukkit.getServer().getScoreboardManager());
        GLOBAL_INJECTOR.set(MinecraftVersion.CURRENT_VERSION);
        GLOBAL_INJECTOR.set(EntityNames.class, EntityNames.getInstance());
        GLOBAL_INJECTOR.set(MaterialNames.class, MaterialNames.getInstance());
        GLOBAL_INJECTOR.set(PotionNames.class, PotionNames.getInstance());
    }
    
    public static void registerPluginEventBus(JavaPlugin plugin, IEventBus<?, Cancellable> eventBus) {
        pluginEventBusses.put(plugin.getName(), eventBus);
        log("Registered " + plugin.getName() + "'s Plugin Event Bus");
    }
    
    public static void registerPluginEventBus(PluginEventBus<?> pluginEventBus) {
        registerPluginEventBus(pluginEventBus.getPlugin(), pluginEventBus);
    }
    
    public static void registerPluginInjector(JavaPlugin plugin, FieldInjector injector) {
        pluginDependencyInjectors.put(plugin.getName().toLowerCase(), injector);
        injector.addParentInjector(GLOBAL_INJECTOR);
        log("Registered " + plugin.getName() + "'s Dependency Injector");
    }
    
    public static void registerPluginInjector(PluginFieldInjector<?> injector) {
        registerPluginInjector(injector.getPlugin(), injector);
    }
    
    private static void log(String msg) {
        plugin.getLogger().info(msg);
    }
    
    public static Map<String, FieldInjector> getPluginDependencyInjectors() {
        return new HashMap<>(pluginDependencyInjectors);
    }
    
    public static Map<String, IEventBus<?, Cancellable>> getPluginEventBusses() {
        return new HashMap<>(pluginEventBusses);
    }
}
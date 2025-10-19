package com.stardevllc.starmclib;

import com.stardevllc.smcversion.MinecraftVersion;
import com.stardevllc.starlib.eventbus.IEventBus;
import com.stardevllc.starlib.eventbus.impl.SimpleEventBus;
import com.stardevllc.starlib.injector.FieldInjector;
import com.stardevllc.starmclib.names.*;
import com.stardevllc.starmclib.plugin.PluginEventBus;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;

public final class StarMCLib {
    public static final IEventBus<Object> GLOBAL_BUS = new SimpleEventBus<>();
    
    public static final FieldInjector GLOBAL_INJECTOR = FieldInjector.create();
    
    private static final Map<String, PluginEventBus<?>> pluginEventBusses = new HashMap<>();
    private static final Map<String, FieldInjector> pluginDependencyInjectors = new HashMap<>();
    
    private static JavaPlugin plugin;
    
    public static void init(JavaPlugin plugin) {
        if (StarMCLib.plugin != null) {
            plugin.getLogger().severe("StarMCLib has already been initialized by " + StarMCLib.plugin.getName());
            return;
        }
        
        StarMCLib.plugin = plugin;
        
        GLOBAL_INJECTOR.setInstance(PluginManager.class, Bukkit.getServer().getPluginManager());
        GLOBAL_INJECTOR.setInstance(ServicesManager.class, Bukkit.getServer().getServicesManager());
        GLOBAL_INJECTOR.setInstance(BukkitScheduler.class, Bukkit.getServer().getScheduler());
        GLOBAL_INJECTOR.setInstance(ScoreboardManager.class, Bukkit.getServer().getScoreboardManager());
        GLOBAL_INJECTOR.setInstance(MinecraftVersion.CURRENT_VERSION);
        GLOBAL_INJECTOR.setInstance(EntityNames.class, EntityNames.getInstance());
        GLOBAL_INJECTOR.setInstance(MaterialNames.class, MaterialNames.getInstance());
        GLOBAL_INJECTOR.setInstance(PotionNames.class, PotionNames.getInstance());
    }
    
    public static void registerPluginEventBus(PluginEventBus<?> pluginEventBus) {
        pluginEventBusses.put(pluginEventBus.getPlugin().getName().toLowerCase(), pluginEventBus);
        log("Registered " + pluginEventBus.getPlugin().getName() + "'s Plugin Event Bus");
    }
    
    public static void registerPluginInjector(JavaPlugin plugin, FieldInjector injector) {
        pluginDependencyInjectors.put(plugin.getName().toLowerCase(), injector);
        injector.addParentInjector(GLOBAL_INJECTOR);
        log("Registered " + plugin.getName() + "'s Dependency Injector");
    }
    
    private static void log(String msg) {
        plugin.getLogger().info(msg);
    }
    
    public static Map<String, FieldInjector> getPluginDependencyInjectors() {
        return new HashMap<>(pluginDependencyInjectors);
    }
    
    public static Map<String, PluginEventBus<?>> getPluginEventBusses() {
        return new HashMap<>(pluginEventBusses);
    }
}
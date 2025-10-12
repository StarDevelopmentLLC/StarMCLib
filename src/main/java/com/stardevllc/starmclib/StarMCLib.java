package com.stardevllc.starmclib;

import com.stardevllc.smcversion.MinecraftVersion;
import com.stardevllc.starlib.dependency.DependencyInjector;
import com.stardevllc.starlib.eventbus.IEventBus;
import com.stardevllc.starlib.eventbus.impl.SimpleEventBus;
import com.stardevllc.starmclib.names.*;
import com.stardevllc.starmclib.plugin.PluginEventBus;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;

public final class StarMCLib {
    public static final IEventBus<Object> GLOBAL_BUS = new SimpleEventBus<>();
    public static final IEventBus<Event> GLOBAL_BUKKIT_EVENT_BUS = new SimpleEventBus<>();
    
    public static final DependencyInjector GLOBAL_INJECTOR = DependencyInjector.create();
    
    public static final Map<String, PluginEventBus<?>> pluginEventBusses = new HashMap<>();
    public static final Map<String, DependencyInjector> pluginDependencyInjectors = new HashMap<>();
    
    public static void init() {
        log("Initializing StarMCLib");
        GLOBAL_BUS.addChildBus(GLOBAL_BUKKIT_EVENT_BUS);
        log("Added the Global Bukkit Event Bus to the Generic Global Event Bus");
        GLOBAL_INJECTOR.setInstance(PluginManager.class, Bukkit.getServer().getPluginManager());
        log("Added the PluginManager to the Global Injector");
        GLOBAL_INJECTOR.setInstance(ServicesManager.class, Bukkit.getServer().getServicesManager());
        log("Added the ServicesManager to the Global Injector");
        GLOBAL_INJECTOR.setInstance(BukkitScheduler.class, Bukkit.getServer().getScheduler());
        log("Added the BukkitScheduler to the Global Injector");
        GLOBAL_INJECTOR.setInstance(ScoreboardManager.class, Bukkit.getServer().getScoreboardManager());
        log("Added the ScoreboardManager to the Global Injector");
        GLOBAL_INJECTOR.setInstance(MinecraftVersion.CURRENT_VERSION);
        log("Added the Current Minecraft Version to the Global Injector");
        GLOBAL_INJECTOR.setInstance(EntityNames.class, EntityNames.getInstance());
        log("Added the default instance of EntityNames to the Global Injector");
        GLOBAL_INJECTOR.setInstance(MaterialNames.class, MaterialNames.getInstance());
        log("Added the default instance of MaterialNames to the Global Injector");
        GLOBAL_INJECTOR.setInstance(PotionNames.class, PotionNames.getInstance());
        log("Added the default instance of PotionNames to the Global Injector");
        log("StarMCLib Initialization Complete");
    }
    
    public static void registerPluginEventBus(PluginEventBus<?> pluginEventBus) {
        pluginEventBusses.put(pluginEventBus.getPlugin().getName().toLowerCase(), pluginEventBus);
        log("Registered " + pluginEventBus.getPlugin().getName() + "'s Plugin Event Bus");
    }
    
    public static void registerPluginInjector(JavaPlugin plugin, DependencyInjector injector) {
        pluginDependencyInjectors.put(plugin.getName().toLowerCase(), injector);
        injector.addParentInjector(GLOBAL_INJECTOR);
        log("Registered " + plugin.getName() + "'s Dependency Injector");
    }
    
    private static void log(String msg) {
        Bukkit.getLogger().info("[StarMCLib] " + msg);
    }
}
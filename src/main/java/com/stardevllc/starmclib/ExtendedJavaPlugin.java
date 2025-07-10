package com.stardevllc.starmclib;

import com.stardevllc.dependency.DependencyInjector;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A class that extends {@link JavaPlugin} that defines some extra things that are useful and/or consistent
 */
public class ExtendedJavaPlugin extends JavaPlugin {
    /**
     * Defines a PluginEventBus that listens for events that are triggered either for this plugin or maybe even some Bukkit Events<br>
     * Override the {@link #createEventBus()} method to define a custom event bus instance
     */
    protected final PluginEventBus<? extends ExtendedJavaPlugin> eventBus;
    
    /**
     * Defines an instance of {@link StarColorsAdventure} to be used bu this plugin<br>
     * Override the {@link #createColors()} method to define a custom colors instance
     */
    protected final StarColorsAdventure colors;
    
    /**
     * Defines an instance of a {@link DependencyInjector} for this plugin<br>
     * The constructor will set a default instance for this injector to this plugin and the direct plugin class<br>
     * Override the {@link #createInjector()} method to define a custom injector instance
     */
    protected final DependencyInjector injector;
    
    /**
     * Creates a new {@link ExtendedJavaPlugin} <br>
     * There needs to always be a no-args constructor for Bukkit/Spigot/Paper
     */
    public ExtendedJavaPlugin() {
        this.eventBus = createEventBus();
        this.colors = createColors();
        this.injector = createInjector();
        this.injector.setInstance(this);
    }
    
    /**
     * Registers a command to the plugin <br>
     * This does the same thing as {@code registerCommand(cmd, tabExecutor, tabExecutor}
     *
     * @param cmd         The command name
     * @param tabExecutor The {@link TabExecutor} for the command
     * @see #registerCommand(String, CommandExecutor, TabCompleter)
     */
    protected void registerCommand(String cmd, TabExecutor tabExecutor) {
        registerCommand(cmd, tabExecutor, tabExecutor);
    }
    
    /**
     * Registers a command to the plugin. <br>
     * This does the same thing as {@code registerCommand(cmd, executor, null);}
     *
     * @param cmd      The command name
     * @param executor Te {@link CommandExecutor} for the command
     * @see #registerCommand(String, CommandExecutor, TabCompleter)
     */
    protected void registerCommand(String cmd, CommandExecutor executor) {
        registerCommand(cmd, executor, null);
    }
    
    /**
     * Registers a command to the plugin with null safety for the command
     *
     * @param cmd          The name of the command
     * @param executor     The {@link CommandExecutor} for the command
     * @param tabCompleter The {@link TabCompleter} for the command. This can be null
     */
    protected void registerCommand(String cmd, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand command = getCommand(cmd);
        if (command != null) {
            injector.inject(executor);
            command.setExecutor(executor);
            if (tabCompleter != null) {
                injector.inject(tabCompleter);
                command.setTabCompleter(tabCompleter);
            }
        }
    }
    
    /**
     * This is the Plugin's specific {@link PluginEventBus}
     *
     * @return The event bus instance
     */
    public PluginEventBus<? extends ExtendedJavaPlugin> getEventBus() {
        return eventBus;
    }
    
    /**
     * Creates a plugin event bus for this plugin.<br>
     * By default it just creates a new {@link PluginEventBus} using the constructor <br>
     * Override for a custom implementation
     *
     * @return The new {@link PluginEventBus}
     */
    protected PluginEventBus<? extends ExtendedJavaPlugin> createEventBus() {
        return new PluginEventBus<>(this);
    }
    
    /**
     * This is the Plugin's specific {@link StarColorsAdventure} <br>
     * Mainly used for customization and consistency for things related to this plugin
     *
     * @return The colors instance
     */
    public StarColorsAdventure getColors() {
        return colors;
    }
    
    /**
     * Creates the colors instance for this plugin <br>
     * By default it just calls the standard constructor of the {@link StarColorsAdventure} class
     *
     * @return The new {@link StarColorsAdventure}
     */
    protected StarColorsAdventure createColors() {
        return new StarColorsAdventure(this);
    }
    
    /**
     * This is the plugin's {@link DependencyInjector} used in the plugin
     *
     * @return The dependency injector instance
     */
    public DependencyInjector getInjector() {
        return injector;
    }
    
    /**
     * Creates a new Dependency Injector instance<br>
     * By default it just calls the {@link DependencyInjector#create()} method
     * 
     * @return The new {@link DependencyInjector}
     */
    protected DependencyInjector createInjector() {
        return DependencyInjector.create();
    }
}
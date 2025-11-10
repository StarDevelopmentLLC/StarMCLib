package com.stardevllc.starmclib.plugin;

import com.stardevllc.config.file.FileConfig;
import com.stardevllc.config.file.yaml.YamlConfig;
import com.stardevllc.starlib.eventbus.IEventBus;
import com.stardevllc.starlib.injector.FieldInjector;
import com.stardevllc.starmclib.StarColorsV2;
import com.stardevllc.starmclib.StarMCLib;
import com.stardevllc.starmclib.command.StarCommand;
import org.bukkit.command.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * A class that extends {@link JavaPlugin} that defines some extra things that are useful and/or consistent
 */
@SuppressWarnings("SameParameterValue")
public class ExtendedJavaPlugin extends JavaPlugin {
    /**
     * This event bus allows listening to things from StarMCLib and adding custom support for events<br>
     * Override the {@link #createEventBus()} method to define a custom event bus instance
     */
    private IEventBus<?, Cancellable> eventBus;
    
    /**
     * Defines an instance of {@link StarColorsV2} to be used by this plugin<br>
     * Override the {@link #createColors()} method to define a custom colors instance
     */
    private StarColorsV2 colors;
    
    /**
     * Defines an instance of a {@link FieldInjector} for this plugin<br>
     * The constructor will set a default instance for this injector to this plugin and the direct plugin class<br>
     * Override the {@link #createInjector()} method to define a custom injector instance
     */
    private FieldInjector injector;
    
    /**
     * Defines a FileConfig instance. This has full comment support through all versions and is similar to modern Bukkit config things
     */
    protected FileConfig mainConfig;
    
    /**
     * Creates a new {@link ExtendedJavaPlugin} <br>
     * There needs to always be a no-args constructor for Bukkit/Spigot/Paper
     */
    public ExtendedJavaPlugin() {
        
    }
    
    @Override
    public void onEnable() {
        this.eventBus = createEventBus();
        this.colors = createColors();
        this.injector = createInjector();
        this.injector.set(this);
        this.colors.init();
        StarMCLib.registerPluginInjector(this, injector);
        StarMCLib.registerPluginEventBus(this, eventBus);
        registerInstanceToGlobalInjector();
    }
    
    /**
     * Gets the main config of this plugin
     *
     * @return The config instance
     */
    public FileConfig getMainConfig() {
        if (this.mainConfig != null) {
            return this.mainConfig;
        }
        
        this.mainConfig = new YamlConfig(new File(getDataFolder(), "config.yml"));
        this.mainConfig.load();
        return this.mainConfig;
    }
    
    /**
     * Saves the main config
     */
    public void saveMainConfig() {
        if (this.mainConfig != null) {
            this.mainConfig.save();
        }
    }
    
    /**
     * Reloads the main config
     */
    public void reloadMainConfig() {
        this.mainConfig = null;
        getMainConfig();
    }
    
    /**
     * Allows custom registration of this plugin's injector to the global injector in StarMCLib
     */
    protected void registerInstanceToGlobalInjector() {
        StarMCLib.GLOBAL_INJECTOR.set(this);
    }
    
    /**
     * Registers a {@link StarCommand}. The plugin instance is provided in the constructor
     *
     * @param command The command
     * @param cmds    Any additional commands
     */
    protected void registerCommand(StarCommand<?> command, StarCommand<?>... cmds) {
        if (command != null) {
            command.register();
        }
        
        if (cmds != null) {
            for (StarCommand<?> cmd : cmds) {
                cmd.register();
            }
        }
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
            command.setExecutor(injector.inject(executor));
            if (tabCompleter != null) {
                command.setTabCompleter(injector.inject(tabCompleter));
            }
        }
    }
    
    /**
     * Registers the listeners to the PluginManager with this plugin
     *
     * @param listeners The array of listeners
     */
    protected void registerListeners(Listener... listeners) {
        if (listeners == null) {
            return;
        }
        
        PluginManager pluginManager = getServer().getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(getInjector().inject(listener), this);
        }
    }
    
    /**
     * This is the Plugin's specific {@link PluginEventBus}
     *
     * @return The event bus instance
     */
    public IEventBus<?, ?> getEventBus() {
        return eventBus;
    }
    
    /**
     * Creates a plugin event bus for this plugin.<br>
     * By default it just creates a new {@link PluginEventBus} using the constructor <br>
     * Override for a custom implementation
     *
     * @return The new {@link IEventBus}
     */
    protected IEventBus<?, Cancellable> createEventBus() {
        return new PluginEventBus<>(this);
    }
    
    /**
     * This is the Plugin's specific {@link StarColorsV2} <br>
     * Mainly used for customization and consistency for things related to this plugin
     *
     * @return The colors instance
     */
    public StarColorsV2 getColors() {
        return colors;
    }
    
    /**
     * Creates the colors instance for this plugin <br>
     * By default it just calls the standard constructor of the {@link StarColorsV2} class
     *
     * @return The new {@link StarColorsV2}
     */
    protected StarColorsV2 createColors() {
        return new StarColorsV2(this);
    }
    
    /**
     * This is the plugin's {@link FieldInjector} used in the plugin <br>
     *
     * @return The field injector instance
     */
    public FieldInjector getInjector() {
        return injector;
    }
    
    /**
     * Creates a new Field Injector instance<br>
     * This creates an instace of {@link PluginFieldInjector} by default
     *
     * @return The new {@link FieldInjector}
     */
    protected FieldInjector createInjector() {
        return new PluginFieldInjector<>(this);
    }
}
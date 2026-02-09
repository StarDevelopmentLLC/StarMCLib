package com.stardevllc.starmclib.command;

import com.stardevllc.starlib.objects.builder.IBuilder;
import com.stardevllc.starmclib.StarColorsV2;
import com.stardevllc.starmclib.command.flags.CmdFlags;
import com.stardevllc.starmclib.command.flags.FlagResult;
import com.stardevllc.starmclib.command.params.CmdParams;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("DuplicatedCode")
public class StarCommand<T extends JavaPlugin> implements ICommand<T>, TabExecutor {
    
    @FunctionalInterface
    public interface Executor<P extends JavaPlugin> {
        boolean execute(P plugin, CommandSender sender, String label, String[] args, FlagResult flagResults);
    }
    
    @FunctionalInterface
    public interface Completer<P extends JavaPlugin> {
        List<String> complete(P plugin, CommandSender sender, String label, String[] args, FlagResult flagResults);
    }
    
    protected final T plugin;
    protected final StarColorsV2 colors;
    
    protected String name;
    protected String[] aliases;
    protected String description;
    protected boolean playerOnly;
    protected boolean consoleOnly;
    protected String permission;
    
    protected Executor<T> executor;
    protected Completer<T> completer;
    
    protected List<SubCommand<T>> subCommands = new ArrayList<>();
    
    protected CmdFlags cmdFlags = new CmdFlags();
    
    /**
     * CmdParams should be parsed on an individual command basis in the handler method. Provided as a field for convenience
     */
    protected CmdParams cmdParams = new CmdParams();
    
    protected Component playerOnlyMessage;
    protected Component consoleOnlyMessage;
    protected Component noPermissionMessage;
    protected Component invalidSubCommandMessage;

    public StarCommand(T plugin, String name, String description, String permission, String... aliases) {
        this.plugin = plugin;
        
        if (plugin instanceof ExtendedJavaPlugin extendedPlugin) {
            this.colors = extendedPlugin.getColors();
        } else {
            this.colors = new StarColorsV2(plugin);
        }
        
        this.permission = permission;
        
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }
    
    public boolean execute(CommandSender sender, String label, String[] args, FlagResult flagResults) {
        if (this.executor != null) {
            return executor.execute(plugin, sender, label, args, flagResults);
        }
        
        return false;
    }
    
    public List<String> getCompletions(CommandSender sender, String label, String[] args, FlagResult flagResults) {
        if (this.completer != null) {
            return this.completer.complete(plugin, sender, label, args, flagResults);
        }
        
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            if (this.subCommands.isEmpty()) {
                completions.addAll(getCompletions(sender, label, args, flagResults));
            } else {
                this.subCommands.forEach(scmd -> {
                    completions.add(scmd.getName());
                    if (scmd.getAliases() != null) {
                        Collections.addAll(completions, scmd.getAliases());
                    }
                });
            }
            String arg = args[0].toLowerCase();
            completions.removeIf(completion -> !completion.toLowerCase().startsWith(arg));
        } else if (args.length > 1) {
            SubCommand<T> subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                String cmdLabel = args[0];
                
                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                
                args = newArgs;
                
                completions.addAll(subCommand.getCompletions(sender, cmdLabel, args, flagResults));
            }
        }
        
        return completions;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (this.playerOnly && !(sender instanceof Player)) {
            if (this.playerOnlyMessage != null) {
                colors.send(sender, this.playerOnlyMessage);
            }
            return true;
        }
        
        if (this.consoleOnly && !(sender instanceof ConsoleCommandSender)) {
            if (this.consoleOnlyMessage != null) {
                colors.send(sender, this.consoleOnlyMessage);
            }
            return true;
        }
        
        if (this.permission != null && !this.permission.isBlank()) {
            if (!sender.hasPermission(this.permission)) {
                if (this.noPermissionMessage != null) {
                    colors.send(sender, this.consoleOnlyMessage);
                }
                return true;
            }
        }

        FlagResult flagResults = cmdFlags.parse(args);
        args = flagResults.args();
        
        if (execute(sender, label, args, flagResults)) {
            return true;
        }
        
        if (args.length > 0) {
            SubCommand<T> subCommand = getSubCommand(args[0]);
            
            if (subCommand == null) {
                if (this.invalidSubCommandMessage != null) {
                    colors.send(sender, this.invalidSubCommandMessage);
                }
                return true;
            }
            
            String cmdLabel = args[0];

            String[] newArgs = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, args.length - 1);

            args = newArgs;
            
            subCommand.onCommand(sender, cmdLabel, args, flagResults);
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(this.permission)) {
            return List.of();
        }
        
        FlagResult flagResult = this.cmdFlags.parse(args);
        args = flagResult.args();
        
        try {
            return getCompletions(sender, label, args, flagResult);
        } catch (Throwable t) {
            return List.of();
        }
    }
    
    public SubCommand<T> getSubCommand(String name) {
        for (SubCommand<T> subCommand : this.subCommands) {
            if (subCommand.getName().equalsIgnoreCase(name)) {
                return subCommand;
            }
            
            if (subCommand.getAliases() == null) {
                continue;
            }

            for (String alias : subCommand.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return subCommand;
                }
            }
        }
        
        return null;
    }
    
    public CmdFlags getCmdFlags() {
        return cmdFlags;
    }

    @Override
    public T getPlugin() {
        return plugin;
    }
    
    public void register() {
        PluginCommand pluginCommand = this.plugin.getCommand(this.name);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }
    
    @Override
    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public String getPermission() {
        return permission;
    }
    
    public List<SubCommand<T>> getSubCommands() {
        return subCommands;
    }

    @Override
    public boolean isPlayerOnly() {
        return playerOnly;
    }
    
    @Override
    public boolean isConsoleOnly() {
        return consoleOnly;
    }
    
    public StarColorsV2 getColors() {
        return colors;
    }
    
    public static <T extends JavaPlugin> Builder<T> builder(T plugin) {
        return new Builder<T>().plugin(plugin);
    }
    
    public static class Builder<T extends JavaPlugin> implements IBuilder<StarCommand<T>, Builder<T>> {
        protected T plugin;
        
        protected String name;
        protected String[] aliases;
        protected String description;
        protected boolean playerOnly;
        protected boolean consoleOnly;
        protected String permission;
        protected Executor<T> executor;
        protected Completer<T> completer;
        
        protected final List<SubCommand<T>> subCommands = new ArrayList<>();
        
        protected Component playerOnlyMessage;
        protected Component consoleOnlyMessage;
        protected Component noPermissionMessage;
        protected Component invalidSubCommandMessage;
        
        public Builder() {}
        
        public Builder(Builder<T> builder) {
            this.plugin = builder.plugin;
            this.name = builder.name;
            this.aliases = builder.aliases;
            this.description = builder.description;
            this.playerOnly = builder.playerOnly;
            this.consoleOnly = builder.consoleOnly;
            this.permission = builder.permission;
            this.executor = builder.executor;
            this.completer = builder.completer;
            this.subCommands.addAll(builder.subCommands);
            this.playerOnlyMessage = builder.playerOnlyMessage;
            this.consoleOnlyMessage = builder.consoleOnlyMessage;
            this.noPermissionMessage = builder.noPermissionMessage;
            this.invalidSubCommandMessage = builder.invalidSubCommandMessage;
        }
        
        @Override
        public StarCommand<T> build() {
            StarCommand<T> cmd = new StarCommand<>(plugin, name, description, permission, aliases);
            cmd.executor = this.executor;
            cmd.completer = this.completer;
            cmd.playerOnly = this.playerOnly;
            cmd.consoleOnly = this.consoleOnly;
            cmd.subCommands.addAll(this.subCommands);
            cmd.playerOnlyMessage = this.playerOnlyMessage;
            cmd.consoleOnlyMessage = this.consoleOnlyMessage;
            cmd.noPermissionMessage = this.noPermissionMessage;
            cmd.invalidSubCommandMessage = this.invalidSubCommandMessage;
            return cmd;
        }
        
        public Builder<T> completer(Completer<T> completer) {
            this.completer = completer;
            return self();
        }
        
        public Builder<T> executor(Executor<T> executor) {
            this.executor = executor;
            return self();
        }
        
        public Builder<T> plugin(T plugin) {
            this.plugin = plugin;
            return self();
        }
        
        public Builder<T> name(String name) {
            this.name = name;
            return self();
        }
        
        public Builder<T> aliases(String... aliases) {
            this.aliases = aliases;
            return self();
        }
        
        public Builder<T> description(String description) {
            this.description = description;
            return self();
        }
        
        public Builder<T> playerOnly(Component playerOnlyMessage) {
            this.playerOnly();
            this.playerOnlyMessage = playerOnlyMessage;
            return self();
        }
        
        public Builder<T> playerOnly() {
            this.playerOnly = true;
            this.consoleOnly = false;
            this.consoleOnlyMessage = null;
            return self();
        }
        
        public Builder<T> consoleOnly(Component consoleOnlyMessage) {
            this.consoleOnly();
            this.consoleOnlyMessage = consoleOnlyMessage;
            return self();
        }
        
        public Builder<T> consoleOnly() {
            this.consoleOnly = true;
            this.playerOnly = false;
            this.playerOnlyMessage = null;
            return self();
        }
        
        public Builder<T> permission(String permission) {
            this.permission = permission;
            return self();
        }
        
        public Builder<T> subCommand(Consumer<SubCommand.Builder<T>> subCommandBuilderConsumer) {
            SubCommand.Builder<T> builder = SubCommand.builder(this.plugin);
            subCommandBuilderConsumer.accept(builder);
            addSubCommand(builder);
            return self();
        }
        
        public Builder<T> addSubCommand(SubCommand.Builder<T> subCommandBuilder) {
            this.subCommands.add(subCommandBuilder.build());
            return self();
        }
        
        public Builder<T> addSubCommand(SubCommand<T> subCommand) {
            this.subCommands.add(subCommand);
            return self();
        }
        
        public Builder<T> playerOnlyMessage(Component playerOnlyMessage) {
            this.playerOnlyMessage = playerOnlyMessage;
            return self();
        }
        
        public Builder<T> consoleOnlyMessage(Component consoleOnlyMessage) {
            this.consoleOnlyMessage = consoleOnlyMessage;
            return self();
        }
        
        public Builder<T> noPermissionMessage(Component noPermissionMessage) {
            this.noPermissionMessage = noPermissionMessage;
            return self();
        }
        
        public Builder<T> invalidSubCommandMessage(Component invalidSubCommandMessage) {
            this.invalidSubCommandMessage = invalidSubCommandMessage;
            return self();
        }
        
        @Override
        public Builder<T> clone() {
            return new Builder<>(this);
        }
    }
}
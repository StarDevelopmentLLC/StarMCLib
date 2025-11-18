package com.stardevllc.starmclib.command;

import com.stardevllc.starlib.objects.builder.IBuilder;
import com.stardevllc.starmclib.StarColorsV2;
import com.stardevllc.starmclib.command.flags.*;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("DuplicatedCode")
public class SubCommand<T extends JavaPlugin> implements ICommand<T> {
    
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
    
    protected ICommand<T> parent;
    
    protected int index;
    protected String name;
    protected String[] aliases;
    protected String description;
    
    protected String permission;
    
    protected Executor<T> executor;
    protected Completer<T> completer;
    
    protected List<SubCommand<T>> subCommands = new ArrayList<>();
    
    protected CmdFlags cmdFlags = new CmdFlags();
    
    protected boolean playerOnly;
    protected boolean consoleOnly;
    
    protected Component playerOnlyMessage;
    protected Component consoleOnlyMessage;
    protected Component noPermissionMessage;
    protected Component invalidSubCommandMessage;
    
    public SubCommand(T plugin, ICommand<T> parent, int index, String name, String description, String permission, String... aliases) {
        this.plugin = plugin;
        
        if (parent instanceof StarCommand<T> pCmd) {
            this.colors = pCmd.getColors();
        } else if (plugin instanceof ExtendedJavaPlugin ePlugin) {
            this.colors = ePlugin.getColors();
        } else {
            this.colors = new StarColorsV2(plugin);
        }
        
        this.parent = parent;
        this.index = index;
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = aliases;
    }
    
    public void onCommand(CommandSender sender, String label, String[] args, FlagResult parentFlagResults) {
        if (this.playerOnly && !(sender instanceof Player)) {
            if (this.playerOnlyMessage != null) {
                colors.send(sender, this.playerOnlyMessage);
            }
            return;
        }
        
        if (this.consoleOnly && !(sender instanceof ConsoleCommandSender)) {
            if (this.consoleOnlyMessage != null) {
                colors.send(sender, this.consoleOnlyMessage);
            }
            return;
        }
        
        if (this.permission != null && !this.permission.isBlank()) {
            if (!sender.hasPermission(this.permission)) {
                if (this.noPermissionMessage != null) {
                    this.colors.send(sender, this.noPermissionMessage);
                }
                return;
            }
        }
        
        FlagResult flagResults = cmdFlags.parse(args);
        args = flagResults.args();
        
        flagResults.addFrom(parentFlagResults);
        
        //If the execute command returns false, the handle the configured sub commands
        if (execute(sender, label, args, flagResults)) {
            return;
        }
        
        if (args.length > 0) {
            SubCommand<T> subCommand = getSubCommand(args[0]);
            
            if (subCommand == null) {
                if (this.invalidSubCommandMessage != null) {
                    colors.send(sender, this.invalidSubCommandMessage);
                }
                return;
            }
            
            String sLabel = args[0];
            
            String[] newArgs = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
            
            args = newArgs;
            
            subCommand.onCommand(sender, sLabel, args, flagResults);
        }
    }
    
    public boolean execute(CommandSender sender, String label, String[] args, FlagResult flagResults) {
        if (this.executor != null) {
            return this.executor.execute(this.plugin, sender, label, args, flagResults);
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
    
    public SubCommand<T> getSubCommand(String label) {
        for (SubCommand<T> subCommand : this.subCommands) {
            if (subCommand.getName().equalsIgnoreCase(label)) {
                return subCommand;
            }
            
            if (subCommand.getAliases() == null) {
                continue;
            }
            
            for (String alias : subCommand.getAliases()) {
                if (alias.equalsIgnoreCase(label)) {
                    return subCommand;
                }
            }
        }
        
        return null;
    }
    
    public T getPlugin() {
        return plugin;
    }
    
    public ICommand<T> getParent() {
        return parent;
    }
    
    public int getIndex() {
        return index;
    }
    
    public String getName() {
        return name;
    }
    
    public String[] getAliases() {
        return aliases;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public boolean isConsoleOnly() {
        return consoleOnly;
    }
    
    public List<SubCommand<T>> getSubCommands() {
        return subCommands;
    }
    
    public boolean isPlayerOnly() {
        if (this.parent.isPlayerOnly()) {
            return true;
        }
        
        return this.playerOnly;
    }
    
    public static <T extends JavaPlugin> Builder<T> builder(T plugin) {
        return new Builder<T>().plugin(plugin);
    }
    
    public static class Builder<T extends JavaPlugin> implements IBuilder<SubCommand<T>, Builder<T>> {
        protected T plugin;
        
        protected int index;
        protected ICommand<T> parent;
        
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
        
        public Builder() {
        }
        
        public Builder(Builder<T> builder) {
            this.plugin = builder.plugin;
            this.parent = builder.parent;
            this.index = builder.index;
            this.name = builder.name;
            this.aliases = builder.aliases;
            this.description = builder.description;
            this.executor = builder.executor;
            this.completer = builder.completer;
            this.playerOnly = builder.playerOnly;
            this.consoleOnly = builder.consoleOnly;
            this.permission = builder.permission;
            this.subCommands.addAll(builder.subCommands);
            this.playerOnlyMessage = builder.playerOnlyMessage;
            this.consoleOnlyMessage = builder.consoleOnlyMessage;
            this.noPermissionMessage = builder.noPermissionMessage;
            this.invalidSubCommandMessage = builder.invalidSubCommandMessage;
        }
        
        @Override
        public SubCommand<T> build() {
            SubCommand<T> cmd = new SubCommand<>(plugin, parent, index, name, description, permission, aliases);
            cmd.playerOnly = this.playerOnly;
            cmd.consoleOnly = this.consoleOnly;
            cmd.subCommands.addAll(this.subCommands);
            cmd.playerOnlyMessage = this.playerOnlyMessage;
            cmd.consoleOnlyMessage = this.consoleOnlyMessage;
            cmd.noPermissionMessage = this.noPermissionMessage;
            cmd.invalidSubCommandMessage = this.invalidSubCommandMessage;
            cmd.executor = this.executor;
            cmd.completer = this.completer;
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
        
        public Builder<T> subCommand(Consumer<Builder<T>> subCommandBuilderConsumer) {
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
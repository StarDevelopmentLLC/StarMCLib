package com.stardevllc.starmclib.smlplugin.cmd;

import com.stardevllc.starlib.converter.string.StringConverter;
import com.stardevllc.starmclib.actors.Actor;
import com.stardevllc.starmclib.actors.Actors;
import com.stardevllc.starmclib.command.StarCommand;
import com.stardevllc.starmclib.command.SubCommand;
import com.stardevllc.starmclib.mojang.MojangAPI;
import com.stardevllc.starmclib.mojang.MojangProfile;
import com.stardevllc.starmclib.paginator.ChatPaginator;
import com.stardevllc.starmclib.paginator.ChatPaginator.DefaultVars;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;

public class StarMCLibCmd extends StarCommand<ExtendedJavaPlugin> {
    
    private ChatPaginator<Actor> actorPaginator;
    private ChatPaginator<MojangProfile> profilePaginator;
    
    private final Map<Object, Actor> actors;
    private final Map<UUID, MojangProfile> profiles;
    
    public StarMCLibCmd(ExtendedJavaPlugin plugin) {
        super(plugin, "starmclib", "A command to manage aspects of StarMCLib", "starmclib.command");
        
        actors = Actors.getActors().addContentMirror(new HashMap<>());
        this.profiles = MojangAPI.getProfiles().addContentMirror(new HashMap<>());
        
        this.actorPaginator = new ChatPaginator.Builder<Actor>()
                .header((paginator, actor) -> "&eList of Actors (&b" + DefaultVars.CURRENT_PAGE + "&e/&b" + DefaultVars.TOTAL_PAGES + "&e)")
                .footer((paginator, actor) -> {
                    if (paginator.getCurrentPage(actor) < paginator.getTotalPages()) {
                        return "&eNext Page: &b/starmclib actors list " + DefaultVars.NEXT_PAGE;
                    }
                    return "";
                })
                .lineFormat("  &8- &e" + DefaultVars.ELEMENT)
                .elements(actors.values())
                .elementsPerPage(7)
                .converter(new StringConverter<>() {
                    public String convertFrom(Actor actor) {
                        return actor.getClass().getSimpleName().replace("Actor", "") + ": " + actor.getName();
                    }
                })
                .build();
        
        this.profilePaginator = new ChatPaginator.Builder<MojangProfile>()
                .header((paginator, actor) -> "&eList of Mojang Profiles (&b" + DefaultVars.CURRENT_PAGE + "&e/&b" + DefaultVars.TOTAL_PAGES + "&e)")
                .footer((paginator, actor) -> {
                    if (paginator.getCurrentPage(actor) < paginator.getTotalPages()) {
                        return "&eNext Page: &b/starmclib profile list " + DefaultVars.NEXT_PAGE;
                    }
                    return "";
                })
                .lineFormat("  &8- &e" + DefaultVars.ELEMENT)
                .elements(profiles.values())
                .elementsPerPage(7)
                .converter(new StringConverter<>() {
                    public String convertFrom(MojangProfile profile) {
                        return profile.getName();
                    }
                })
                .build();
        
        this.subCommands.add(SubCommand.builder(plugin)
                .name("actors")
                .description("Actor management command")
                .permission("starmclib.command.actors")
                .noPermissionMessage(Component.text("You do not have permission to use that command.").color(NamedTextColor.RED))
                .subCommand(sb -> {
                    sb.name("list").description("List the actors that are registered");
                    sb.noPermissionMessage(Component.text("You do not have permission to use that command.").color(NamedTextColor.RED));
                    sb.permission("starmclib.command.actors.list");
                    sb.executor((p, sender, label, args, flagResults) -> {
                        handlePaginator(args, actorPaginator, Actors.create(sender));
                        return true;
                    });
                })
                .subCommand(sb -> {
                    sb.name("sendmessage").aliases("sendmsg", "sm").description("Send a message to an actor");
                    sb.noPermissionMessage(Component.text("You do not have permission to use that command.").color(NamedTextColor.RED));
                    sb.permission("starmclib.command.actors.sendmessage");
                    sb.executor((p, sender, label, args, flagResults) -> {
                        Actor senderActor = Actors.create(sender);
                        if (!(args.length > 1)) {
                            senderActor.sendColoredMessage("&cUsage: /starmclib actors " + label + " <actor> <message>");
                            return true;
                        }
                        
                        Actor target = Actors.create(args[0]);
                        if (target == null) {
                            senderActor.sendColoredMessage("&cYou provided an invalid actor identifier.");
                            return true;
                        }
                        
                        StringBuilder sb1 = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            sb1.append(args[i]);
                            if (i < args.length - 1) {
                                sb1.append(" ");
                            }
                        }
                        
                        target.sendColoredMessage(sb1.toString());
                        return true;
                    });
                    sb.completer((p, sender, label, args, flagResults) -> {
                        List<String> completions = new ArrayList<>();
                        if (args.length == 1) {
                            for (Actor value : Actors.getActors().values()) {
                                completions.add(value.getName());
                            }
                            
                            completions.removeIf(a -> !a.toLowerCase().startsWith(args[0].toLowerCase()));
                        } else {
                            completions.add("<message>");
                        }
                        
                        return completions;
                    });
                })
                .build());
        
        this.subCommands.add(SubCommand.builder(plugin)
                .name("profiles")
                .permission("starmclib.command.profiles")
                .noPermissionMessage(Component.text("You do not have permission to use that command.").color(NamedTextColor.RED))
                .description("Profile management command")
                .subCommand(sb -> {
                    sb.name("list").permission("starmclib.command.profiles.list").description("List the profiles");
                    sb.noPermissionMessage(Component.text("You do not have permission to use that command.").color(NamedTextColor.RED));
                    sb.executor((p, sender, label, args, flagResults) -> {
                        handlePaginator(args, profilePaginator, Actors.create(sender));
                        return true;
                    });
                })
                
                .build());
    }
    
    private void handlePaginator(String[] args, ChatPaginator<?> paginator, Actor senderActor) {
        if (paginator.getTotalPages() == 0) {
            senderActor.sendColoredMessage("&cThere are no results to display.");
            return;
        }
        
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                senderActor.sendColoredMessage("&cInvalid number for page argument.");
                return;
            }
        }
        
        paginator.display(senderActor, page);
    }
}

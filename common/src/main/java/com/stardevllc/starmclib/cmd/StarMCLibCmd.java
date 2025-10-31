package com.stardevllc.starmclib.cmd;

import com.stardevllc.starlib.converter.string.StringConverter;
import com.stardevllc.starmclib.StarColorsV2;
import com.stardevllc.starmclib.actors.Actor;
import com.stardevllc.starmclib.actors.Actors;
import com.stardevllc.starmclib.mojang.MojangAPI;
import com.stardevllc.starmclib.mojang.MojangProfile;
import com.stardevllc.starmclib.paginator.ChatPaginator;
import com.stardevllc.starmclib.paginator.ChatPaginator.DefaultVars;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import org.bukkit.command.*;

import java.util.*;

@SuppressWarnings("FoldExpressionIntoStream")
public class StarMCLibCmd implements CommandExecutor {
    private ExtendedJavaPlugin plugin;
    
    private ChatPaginator<Actor> actorPaginator;
    private ChatPaginator<MojangProfile> profilePaginator;
    
    private final Map<Object, Actor> actors;
    private final Map<UUID, MojangProfile> profiles;
    
    public StarMCLibCmd(ExtendedJavaPlugin plugin) {
        this.plugin = plugin;
        
        actors = Actors.getActors().addContentMirror(new HashMap<>());
        
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
        
        this.profiles = MojangAPI.getProfiles().addContentMirror(new HashMap<>());
        
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
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        StarColorsV2 colors = plugin.getColors();
        
        Actor senderActor = Actors.create(sender);
        if (!sender.hasPermission("starmclib.command")) {
            colors.coloredLegacy(sender, "&cYou do not have permission to use that command.");
            return true;
        }
        
        if (!(args.length > 0)) {
            colors.coloredLegacy(sender, "&cUsage: /" + label + " actors <subcommand> [args]");
            colors.coloredLegacy(sender, "&cUsage: /" + label + " profiles <subcommand>");
            return true;
        }
        
        // actors management
        if (args[0].equalsIgnoreCase("actors")) {
            if (!sender.hasPermission("starmclib.command.actors")) {
                colors.coloredLegacy(sender, "&cYou do not have permission to use that command.");
                return true;
            }
            
            if (args.length == 1) {
                colors.coloredLegacy(sender, "&cUsage: /" + label + " " + args[0] + " list [page]");
                colors.coloredLegacy(sender, "&cUsage: /" + label + " " + args[0] + " sendmessage|sendmsg|sm <message>");
                return true;
            }
            
            if (args[1].equalsIgnoreCase("list")) {
                if (!sender.hasPermission("starmclib.command.actors.list")) {
                    colors.coloredLegacy(sender, "&cYou do not have permission to use that command.");
                    return true;
                }
                
                handlePaginator(args, actorPaginator, senderActor);
            } else if (args[1].equalsIgnoreCase("sendmessage") || args[1].equalsIgnoreCase("sendmsg") || args[1].equalsIgnoreCase("sm")) {
                if (!sender.hasPermission("starmclib.command.actors.sendmessage")) {
                    colors.coloredLegacy(sender, "&cYou do not have permission to use that command.");
                    return true;
                }
                
                if (!(args.length > 3)) {
                    senderActor.sendColoredMessage("&cUsage: /starmclib actors " + args[1] + " <actor> <message>");
                    return true;
                }
                
                Actor target = Actors.create(args[2]);
                if (target == null) {
                    senderActor.sendColoredMessage("&cYou provided an invalid actor identifier.");
                    return true;
                }
                
                StringBuilder sb = new StringBuilder();
                for (int i = 3; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i < args.length - 1) {
                        sb.append(" ");
                    }
                }
                
                target.sendColoredMessage(sb.toString());
            }
        } else if (args[0].equalsIgnoreCase("profiles")) {
            if (!sender.hasPermission("starmclib.command.profiles")) {
                colors.coloredLegacy(sender, "&cYou do not have permission to use that command.");
                return true;
            }
            
            if (args.length == 1) {
                colors.coloredLegacy(sender, "&cUsage: /" + label + " " + args[0] + " list");
                return true;
            }
            
            if (args[1].equalsIgnoreCase("list")) {
                if (!sender.hasPermission("starmclib.command.profiles.list")) {
                    colors.coloredLegacy(sender, "&cYou do not have permission to use that command.");
                    return true;
                }
                
                handlePaginator(args, profilePaginator, senderActor);
            }
        }
        
        return true;
    }
    
    private void handlePaginator(String[] args, ChatPaginator<?> paginator, Actor senderActor) {
        if (paginator.getTotalPages() == 0) {
            senderActor.sendColoredMessage("&cThere are no results to display.");
            return;
        }
        
        int page = 1;
        if (args.length > 2) {
            try {
                page = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                senderActor.sendColoredMessage("&cInvalid number for page argument.");
                return;
            }
        }
        
        paginator.display(senderActor, page);
    }
}

package com.stardevllc.starmclib.cmd;

import com.stardevllc.starlib.converter.string.StringConverters;
import com.stardevllc.starmclib.StarColorsV2;
import com.stardevllc.starmclib.actors.Actor;
import com.stardevllc.starmclib.actors.Actors;
import com.stardevllc.starmclib.paginator.Paginator;
import com.stardevllc.starmclib.paginator.Paginator.DefaultVars;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.List;

public class StarMCLibCmd implements CommandExecutor {
    private ExtendedJavaPlugin plugin;
    
    private Paginator<Actor> actorPaginator;
    
    private final List<Actor> actors = new ArrayList<>();
    
    public StarMCLibCmd(ExtendedJavaPlugin plugin) {
        this.plugin = plugin;
        
        Actors.getActors().addChangeListener(e -> {
            if (e.added() != null && !actors.contains(e.added())) {
                actors.add(e.added());
            } else if (e.removed() != null && e.added() == null) {
                actors.remove(e.removed());
            }
        });
        
        this.actorPaginator = new Paginator.Builder<Actor>()
                .header((paginator, actor) -> "&eList of Actors (&b" + DefaultVars.CURRENT_PAGE + "&e/&b" + DefaultVars.TOTAL_PAGES + "&e)")
                .footer((paginator, actor) -> {
                    if (paginator.getCurrentPage(actor) < paginator.getTotalPages()) {
                        return "&eNext Page: &b/starmclib actors list " + DefaultVars.NEXT_PAGE;
                    }
                    return "";
                })
                .lineFormat("  &8- &e" + DefaultVars.ELEMENT)
                .elements(actors)
                .elementsPerPage(7)
                .converter(StringConverters.getConverter(Actor.class))
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
            //Print usage
            return true;
        }
        
        // actors management
        if (args[0].equalsIgnoreCase("actors")) {
            if (args.length == 1) {
                //Print usage
                return true;
            }
            
            if (args[1].equalsIgnoreCase("list")) {
                if (actorPaginator.getTotalPages() == 0) {
                    senderActor.sendColoredMessage("&cThere are no results to display.");
                    return true;
                }
                
                int page = 1;
                if (args.length > 2) {
                    try {
                        page = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        senderActor.sendColoredMessage("&cInvalid number for page argument.");
                        return true;
                    }
                }
                
                actorPaginator.display(senderActor, page);
            }
        }
        
        // Mojang API management
        
        // The names utility management
        
        // Skin management
        
        // Thread Management
        
        return true;
    }
}

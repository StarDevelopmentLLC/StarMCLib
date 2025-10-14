package com.stardevllc.starmclib.cmd;

import com.stardevllc.starmclib.StarColorsV2;
import com.stardevllc.starmclib.actors.Actor;
import com.stardevllc.starmclib.actors.Actors;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import org.bukkit.command.*;

import java.util.Map;

public class StarMCLibCmd implements CommandExecutor {
    private ExtendedJavaPlugin plugin;
    
    public StarMCLibCmd(ExtendedJavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        StarColorsV2 colors = plugin.getColors();
        
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
            Map<Object, Actor> actors = Actors.getActors();
            
        }
        
        // Mojang API management
        
        // The names utility management
        
        // Skin management
        
        // Thread Management
        
        return true;
    }
}

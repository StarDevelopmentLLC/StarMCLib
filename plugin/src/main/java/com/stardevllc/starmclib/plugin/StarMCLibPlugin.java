package com.stardevllc.starmclib.plugin;

import com.stardevllc.starmclib.StarMCLib;
import com.stardevllc.starmclib.smlplugin.cmd.StarMCLibCmd;
import net.byteflux.libby.BukkitLibraryManager;

import java.io.File;

public class StarMCLibPlugin extends ExtendedJavaPlugin {
    public StarMCLibPlugin() {
        BukkitLibraryManager bukkitLibraryManager = new BukkitLibraryManager(this, new File(".", "plugins").toPath(), "libraries");
        bukkitLibraryManager.configureFromJSON();
    }
    
    @Override
    public void onEnable() {
        StarMCLib.init(this);
        super.onEnable();
        
        registerCommand(new StarMCLibCmd(this));
    }
}
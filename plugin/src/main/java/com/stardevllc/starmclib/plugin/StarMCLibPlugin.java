package com.stardevllc.starmclib.plugin;

import com.stardevllc.starmclib.StarMCLib;
import org.bukkit.plugin.java.JavaPlugin;

public class StarMCLibPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        StarMCLib.init(this);
    }
}
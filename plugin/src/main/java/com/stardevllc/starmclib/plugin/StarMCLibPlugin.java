package com.stardevllc.starmclib.plugin;

import com.stardevllc.starmclib.StarMCLib;
import com.stardevllc.starmclib.cmd.StarMCLibCmd;

public class StarMCLibPlugin extends ExtendedJavaPlugin {
    @Override
    public void onEnable() {
        StarMCLib.init(this);
        
        registerCommand("starmclib", new StarMCLibCmd(this));
    }
}
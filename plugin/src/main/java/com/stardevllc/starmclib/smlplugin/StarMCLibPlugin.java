package com.stardevllc.starmclib.smlplugin;

import com.stardevllc.starmclib.StarMCLib;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import com.stardevllc.starmclib.smlplugin.cmd.StarMCLibCmd;

public class StarMCLibPlugin extends ExtendedJavaPlugin {
    @Override
    public void onEnable() {
        StarMCLib.init(this);
        super.onEnable();
        
        registerCommand(new StarMCLibCmd(this));
    }
}
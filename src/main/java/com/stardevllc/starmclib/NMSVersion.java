package com.stardevllc.starmclib;

import org.bukkit.Bukkit;

/**
 * Represents the NMSVersion of the server. Only 1.8 to 1.20.2 are supported
 */
public enum NMSVersion {
    v1_8_R1,
    v1_8_R2,
    v1_8_R3,
    v1_9_R1,
    v1_9_R2, 
    v1_10_R1, 
    v1_11_R1, 
    v1_12_R1,
    v1_13_R1,
    v1_13_R2, 
    v1_14_R1, 
    v1_15_R1,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3, 
    v1_17_R1,
    v1_18_R1,
    v1_18_R2,
    v1_19_R1,
    v1_19_R2,
    v1_19_R3, 
    v1_20_R1,
    v1_20_R2;

    /**
     * The current version of the server
     */
    public static final NMSVersion CURRENT_VERSION = getCurrentVersion();
    
    private static NMSVersion getCurrentVersion() {
        String a = Bukkit.getServer().getClass().getPackage().getName();
        String version = a.substring(a.lastIndexOf('.') + 1);
        return valueOf(version);
    }
}
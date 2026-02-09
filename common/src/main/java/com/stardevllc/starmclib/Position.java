package com.stardevllc.starmclib;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.util.NumberConversions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Position implements ConfigurationSerializable, Cloneable {
    
    static {
        ConfigurationSerialization.registerClass(Position.class);
    }
    
    protected double x, y, z;
    protected float yaw, pitch;

    public static Position fromLocation(Location location) {
        return new Position(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public Position() {
        this(0, 0, 0);
    }

    public Position(double x, double y, double z) {
        this(x, y, z, 0, 0);
    }

    public Position(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public Position(Map<String, Object> serialized) {
        this.x = (double) serialized.get("x");
        this.y = (double) serialized.get("y");
        this.z = (double) serialized.get("z");
        this.yaw = (float) (double) serialized.get("yaw");
        this.pitch = (float) (double) serialized.get("pitch");
    }

    public double getX() {
        return x;
    }
    
    public int getBlockX() {
        return NumberConversions.floor(x);
    }

    public double getY() {
        return y;
    }
    
    public int getBlockY() {
        return NumberConversions.floor(y);
    }

    public double getZ() {
        return z;
    }
    
    public int getBlockZ() {
        return NumberConversions.floor(z);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    public Location toBlockLocation(World world) {
        return new Location(world, getBlockX(), getBlockY(), getBlockZ());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return x == position.x && y == position.y && z == position.z;
    }

    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public Map<String, Object> serialize() {
        return new HashMap<>(Map.of("x", this.x, "y", this.y, "z", this.z, "yaw", this.yaw, "pitch", this.pitch));
    }
    
    @Override
    public Position clone() {
        return new Position(this.x, this.y, this.z, this.yaw, this.pitch);
    }
}
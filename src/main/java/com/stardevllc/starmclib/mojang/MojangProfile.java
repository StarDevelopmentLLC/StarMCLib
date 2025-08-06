package com.stardevllc.starmclib.mojang;

import com.google.gson.JsonObject;

import java.util.UUID;

public class MojangProfile {
    protected UUID uniqueId;
    protected String name;
    
    //Skins aren't stored because players can change their skins
    protected String skinValue;
    protected String skinSignature;
    
    public MojangProfile(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    public MojangProfile(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }
    
    public MojangProfile(UUID uniqueId, String name, String skinValue, String skinSignature) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.skinValue = skinValue;
        this.skinSignature = skinSignature;
    }
    
    public MojangProfile(JsonObject json) {
        this.uniqueId = UUID.fromString(json.get("uniqueid").toString());
        this.name = json.get("name").getAsString();
    }
    
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("uniqueid", this.uniqueId.toString());
        json.addProperty("name", this.name);
        return json;
    }
    
    public UUID getUniqueId() {
        return uniqueId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSkinValue() {
        return skinValue;
    }
    
    public void setSkinValue(String skinValue) {
        this.skinValue = skinValue;
    }
    
    public String getSkinSignature() {
        return skinSignature;
    }
    
    public void setSkinSignature(String skinSignature) {
        this.skinSignature = skinSignature;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        MojangProfile that = (MojangProfile) object;
        return uniqueId.equals(that.uniqueId);
    }
    
    @Override
    public int hashCode() {
        return uniqueId.hashCode();
    }
    
    @Override
    public String toString() {
        return "MojangProfile{" +
                "uniqueId=" + uniqueId +
                ", name='" + name + '\'' +
                ", skinValue='" + skinValue + '\'' +
                ", skinSignature='" + skinSignature + '\'' +
                '}';
    }
}

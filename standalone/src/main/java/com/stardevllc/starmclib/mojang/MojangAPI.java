package com.stardevllc.starmclib.mojang;

import com.google.gson.*;
import com.stardevllc.starlib.helper.StringHelper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

public final class MojangAPI {
    private static final String NAME_TO_UUID_URL = "https://api.mojang.com/users/profiles/minecraft/{playername}";
    
    private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/{uuid}?unsigned=false";
    
    private static final Map<UUID, MojangProfile> profiles = new HashMap<>();
    
    private static final Map<String, UUID> nameToUUIDCache = new HashMap<>();
    
    private static final Gson GSON = new Gson();
    
    private static JsonObject getJsonFromURL(String urlString) throws IOException {
        URL url = URI.create(urlString).toURL();
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:135.0) Gecko/20100101 Firefox/135.0");
        request.setRequestMethod("GET");
        request.connect();
        
        int responseCode = request.getResponseCode();
        if (responseCode != 200) {
            return null;
        }
        JsonElement json = JsonParser.parseReader(new InputStreamReader(request.getInputStream()));
        return json.getAsJsonObject();
    }
    
    public static MojangProfile getProfile(String name) {
        if (nameToUUIDCache.containsKey(name)) {
            return getProfile(nameToUUIDCache.get(name));
        }
        
        try {
            JsonObject json = getJsonFromURL(NAME_TO_UUID_URL.replace("{playername}", name));
            
            if (json == null) {
                return null;
            }
            
            UUID uuid = StringHelper.toUUID(json.get("id").getAsString());
            if (uuid == null) {
                return null;
            }
            
            return getProfile(uuid);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static MojangProfile getProfile(UUID uniqueId) {
        MojangProfile cachedProfile = profiles.get(uniqueId);
        if (cachedProfile != null) {
            if (cachedProfile.getSkinValue() != null && !cachedProfile.getSkinValue().isBlank()) {
                return cachedProfile;
            }
        }
        
        try {
            JsonObject json = getJsonFromURL(PROFILE_URL.replace("{uuid}", uniqueId.toString()));
            
            if (json == null) {
                return null;
            }
            
            if (json.has("errorMessage")) {
                return null;
            }
            
            String name = json.get("name").getAsString();
            JsonObject textures = json.get("properties").getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject();
            String skinValue = textures.get("value").getAsString();
            String skinSignature = textures.get("signature").getAsString();
            
            MojangProfile profile = cachedProfile;
            if (profile == null) {
                profile = new MojangProfile(uniqueId);
            }
            
            profile.setName(name);
            profile.setSkinValue(skinValue);
            profile.setSkinSignature(skinSignature);
            
            profiles.put(uniqueId, profile);
            nameToUUIDCache.put(name, uniqueId);
            
            return profile;
        } catch (Exception e) {
            return null;
        }
    }
}
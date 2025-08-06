package com.stardevllc.starmclib.skin;

import com.stardevllc.starlib.helper.StringHelper;
import com.stardevllc.starmclib.mojang.MojangAPI;
import com.stardevllc.starmclib.mojang.MojangProfile;

import java.util.*;

public final class SkinAPI {
//    private static final String MINESKIN_URL = "https://api.mineskin.org/generate/url?url=%s";
    
    private static final Map<String, Skin> skins = new HashMap<>();
    
//    public Skin getFromImage(String imageUrl) {
//        Skin skin = this.skins.get(imageUrl);
//        if (skin != null) {
//            return skin;
//        }
//        
//        try {
//            JsonObject object = getObjectFromURL(String.format(MINESKIN_URL, imageUrl));
//            
//            if (object.has("error")) {
//                return null;
//            }
//            
//            JsonObject skinInfo = object.getAsJsonObject("data").getAsJsonObject("texture");
//            
//            skin = new Skin(null, null, imageUrl, skinInfo.get("value").getAsString(), skinInfo.get("signature").getAsString());
//            this.skins.put(imageUrl, skin);
//            return skin;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
//        return null;
//    }
    
    public static Skin getFromMojang(Object object) {
        if (object instanceof String str) {
            try {
                UUID uuid = StringHelper.toUUID(str);
                return getByUUID(uuid);
            } catch (Exception e) {
                return getByPlayerName(str);
            }
        } else if (object instanceof UUID uuid) {
            return getByUUID(uuid);
        } else {
            return null;
        }
    }
    
    public static Skin getByPlayerName(String playerName) {
        if (skins.containsKey(playerName)) {
            return skins.get(playerName);
        }
        
        MojangProfile profile = MojangAPI.getProfile(playerName);
        
        if (profile == null) {
            return null;
        }
        
        Skin skin = new Skin(profile.getUniqueId(), profile.getName(), profile.getSkinValue(), profile.getSkinSignature());
        skins.put(profile.getUniqueId().toString(), skin);
        skins.put(profile.getName(), skin);
        return skin;
    }
    
    public static Skin getByUUID(UUID playerId) {
        if (skins.containsKey(playerId.toString())) {
            return skins.get(playerId.toString());
        }
        
        MojangProfile profile = MojangAPI.getProfile(playerId);
        
        if (profile == null) {
            return null;
        }
        
        Skin skin = new Skin(playerId, profile.getName(), profile.getSkinValue(), profile.getSkinSignature());
        skins.put(profile.getUniqueId().toString(), skin);
        skins.put(profile.getName(), skin);
        return skin;
    }
}

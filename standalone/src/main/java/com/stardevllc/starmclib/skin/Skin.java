package com.stardevllc.starmclib.skin;

import java.util.Objects;
import java.util.UUID;

public class Skin {
    private String identifier;
    private UUID uniqueId;
    private String textures;
    private String signature;

    public Skin(UUID uuid, String identifier, String textures, String signature) {
        this.uniqueId = uuid;
        this.identifier = identifier;
        this.textures = textures;
        this.signature = signature;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getTextures() {
        return textures;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (Skin) obj;
        return Objects.equals(this.identifier, that.identifier) &&
                Objects.equals(this.textures, that.textures) &&
                Objects.equals(this.signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, textures, signature);
    }

    @Override
    public String toString() {
        return "Skin[" +
                "identifier=" + identifier + ", " +
                "value=" + textures + ", " +
                "signature=" + signature + ']';
    }

}
package com.stardevllc.starmclib.skin;

import java.util.Objects;
import java.util.UUID;

public class Skin {
    private String identifier;
    private String playerName;
    private UUID uniqueId;
    private String value;
    private String signature;

    public Skin(UUID uuid, String playerName, String identifier, String value, String signature) {
        this.uniqueId = uuid;
        this.playerName = playerName;
        this.identifier = identifier;
        this.value = value;
        this.signature = signature;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getValue() {
        return value;
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
                Objects.equals(this.value, that.value) &&
                Objects.equals(this.signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, value, signature);
    }

    @Override
    public String toString() {
        return "Skin[" +
                "identifier=" + identifier + ", " +
                "value=" + value + ", " +
                "signature=" + signature + ']';
    }

}
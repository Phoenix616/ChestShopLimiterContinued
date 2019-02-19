package me.droreo002.cslimit.database;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {

    @Getter
    private final UUID playerUUID;
    @Getter
    private String playerName;
    @Getter
    @Setter
    private int maxShop;
    @Getter
    @Setter
    private int shopCreated;
    @Getter
    @Setter
    private String lastPermission;
    @Getter
    @Setter
    private String lastRank;

    public PlayerData(UUID playerUUID, String playerName, int maxShop, int shopCreated, String lastPermission, String lastRank) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.maxShop = maxShop;
        this.shopCreated = shopCreated;
        this.lastPermission = lastPermission;
        this.lastRank = lastRank;
    }


    public static PlayerData fromYaml(FileConfiguration config) {
        Validate.notNull(config, "Config cannot be null!");
        UUID uuid = UUID.fromString(config.getString("Data.uuid"));
        String playerName = config.getString("Data.playerName");
        int maxShop = config.getInt("Data.maxShop");
        int shopCreated = config.getInt("Data.shopCreated");
        String lastPermission = (config.getString("Data.lastPermission") == null) ? "empty" : config.getString("Data.lastPermission");
        String lastRank = (config.getString("Data.lastRank") == null) ? "empty" : config.getString("Data.lastRank");
        Validate.notNull(playerName, "Invalid config value!");
        return new PlayerData(uuid, playerName, maxShop, shopCreated, lastPermission, lastRank);
    }

    public static PlayerData fromSql() {
        // TODO : Continue
        return null;
    }
}

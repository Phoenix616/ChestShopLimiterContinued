package me.droreo002.cslimit.database;

import lombok.Getter;
import lombok.Setter;
import me.droreo002.cslimit.database.object.SqlData;
import me.droreo002.cslimit.database.object.SqlDataName;
import me.droreo002.oreocore.utils.world.LocationUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    // Final var
    @Getter
    private final UUID playerUUID;
    @Getter
    private final String playerName;
    @Getter
    private final List<SqlDataName> changes;

    // Non final
    @Getter
    private int maxShop;
    @Getter
    private int shopCreated;
    @Getter
    private String lastPermission;
    @Getter
    private String lastRank;
    @Getter
    private String lastShopLocation;

    public PlayerData(UUID playerUUID, String playerName, int maxShop, int shopCreated, String lastPermission, String lastRank, String lastShopLocation) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.maxShop = maxShop;
        this.shopCreated = shopCreated;
        this.lastPermission = lastPermission;
        this.lastRank = lastRank;
        this.lastShopLocation = lastShopLocation;
        this.changes = new ArrayList<>();
    }

    public void setMaxShop(int maxShop) {
        if (!changes.contains(SqlDataName.MAX_SHOP)) changes.add(SqlDataName.MAX_SHOP);
        this.maxShop = maxShop;
    }

    public void setShopCreated(int shopCreated) {
        if (!changes.contains(SqlDataName.SHOP_CREATED)) changes.add(SqlDataName.SHOP_CREATED);
        this.shopCreated = shopCreated;
    }

    public void setLastPermission(String lastPermission) {
        if (!changes.contains(SqlDataName.LAST_PERMISSION)) changes.add(SqlDataName.LAST_PERMISSION);
        this.lastPermission = lastPermission;
    }

    public void setLastRank(String lastRank) {
        if (!changes.contains(SqlDataName.LAST_RANK)) changes.add(SqlDataName.LAST_RANK);
        this.lastRank = lastRank;
    }

    public void setLastShopLocation(String lastShopLocation) {
        if (!changes.contains(SqlDataName.LAST_SHOP_LOCATION)) changes.add(SqlDataName.LAST_SHOP_LOCATION);
        this.lastShopLocation = lastShopLocation;
    }

    /**
     * Get the data from YAML file
     *
     * @param config : The YAML FileConfiguration
     * @return a new YAML based data if succeeded, null otherwise
     */
    public static PlayerData fromYaml(FileConfiguration config) {
        if (config == null) return null;
        if (!config.contains("Data")) return null;
        if (config.getConfigurationSection("Data") == null) return null;
        UUID uuid = UUID.fromString(config.getString("Data.uuid"));
        String playerName = config.getString("Data.playerName");
        int maxShop = config.getInt("Data.maxShop");
        int shopCreated = config.getInt("Data.shopCreated");
        String lastPermission = (config.getString("Data.lastPermission") == null) ? "empty" : config.getString("Data.lastPermission");
        String lastRank = (config.getString("Data.lastRank") == null) ? "empty" : config.getString("Data.lastRank");
        String lastShop = (config.getString("Data.lastShop") == null) ? "empty" : config.getString("Data.lastShop");
        return new PlayerData(uuid, playerName, maxShop, shopCreated, lastPermission, lastRank, lastShop);
    }

    /**
     * Get the data from SQL or MYSQL
     *
     * @param values : The values list
     * @return a new SQL or MYSQL based data if succeeded, null otherwise
     */
    public static PlayerData fromSql(List<Object> values) {
        if (values.isEmpty()) return null;
        if (values.size() != 7) return null;
        UUID uuid = UUID.fromString((String) values.get(0));
        String playerName = (String) values.get(1);
        int shopCreated = (int) values.get(2);
        int maxShop = (int) values.get(3);
        String lastPermission = (String) values.get(4);
        String lastRank = (String) values.get(5);
        String lastShopLocation = (String) values.get(6);
        return new PlayerData(uuid, playerName, maxShop, shopCreated, lastPermission, lastRank, lastShopLocation);
    }
}

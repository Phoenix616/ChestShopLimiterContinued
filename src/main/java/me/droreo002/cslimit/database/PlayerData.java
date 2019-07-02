package me.droreo002.cslimit.database;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.object.DataProperty;
import me.droreo002.cslimit.hook.objects.LuckPermsHook;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    private static final String PERMISSION_STRING = "csl.limit.";

    // Final var
    @Getter
    private final UUID playerUUID;
    @Getter
    private final String playerName;
    @Getter
    private final List<DataProperty> changes;

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
        if (!changes.contains(DataProperty.MAX_SHOP)) changes.add(DataProperty.MAX_SHOP);
        this.maxShop = maxShop;
    }

    public void setShopCreated(int shopCreated) {
        if (!changes.contains(DataProperty.SHOP_CREATED)) changes.add(DataProperty.SHOP_CREATED);
        this.shopCreated = shopCreated;
    }

    public void setLastPermission(String lastPermission) {
        if (!changes.contains(DataProperty.LAST_PERMISSION)) changes.add(DataProperty.LAST_PERMISSION);
        this.lastPermission = lastPermission;
    }

    public void setLastRank(String lastRank) {
        if (!changes.contains(DataProperty.LAST_RANK)) changes.add(DataProperty.LAST_RANK);
        this.lastRank = lastRank;
    }

    public void setLastShopLocation(String lastShopLocation) {
        if (!changes.contains(DataProperty.LAST_SHOP_LOCATION)) changes.add(DataProperty.LAST_SHOP_LOCATION);
        this.lastShopLocation = lastShopLocation;
    }

    public void addMaxShop(int value) {
        if (!changes.contains(DataProperty.MAX_SHOP)) changes.add(DataProperty.MAX_SHOP);
        this.maxShop += value;
    }

    public void addShopCreated(int value) {
        if (!changes.contains(DataProperty.SHOP_CREATED)) changes.add(DataProperty.SHOP_CREATED);
        this.shopCreated += value;
    }

    /**
     * Setup the data
     *
     * @param plugin ChestShopLimiter plugin instance
     */
    public void setupData(ChestShopLimiter plugin, boolean sql) {
        Player player = Bukkit.getPlayer(playerUUID);
        ConfigManager.Memory mem = plugin.getConfigManager().getMemory();
        DatabaseWrapper database = plugin.getDatabase().getWrapper();
        ConfigurationSection permLimit = mem.getShopLimit();

        if (plugin.getHookManager().isLuckPerms()) {
            LuckPermsHook hook = (LuckPermsHook) plugin.getHookManager().getHookMap().get("LuckPerms");
            hook.setupData(this);
        } else {
            if (player != null) {
                boolean hasPermission = false;
                for (String s : permLimit.getKeys(false)) {
                    int newLimit = permLimit.getInt(s + ".limit");
                    int playerLimit = getMaxShop();

                    if (player.hasPermission(PERMISSION_STRING + s)) {
                        // Don't update if player's perm is the same and the limit is also the same a.k.a no limit update on permission (We do this on sql only)
                        if (sql) {
                            if (getLastPermission().equalsIgnoreCase(PERMISSION_STRING + s) && (playerLimit == newLimit))
                                continue;
                        }
                        setLastPermission(PERMISSION_STRING + s);
                        hasPermission = true;
                        break;
                    }
                }
                if (!hasPermission) {
                    if (permLimit.getBoolean("force-default")) {
                        setLastPermission(PERMISSION_STRING + "default");
                        setMaxShop(permLimit.getInt("default.limit"));
                    }
                }
                if (player.hasPermission("csl.limit.unlimited")) {
                    setLastPermission("csl.limit.unlimited");
                }
            }
        }
        database.savePlayerData(this);
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
        String lastShopLocation = (config.getString("Data.lastShopLocation") == null) ? "empty" : config.getString("Data.lastShopLocation");
        return new PlayerData(uuid, playerName, maxShop, shopCreated, lastPermission, lastRank, lastShopLocation);
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

    /**
     * Get the data from old YAML File
     *
     * @param config : The YAML FileConfiguration
     * @return a new YAML based data if succeeded, null otherwise
     */
    public static PlayerData fromOldYaml(FileConfiguration config) {
        if (config == null) return null;
        if (!config.contains("player-name")) return null;
        if (config.getConfigurationSection("Info") == null) return null;
        UUID uuid = UUID.fromString(config.getString("Updated.fileName")); // File name will be a valid UUID
        String playerName = config.getString("player-name");
        int maxShop = config.getInt("Info.shopLimit");
        int shopCreated = config.getInt("Info.shopCreated");
        String lastPermission = "empty";
        String lastRank = "empty";
        String lastShopLocation = "empty";
        return new PlayerData(uuid, playerName, maxShop, shopCreated, lastPermission, lastRank, lastShopLocation);
    }
}

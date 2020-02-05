package me.droreo002.cslimit.database.object;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.database.DatabaseManager;
import me.droreo002.oreocore.database.DatabaseType;
import me.droreo002.oreocore.database.object.DatabaseFlatFile;
import me.droreo002.oreocore.utils.entity.PlayerUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlatFileData extends DatabaseFlatFile implements DatabaseWrapper {

    @Getter
    private final ConfigManager.Memory memory;
    @Getter
    private final Map<UUID, PlayerData> playerData;
    @Getter
    private final ChestShopLimiter plugin;

    public FlatFileData(ChestShopLimiter plugin, ConfigManager.Memory memory) {
        super(plugin, new File(plugin.getDataFolder(), memory.getFlatFileDatabaseFolder()), false);
        this.plugin = plugin;
        this.memory = memory;
        this.playerData = new HashMap<>();

        DatabaseManager.registerDatabase(plugin, this);
    }

    @Override
    public void loadData() {
        for (DataCache data : getDataCaches()) {
            PlayerData pData = PlayerData.fromYaml(data.getConfig());
            playerData.put(pData.getPlayerUUID(), pData);
        }
    }

    @Override
    public void addDefaults(FileConfiguration config) {
        config.set("Data", "{}");
    }

    /**
     * Get the player data, where the key is the player's UUID
     * Will auto setup if not contains.
     *
     * @param key : The key UUID
     * @return the PlayerData object if there's any, null otherwise
     */
    @Override
    public PlayerData getPlayerData(UUID key) {
        load(key);
        return playerData.get(key);
    }

    @Override
    public void savePlayerData(PlayerData playerData) {
        if (playerData.getChanges().isEmpty()) return;
        DataCache objectData = getDataCache(playerData.getPlayerUUID().toString());
        FileConfiguration config = objectData.getConfig();

        for (DataProperty property : playerData.getChanges()) {
            switch (property) {
                case LAST_RANK:
                    config.set(DataProperty.LAST_RANK.getAsPath(), playerData.getLastRank());
                    break;
                case LAST_PERMISSION:
                    config.set(DataProperty.LAST_PERMISSION.getAsPath(), playerData.getLastRank());
                    break;
                case UUID:
                    config.set(DataProperty.UUID.getAsPath(), playerData.getPlayerUUID().toString());
                    break;
                case NAME:
                    config.set(DataProperty.NAME.getAsPath(), playerData.getPlayerName());
                    break;
                case SHOP_CREATED:
                    config.set(DataProperty.SHOP_CREATED.getAsPath(), playerData.getShopCreated());
                    break;
                case MAX_SHOP:
                    config.set(DataProperty.MAX_SHOP.getAsPath(), playerData.getMaxShop());
                    break;
                case LAST_SHOP_LOCATION:
                    config.set(DataProperty.LAST_SHOP_LOCATION.getAsString(), playerData.getLastShopLocation());
                    break;
            }
        }
        saveData(objectData);
    }

    @Override
    public void updatePlayerData(PlayerData playerData) {
        playerData.setupData(plugin, false);
        savePlayerData(playerData);
    }

    @Override
    public void removePlayerData(UUID uuid, boolean delete) {
        final PlayerData playerData = getPlayerData(uuid);
        final DataCache data = getDataCache(uuid.toString());
        if (playerData == null) return;
        removeData(data, delete);
        this.playerData.remove(playerData.getPlayerUUID());
    }

    @Override
    public DatabaseType getType() {
        return databaseType;
    }

    @Override
    public void load(UUID key) {
        if (!playerData.containsKey(key)) {
            setup(key.toString(), true, setupCallbackType -> {
                // Try to generate new data
                DataCache objectData = getDataCache(key.toString());
                if (objectData == null)
                    throw new NullPointerException("No object data found for UUID " + key.toString() + " please contact administrator!");
                FileConfiguration config = objectData.getConfig();
                PlayerData data = null;
                if (setupCallbackType == SetupCallback.Type.CREATED_AND_LOADED) {
                    // If this is a new data. Process it in different way then
                    config.set("Data.uuid", key.toString());
                    config.set("Data.playerName", PlayerUtils.getPlayerName(key));
                    config.set("Data.shopCreated", 0);
                    config.set("Data.maxShop", 0);
                    config.set("Data.lastPermission", "{}");
                    config.set("Data.lastRank", "{}");
                    config.set("Data.lastShopLocation", "{}");

                    data = PlayerData.fromYaml(config);
                    data.setupData(plugin, false);

                    config.set("Data.maxShop", data.getMaxShop());
                    config.set("Data.lastRank", data.getLastRank());
                }
                if (setupCallbackType == SetupCallback.Type.LOADED) {
                    data = PlayerData.fromYaml(config);
                    data.setupData(plugin, false);

                    config.set("Data.maxShop", data.getMaxShop());
                    config.set("Data.lastRank", data.getLastRank());
                }
                if (data == null) throw new NullPointerException("Data is null!");
                saveData(objectData);
                playerData.put(key, data);
            });
        } else {
            updatePlayerData(playerData.get(key));
        }
    }

    @Override
    public void migrate(PlayerData playerData) {
        Debug.info("Trying to migrate &e" + playerData.getPlayerName() + " &fthis will take sometimes...", true, Debug.LogType.BOTH);
        UUID key = playerData.getPlayerUUID();
        setup(key.toString(), true, setupCallbackType -> {
            DataCache objectData = getDataCache(key.toString());
            if (objectData == null) throw new NullPointerException("No object data found for UUID " + key.toString() + " please contact administrator!");
            FileConfiguration config = objectData.getConfig();
            // Update the new config
            config.set("Data.uuid", playerData.getPlayerUUID().toString());
            config.set("Data.playerName", playerData.getPlayerName());
            config.set("Data.shopCreated", playerData.getShopCreated());
            config.set("Data.maxShop", playerData.getMaxShop());
            config.set("Data.lastPermission", playerData.getLastPermission());
            config.set("Data.lastRank", playerData.getLastRank());
            config.set("Data.lastShopLocation", playerData.getLastShopLocation());
            // Update the data
            objectData.setConfig(config);
            saveData(objectData);
            this.playerData.put(key, playerData);
        });
    }
}

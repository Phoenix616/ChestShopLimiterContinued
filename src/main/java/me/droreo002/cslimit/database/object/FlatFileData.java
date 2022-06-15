package me.droreo002.cslimit.database.object;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.CSLConfig;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.cslimit.utils.PlayerUtils;
import me.droreo002.oreocore.database.DatabaseType;
import me.droreo002.oreocore.database.FlatFileDatabase;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class FlatFileData extends FlatFileDatabase implements DatabaseWrapper {

    @Getter
    private final List<PlayerData> playerDataList;
    @Getter
    private final ChestShopLimiter plugin;

    public FlatFileData(ChestShopLimiter plugin, CSLConfig cslConfig) {
        super(plugin, new File(plugin.getDataFolder(), cslConfig.getFlatFileDatabaseFolder()), false);
        this.plugin = plugin;
        this.playerDataList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addDefaults(FileConfiguration config) {
        config.set("Data", "{}");
    }

    /**
     * Get the player data, where the key is the player's UUID
     * Will auto setup if not contains.
     *
     * @param key The key UUID
     * @return the PlayerData object if there's any, null otherwise
     */
    @Override
    public PlayerData getPlayerData(UUID key) {
        load(key);
        return this.playerDataList.stream().filter(playerData -> playerData.getPlayerUUID().equals(key)).findAny().orElse(null);
    }

    @Override
    public void savePlayerData(PlayerData playerData) {
        if (playerData.getChanges().isEmpty()) return;
        DataCache objectData = getDataCache(playerData.getPlayerUUID().toString());
        if (objectData == null) return;
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
        playerData.updateLimit(plugin, false);
    }

    @Override
    public void removePlayerData(UUID uuid, boolean delete) {
        final PlayerData playerData = getPlayerData(uuid);
        final DataCache data = getDataCache(uuid.toString());
        if (playerData == null) return;
        removeData(data, delete);
        this.playerDataList.removeIf(pData -> pData.getPlayerUUID().equals(uuid));
    }

    @Override
    public DatabaseType getType() {
        return databaseType;
    }

    @Override
    public void load(UUID key) {
        if (this.playerDataList.stream().filter(playerData -> playerData.getPlayerUUID().equals(key)).findAny().orElse(null) != null) return;
        createData(key.toString(), true, createResult -> {
            DataCache objectData = getDataCache(key.toString());
            FileConfiguration config = objectData.getConfig();
            // Setup default value
            if (createResult == CreateResult.CREATED_AND_LOADED) {
                config.set("Data.uuid", key.toString());
                config.set("Data.playerName", PlayerUtils.getPlayerName(key));
                config.set("Data.shopCreated", 0);
                config.set("Data.maxShop", 0);
                config.set("Data.lastPermission", "{}");
                config.set("Data.lastRank", "{}");
                config.set("Data.lastShopLocation", "{}");
            }
            PlayerData data = PlayerData.fromYaml(config);
            data.updateLimit(plugin, false); // Setup other value
            saveData(objectData);
            this.playerDataList.add(data);
        });
    }

    @Override
    public void migrate(PlayerData playerData) {
        Debug.info("&cMigration is no longer supported", true, Debug.LogType.BOTH);
    }
}

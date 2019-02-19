package me.droreo002.cslimit.database.object;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.hook.objects.LuckPermsHook;
import me.droreo002.oreocore.database.DatabaseManager;
import me.droreo002.oreocore.database.object.DatabaseFlatFile;
import me.droreo002.oreocore.utils.entity.PlayerUtils;
import me.droreo002.oreocore.utils.io.FileUtils;
import org.apache.commons.lang.Validate;
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
        super(plugin, new File(plugin.getDataFolder(), memory.getFlatFileDatabaseFolder()));
        this.plugin = plugin;
        this.memory = memory;
        this.playerData = new HashMap<>();
        loadData();
        DatabaseManager.registerDatabase(plugin, this);
    }

    @Override
    public void loadData() {
        for (Map.Entry ent : getDataCache().entrySet()) {
            Data data = (Data) ent.getValue();
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
        if (!playerData.containsKey(key)) {
            // Try to generate new data
            setup(key.toString(), true);
            Data objectData = getDataClass(key.toString());
            if (objectData == null) throw new NullPointerException("No object data found for UUID " + key.toString());
            FileConfiguration config = objectData.getConfig();
            // New data configuring
            if (config.contains("Data") && config.getConfigurationSection("Data") == null) {
                config.set("Data.uuid", key.toString());
                config.set("Data.playerName", PlayerUtils.getPlayerName(key));
                config.set("Data.shopCreated", 0);
                config.set("Data.maxShop", 0);
                if (plugin.getHookManager().isLuckPerms()) {
                    LuckPermsHook hook = (LuckPermsHook) plugin.getHookManager().getHookMap().get("LuckPerms");
                    hook.setupData(key, config, true);
                } else {
                    // TODO : Setup for permission based support. And then other database
                    // TODO : We'll do the core and commands later, just make sure the database is completed
                }
            }
            objectData.setConfig(config); // Update it
            saveData(objectData);
            PlayerData data = PlayerData.fromYaml(config);
            playerData.put(key, data);
            return playerData.get(key);
        } else {
            return playerData.get(key);
        }
    }

    @Override
    public void updatePlayerData(PlayerData playerData) {
        Data objectData = getDataClass(playerData.getPlayerUUID().toString());
        FileConfiguration config = objectData.getConfig();
        config.set("Data.uuid", playerData.getPlayerUUID().toString());
        config.set("Data.playerName", playerData.getPlayerName());
        config.set("Data.maxShop", playerData.getMaxShop());
        config.set("Data.shopCreated", playerData.getShopCreated());
        objectData.setConfig(config);
        saveData(objectData);
    }

    @Override
    public void removePlayerData(UUID uuid, boolean delete) {
        final PlayerData playerData = getPlayerData(uuid);
        final Data data = getDataClass(uuid.toString());
        Validate.notNull(playerData, "Cannot find data object for UUID " + uuid.toString());
        Validate.notNull(data, "Cannot find data object for player " + playerData.getPlayerName());
        if (delete) {
            removeData(data, true);
            this.playerData.remove(playerData.getPlayerUUID());
        } else {
            removeData(data, false);
            this.playerData.remove(playerData.getPlayerUUID());
        }
    }
}

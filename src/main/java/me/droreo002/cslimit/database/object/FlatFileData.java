package me.droreo002.cslimit.database.object;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.hook.objects.LuckPermsHook;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.configuration.ConfigMemory;
import me.droreo002.oreocore.database.DatabaseManager;
import me.droreo002.oreocore.database.DatabaseType;
import me.droreo002.oreocore.database.object.DatabaseFlatFile;
import me.droreo002.oreocore.utils.entity.PlayerUtils;
import me.droreo002.oreocore.utils.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlatFileData extends DatabaseFlatFile implements DatabaseWrapper {

    private static final String PERMISSION_STRING = "csl.limit.";

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
        load(key);
        return playerData.get(key);
    }

    @Override
    public void updatePlayerData(PlayerData playerData) {
        Data objectData = getDataClass(playerData.getPlayerUUID().toString());
        FileConfiguration config = objectData.getConfig();
        config.set("Data.uuid", playerData.getPlayerUUID().toString());
        config.set("Data.playerName", playerData.getPlayerName());
        config.set("Data.shopCreated", playerData.getShopCreated());
        config.set("Data.maxShop", playerData.getMaxShop());
        config.set("Data.lastPermission", playerData.getLastPermission());
        config.set("Data.lastRank", playerData.getLastRank());
        config.set("Data.lastShopLocation", playerData.getLastShopLocation());
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
            this.playerData.remove(playerData.getPlayerUUID());
        }
    }

    @Override
    public DatabaseType getType() {
        return databaseType;
    }

    @Override
    public void load(UUID key) {
        if (!playerData.containsKey(key)) {
            // Try to generate new data
            setup(key.toString(), true);
            Data objectData = getDataClass(key.toString());
            if (objectData == null) throw new NullPointerException("No object data found for UUID " + key.toString() + " please contact administrator!");
            FileConfiguration config = objectData.getConfig();
            // If this is a new data. Process it in different way then
            if (config.contains("Data") && config.getConfigurationSection("Data") == null) {
                config.set("Data.uuid", key.toString());
                config.set("Data.playerName", PlayerUtils.getPlayerName(key));
                config.set("Data.shopCreated", 0);
                config.set("Data.maxShop", 0);
                config.set("Data.lastPermission", "{}");
                config.set("Data.lastRank", "{}");
                config.set("Data.lastShopLocation", "{}");
                if (plugin.getHookManager().isLuckPerms()) {
                    LuckPermsHook hook = (LuckPermsHook) plugin.getHookManager().getHookMap().get("LuckPerms");
                    hook.setupData(key, config, true);
                } else {
                    setupData(key, config, true);
                }
                objectData.setConfig(config); // Update it
                saveData(objectData);
                PlayerData data = PlayerData.fromYaml(config);
                playerData.put(key, data);
                return;
            }
            if (plugin.getHookManager().isLuckPerms()) {
                LuckPermsHook hook = (LuckPermsHook) plugin.getHookManager().getHookMap().get("LuckPerms");
                hook.setupData(key, config, false);
            } else {
                setupData(key, config, false);
            }
            objectData.setConfig(config); // Update it
            saveData(objectData);
            PlayerData data = PlayerData.fromYaml(config);
            playerData.put(key, data);
        }
    }

    @Override
    public void migrate(PlayerData playerData) {
        Debug.info("Trying to migrate &e" + playerData.getPlayerName() + " &fthis will take sometimes...", true, Debug.LogType.BOTH);
        UUID key = playerData.getPlayerUUID();
        setup(key.toString(), true);
        Data objectData = getDataClass(key.toString());
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
    }

    private void setupData(UUID uuid, FileConfiguration config, boolean firstSetup) {
        Player player = Bukkit.getPlayer(uuid);
        ConfigManager.Memory mem = plugin.getConfigManager().getMemory();
        ConfigurationSection permLimit = mem.getShopLimit();
        DatabaseWrapper database = plugin.getDatabase().getWrapper();
        if (firstSetup) {
            if (player != null) {
                for (String s : permLimit.getKeys(false)) {
                    // Has perm. But not default
                    if (player.hasPermission(PERMISSION_STRING + s) && !s.equalsIgnoreCase("default")) {
                        config.set("Data.lastPermission", PERMISSION_STRING + s);
                        config.set("Data.maxShop", permLimit.getInt(s + ".limit"));
                        break;
                    } else {
                        if (permLimit.getBoolean("force-default")) {
                            config.set("Data.lastPermission", PERMISSION_STRING + "default");
                            config.set("Data.maxShop", permLimit.getInt("default.limit"));
                        }
                    }
                }
            }
        } else {
            if (player != null) {
                PlayerData data = PlayerData.fromYaml(config);
                if (data == null) throw new NullPointerException("Fatal Error! : Failed to get player data!");
                for (String s : permLimit.getKeys(false)) {
                    if (player.hasPermission(PERMISSION_STRING + s)) {
                        // Don't update because its the same perm
                        if (data.getLastPermission().equalsIgnoreCase(PERMISSION_STRING + s)) continue;
                        int newLimit = permLimit.getInt(s + ".limit");
                        if (data.getMaxShop() < newLimit) data.setMaxShop(newLimit);
                        data.setLastPermission(PERMISSION_STRING + s);
                        database.updatePlayerData(data);
                        break;
                    }
                }
            }
        }
    }
}

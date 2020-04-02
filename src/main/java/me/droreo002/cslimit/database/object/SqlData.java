package me.droreo002.cslimit.database.object;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.oreocore.database.DatabaseManager;
import me.droreo002.oreocore.database.DatabaseType;
import me.droreo002.oreocore.database.object.DatabaseSQL;
import me.droreo002.oreocore.database.utils.SqlDataKey;
import me.droreo002.oreocore.database.utils.SqlDatabaseTable;
import me.droreo002.oreocore.utils.entity.PlayerUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SqlData extends DatabaseSQL implements DatabaseWrapper {

    @Getter
    private final ConfigManager.Memory memory;
    @Getter
    private final Map<UUID, PlayerData> playerData;
    @Getter
    private final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
    @Getter
    private final List<DataProperty> column;

    public SqlData(JavaPlugin plugin, ConfigManager.Memory memory) {
        super(plugin, memory.getSqlDatabaseName(), new File(plugin.getDataFolder(), memory.getSqlDatabaseFolder()), memory.getSqlDatabaseType());
        this.memory = memory;
        this.playerData = new ConcurrentHashMap<>();
        this.column = new ArrayList<>();
        column.addAll(new ArrayList<>(Arrays.asList(DataProperty.values())));
        DatabaseManager.registerDatabase(plugin, this);
    }

    @Override
    public PlayerData getPlayerData(UUID uuid) {
        load(uuid);
        return playerData.get(uuid);
    }

    @Override
    public void savePlayerData(PlayerData playerData) {
        if (memory.isSqlForceUpdate()) {
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE `csl` SET ");
            for (DataProperty name : column) {
                switch (name) {
                    case SHOP_CREATED:
                        builder.append(name.getAsString()).append(" = '").append(playerData.getShopCreated()).append("',");
                        break;
                    case MAX_SHOP:
                        builder.append(name.getAsString()).append(" = '").append(playerData.getMaxShop()).append("',");
                        break;
                    case LAST_PERMISSION:
                        builder.append(name.getAsString()).append(" = '").append(playerData.getLastPermission()).append("',");
                        break;
                    case LAST_RANK:
                        builder.append(name.getAsString()).append(" = '").append(playerData.getLastRank()).append("',");
                        break;
                    case LAST_SHOP_LOCATION:
                        builder.append(name.getAsString()).append(" = '").append(playerData.getLastShopLocation()).append("',");
                        break;
                }
            }
            builder.setCharAt(builder.toString().length() - 1, ' '); // Remove last
            builder.append("WHERE UUID IS '").append(playerData.getPlayerUUID().toString()).append("';");
            execute(builder.toString());
        } else {
            if (playerData.getChanges().isEmpty()) return;
            // Only update if there's any changes
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE `csl` SET ");
            for (DataProperty name : playerData.getChanges()) {
                switch (name) {
                    case SHOP_CREATED:
                        builder.append(name.getAsString()).append(" = '").append(playerData.getShopCreated()).append("',");
                        break;
                    case MAX_SHOP:
                        builder.append(name.getAsString()).append(" = '").append(playerData.getMaxShop()).append("',");
                        break;
                    case LAST_PERMISSION:
                        builder.append(name.getAsString()).append(" = '").append(playerData.getLastPermission()).append("',");
                        break;
                    case LAST_RANK:
                        builder.append(name.getAsString()).append(" = '").append(playerData.getLastRank()).append("',");
                        break;
                    case LAST_SHOP_LOCATION:
                        builder.append(name.getAsString()).append(" = '").append(playerData.getLastShopLocation()).append("',");
                        break;
                }
            }
            builder.setCharAt(builder.toString().length() - 1, ' '); // Remove last
            builder.append("WHERE UUID IS '").append(playerData.getPlayerUUID().toString()).append("';");
            execute(builder.toString());
        }
    }

    @Override
    public void updatePlayerData(PlayerData playerData) {
        playerData.setupData(plugin, true);
        savePlayerData(playerData);
    }

    @Override
    public void removePlayerData(UUID uuid, boolean delete) {
        if (!playerData.containsKey(uuid)) {
            if (delete) {
                if (isExists("UUID", uuid.toString(), "csl"))
                    execute("DELETE FROM `csl` WHERE UUID IS '" + uuid.toString() + "'");
            }
        } else {
            if (delete) {
                playerData.remove(uuid);
                execute("DELETE FROM `csl` WHERE UUID IS '" + uuid.toString() + "'");
            } else {
                playerData.remove(uuid);
            }
        }
    }

    @Override
    public DatabaseType getType() {
        return databaseType;
    }

    @Override
    public void load(UUID uuid) {
        if (!playerData.containsKey(uuid)) {
            if (!isExists("UUID", uuid.toString(), "csl")) insertNew(uuid);
            final List<Object> values = queryRow("SELECT * FROM `csl` WHERE UUID IS '" + uuid.toString() + "'", column.stream().map(DataProperty::getAsString).toArray(String[]::new));
            final PlayerData resData = PlayerData.fromSql(values);
            if (resData == null)
                throw new NullPointerException("An error occurred when trying to load data from " + uuid.toString() + " UUID!");

            resData.setupData(plugin, false);
            playerData.put(resData.getPlayerUUID(), resData);
        } else {
            updatePlayerData(playerData.get(uuid));
        }
    }

    @Override
    public void migrate(PlayerData playerData) {
        if (!isExists("UUID", playerData.getPlayerUUID().toString(), "csl")) insertNew(playerData.getPlayerUUID());
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE `csl` SET ");
        for (DataProperty name : column) {
            switch (name) {
                case SHOP_CREATED:
                    builder.append(name.getAsString()).append(" = '").append(playerData.getShopCreated()).append("',");
                    break;
                case MAX_SHOP:
                    builder.append(name.getAsString()).append(" = '").append(playerData.getMaxShop()).append("',");
                    break;
                case LAST_PERMISSION:
                    builder.append(name.getAsString()).append(" = '").append(playerData.getLastPermission()).append("',");
                    break;
                case LAST_RANK:
                    builder.append(name.getAsString()).append(" = '").append(playerData.getLastRank()).append("',");
                    break;
                case LAST_SHOP_LOCATION:
                    builder.append(name.getAsString()).append(" = '").append(playerData.getLastShopLocation()).append("',");
                    break;
            }
        }
        builder.setCharAt(builder.toString().length() - 1, ' '); // Remove last
        builder.append("WHERE UUID IS '").append(playerData.getPlayerUUID().toString()).append("';");
        execute(builder.toString());
        this.playerData.put(playerData.getPlayerUUID(), playerData);
    }

    private void insertNew(UUID uuid) {
        String playerName = PlayerUtils.getPlayerName(uuid);
        if (playerName == null) throw new NullPointerException("Could not find any player name data from " + uuid + " UUID!");
        execute("INSERT INTO `csl` (UUID,name,shopCreated,maxShop,lastPermission,lastRank,lastShopLocation) " +
                "VALUES ('" + uuid.toString() + "','" + playerName + "',0,0,'empty','empty','empty');");
    }

    @Override
    public void loadAllData() {
        // Do nothing
    }

    @Override
    public SqlDatabaseTable getSqlDatabaseTable() {
        return new SqlDatabaseTable("csl",
                new SqlDataKey("UUID", true, SqlDataKey.KeyType.UUID, false, null),
                new SqlDataKey("name", false, SqlDataKey.KeyType.MINECRAFT_USERNAME, false, null),
                new SqlDataKey("shopCreated", false, SqlDataKey.KeyType.OPTIMIZED_INTEGER, false, "0"),
                new SqlDataKey("maxShop", false, SqlDataKey.KeyType.OPTIMIZED_INTEGER, false, "0"),
                new SqlDataKey("lastPermission", false, SqlDataKey.KeyType.TEXT, false, null),
                new SqlDataKey("lastRank", false, SqlDataKey.KeyType.TEXT, false, null),
                new SqlDataKey("lastShopLocation", false, SqlDataKey.KeyType.TEXT, false, null)
        );
    }
}

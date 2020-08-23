package me.droreo002.cslimit.database.object;

import lombok.Getter;
import lombok.SneakyThrows;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.CSLConfig;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.oreocore.database.DatabaseType;
import me.droreo002.oreocore.database.SQLDatabase;
import me.droreo002.oreocore.database.utils.SQLTableBuilder;
import me.droreo002.oreocore.utils.entity.PlayerUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static me.droreo002.oreocore.database.utils.SQLDataKey.create;
import static me.droreo002.oreocore.database.utils.SQLDataKey.KeyType;

public class SQLData extends SQLDatabase implements DatabaseWrapper {

    @Getter
    private final CSLConfig config;
    @Getter
    private final Map<UUID, PlayerData> playerData;
    @Getter
    private final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
    @Getter
    private final List<DataProperty> column;

    public SQLData(JavaPlugin plugin, CSLConfig config) {
        super(plugin, config.getDatabaseType(), config.getSqlConfiguration(),
                SQLTableBuilder.of("csl",
                create("UUID", KeyType.UUID).primary(),
                create("name", KeyType.MINECRAFT_USERNAME),
                create("shopCreated", KeyType.OPTIMIZED_INTEGER).defaultValue("0"),
                create("maxShop", KeyType.OPTIMIZED_INTEGER).defaultValue("0"),
                create("lastPermission", KeyType.TEXT),
                create("lastRank", KeyType.TEXT),
                create("lastShopLocation", KeyType.TEXT)
        ));
        this.playerData = new ConcurrentHashMap<>();
        this.config = config;
        this.column = new ArrayList<>(Arrays.asList(DataProperty.values()));
    }

    @Override
    public PlayerData getPlayerData(UUID uuid) {
        try {
            load(uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerData.get(uuid);
    }

    @Override
    public void savePlayerData(PlayerData playerData) {
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
        builder.append("WHERE UUID = '").append(playerData.getPlayerUUID().toString()).append("';");
        runStatement(builder.toString());
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
                Future<Boolean> exists = isExistsAsync("UUID", uuid.toString(), "csl");
                try {
                    if (exists.get()) {
                        runStatement("DELETE FROM `csl` WHERE UUID = '" + uuid.toString() + "'");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (delete) {
                playerData.remove(uuid);
                runStatement("DELETE FROM `csl` WHERE UUID = '" + uuid.toString() + "'");
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
    public void load(UUID uuid) throws Exception {
        if (!playerData.containsKey(uuid)) {
            if (!isExists("UUID", uuid.toString(), "csl")) insertNew(uuid);
            List<Object> values;
            if (config.isSqlAsyncMode()) {
                final Future<Object> v = queryRowAsync("SELECT * FROM `csl` WHERE UUID = '" + uuid.toString() + "'", column.stream().map(DataProperty::getAsString).toArray(String[]::new));
                values = (List<Object>) v.get();
            } else {
                values = queryRow("SELECT * FROM `csl` WHERE UUID = '" + uuid.toString() + "'", column.stream().map(DataProperty::getAsString).toArray(String[]::new));
            }
            final PlayerData resData = PlayerData.fromSql(values);
            if (resData == null)
                throw new NullPointerException("An error occurred when trying to load data from " + uuid.toString() + " UUID!");

            resData.setupData(plugin, true);
            playerData.put(resData.getPlayerUUID(), resData);
        } else {
            updatePlayerData(playerData.get(uuid));
        }
    }

    @Override
    @SneakyThrows
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
        builder.append("WHERE UUID = '").append(playerData.getPlayerUUID().toString()).append("';");
        runStatement(builder.toString());
        this.playerData.put(playerData.getPlayerUUID(), playerData);
    }

    private void insertNew(UUID uuid) {
        String playerName = PlayerUtils.getPlayerName(uuid);
        if (playerName == null) throw new NullPointerException("Could not find any player name data from " + uuid + " UUID!");
        runStatement("INSERT INTO `csl` (UUID,name,shopCreated,maxShop,lastPermission,lastRank,lastShopLocation) " +
                "VALUES ('" + uuid.toString() + "','" + playerName + "',0,0,'empty','empty','empty');");
    }

    @SneakyThrows
    private void runStatement(String sql) {
        if (config.isSqlAsyncMode()) {
            executeQueryAsync(sql);
        } else {
            executeQuery(sql);
        }
    }

    @Override
    public void onDisable() { }
}

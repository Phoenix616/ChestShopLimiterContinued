package me.droreo002.cslimit.database.object;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.hook.objects.LuckPermsHook;
import me.droreo002.oreocore.database.DatabaseManager;
import me.droreo002.oreocore.database.DatabaseType;
import me.droreo002.oreocore.database.object.DatabaseSQL;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class SqlData extends DatabaseSQL implements DatabaseWrapper {

    private static final String PERMISSION_STRING = "csl.limit.";

    @Getter
    private final ConfigManager.Memory memory;
    @Getter
    private final Map<UUID, PlayerData> playerData;
    @Getter
    private final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
    @Getter
    private final List<SqlDataName> column;

    public SqlData(JavaPlugin plugin, ConfigManager.Memory memory) {
        super(plugin, memory.getSqlDatabaseName(), new File(plugin.getDataFolder(), memory.getSqlDatabaseFolder()), memory.getSqlDatabaseType());
        this.memory = memory;
        this.playerData = new HashMap<>();
        this.column = new ArrayList<>();
        column.addAll(new ArrayList<>(Arrays.asList(SqlDataName.values())));
        loadData();
        DatabaseManager.registerDatabase(plugin, this);
    }

    @Override
    public void loadData() {
        // We do nothing here
    }

    @Override
    public String getFirstCommand() {
        // Change the column variable if you made change here
        return "CREATE TABLE IF NOT EXISTS `csl` (\n"
                + "  `UUID` VARCHAR(36) NOT NULL,\n" // UUID Length is 36 according to google
                + "  `name` VARCHAR(16) NOT NULL,\n" // Minecraft usename length is 16 according to google
                + "  `shopCreated` int(11) NOT NULL DEFAULT '0',\n"
                + "  `maxShop` int(11) NOT NULL DEFAULT '0',\n"
                + "  `lastPermission` TEXT NOT NULL,\n"
                + "  `lastRank` TEXT NOT NULL,\n"
                + "  `lastShopLocation` TEXT NOT NULL,\n"
                + " PRIMARY KEY (UUID));";
    }

    @Override
    public PlayerData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    @Override
    public void updatePlayerData(PlayerData playerData) {
        if (memory.isSqlForceUpdate()) {
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE `csl` SET ");
            for (SqlDataName name : column) {
                switch (name) {
                    case SHOP_CREATED:
                        builder.append(name.getNameAsString()).append(" = '").append(playerData.getShopCreated()).append("',");
                        break;
                    case MAX_SHOP:
                        builder.append(name.getNameAsString()).append(" = '").append(playerData.getMaxShop()).append("',");
                        break;
                    case LAST_PERMISSION:
                        builder.append(name.getNameAsString()).append(" = '").append(playerData.getLastPermission()).append("',");
                        break;
                    case LAST_RANK:
                        builder.append(name.getNameAsString()).append(" = '").append(playerData.getLastRank()).append("',");
                        break;
                    case LAST_SHOP_LOCATION:
                        builder.append(name.getNameAsString()).append(" = '").append(playerData.getLastShopLocation()).append("',");
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
            for (SqlDataName name : playerData.getChanges()) {
                switch (name) {
                    case SHOP_CREATED:
                        builder.append(name.getNameAsString()).append(" = '").append(playerData.getShopCreated()).append("',");
                        break;
                    case MAX_SHOP:
                        builder.append(name.getNameAsString()).append(" = '").append(playerData.getMaxShop()).append("',");
                        break;
                    case LAST_PERMISSION:
                        builder.append(name.getNameAsString()).append(" = '").append(playerData.getLastPermission()).append("',");
                        break;
                    case LAST_RANK:
                        builder.append(name.getNameAsString()).append(" = '").append(playerData.getLastRank()).append("',");
                        break;
                    case LAST_SHOP_LOCATION:
                        builder.append(name.getNameAsString()).append(" = '").append(playerData.getLastShopLocation()).append("',");
                        break;
                }
            }
            builder.setCharAt(builder.toString().length() - 1, ' '); // Remove last
            builder.append("WHERE UUID IS '").append(playerData.getPlayerUUID().toString()).append("';");
            execute(builder.toString());
        }
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
            if (!isExists("UUID", uuid.toString(), "csl")) {
                insertNew(uuid); // Insert new data and then create. This is a first setup!

                final List<Object> values = queryRow("SELECT * FROM `csl` WHERE UUID IS '" + uuid.toString() + "'", column.stream().map(SqlDataName::getNameAsString).toArray(String[]::new));
                final PlayerData resData = PlayerData.fromSql(values);
                if (resData == null) throw new NullPointerException("An error occurred when trying to load data from " + uuid.toString() + " UUID!");
                if (plugin.getHookManager().isLuckPerms()) {
                    LuckPermsHook hook = (LuckPermsHook) plugin.getHookManager().getHookMap().get("LuckPerms");
                    hook.setupData(uuid, resData, true);
                } else {
                    setupData(uuid, resData, true);
                }

                updatePlayerData(resData);
                playerData.put(resData.getPlayerUUID(), resData);
            } else {
                final List<Object> values = queryRow("SELECT * FROM `csl` WHERE UUID IS '" + uuid.toString() + "'", column.stream().map(SqlDataName::getNameAsString).toArray(String[]::new));
                final PlayerData resData = PlayerData.fromSql(values);
                if (resData == null) throw new NullPointerException("An error occurred when trying to load data from " + uuid.toString() + " UUID!");
                if (plugin.getHookManager().isLuckPerms()) {
                    LuckPermsHook hook = (LuckPermsHook) plugin.getHookManager().getHookMap().get("LuckPerms");
                    hook.setupData(uuid, resData, false);
                } else {
                    setupData(uuid, resData, false);
                }

                updatePlayerData(resData);
                playerData.put(resData.getPlayerUUID(), resData);
            }
        }
    }

    private void setupData(UUID uuid, PlayerData data, boolean firstSetup) {
        Player player = Bukkit.getPlayer(uuid);
        ConfigManager.Memory mem = plugin.getConfigManager().getMemory();
        ConfigurationSection permLimit = mem.getShopLimit();
        DatabaseWrapper database = plugin.getDatabase().getWrapper();
        if (firstSetup) {
            if (player != null) {
                for (String s : permLimit.getKeys(false)) {
                    // Has perm. But not default
                    if (player.hasPermission(PERMISSION_STRING + s) && !s.equalsIgnoreCase("default")) {
                        data.setLastPermission(PERMISSION_STRING + s);
                        data.setMaxShop(permLimit.getInt(s + ".limit"));
                        break;
                    } else {
                        if (permLimit.getBoolean("force-default")) {
                            data.setLastPermission(PERMISSION_STRING + "default");
                            data.setMaxShop(permLimit.getInt("default.limit"));
                        }
                    }
                }
            }
        } else {
            if (player != null) {
                if (data == null) throw new NullPointerException("Fatal Error! : Failed to get player data!");
                for (String s : permLimit.getKeys(false)) {
                    if (player.hasPermission(PERMISSION_STRING + s)) {
                        // Don't update because its the same perm
                        if (data.getLastPermission().equalsIgnoreCase(PERMISSION_STRING + s)) continue;
                        data.setMaxShop(permLimit.getInt(s + ".limit"));
                        data.setLastPermission(PERMISSION_STRING + s);
                        database.updatePlayerData(data);
                        break;
                    }
                }
            }
        }
    }

    private void insertNew(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) throw new NullPointerException("Player cannot be null when inserting new data!");
        execute("INSERT INTO `csl` (UUID,name,shopCreated,maxShop,lastPermission,lastRank,lastShopLocation) " +
                "VALUES ('" + uuid.toString() + "','" + player.getName() + "',0,0,'empty','empty','empty');");
    }
}

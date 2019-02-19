package me.droreo002.cslimit.database.object;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.oreocore.database.DatabaseManager;
import me.droreo002.oreocore.database.object.DatabaseSQL;
import me.droreo002.oreocore.database.object.interfaces.SqlCallback;
import me.droreo002.oreocore.utils.entity.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SqlData extends DatabaseSQL implements DatabaseWrapper {

    @Getter
    private final ConfigManager.Memory memory;
    @Getter
    private final Map<UUID, PlayerData> playerData;

    public SqlData(JavaPlugin plugin, ConfigManager.Memory memory) {
        super(plugin, memory.getSqlDatabaseName(), new File(plugin.getDataFolder(), memory.getSqlDatabaseFolder()));
        this.memory = memory;
        this.playerData = new HashMap<>();
        loadData();
        DatabaseManager.registerDatabase(plugin, this);
    }

    @Override
    public void loadData() {

    }

    @Override
    public String getFirstCommand() {
        return "CREATE TABLE IF NOT EXISTS `csl` (\n"
                + "  `UUID` VARCHAR(36) NOT NULL,\n" // UUID Length is 36 according to google
                + "  `name` VARCHAR(16) NOT NULL,\n" // Minecraft usename length is 16 according to google
                + "  `shopCreated` int(11) NOT NULL DEFAULT '0',\n"
                + "  `maxShop` int(11) NOT NULL DEFAULT '0',\n"
                + "PRIMARY KEY (UUID));";
    }

    private void insertNew(UUID uuid) {
        // Check if the player contains on the db first
        queryValueAsync("SELECT * FROM `csl` WHERE `UUID` is '" + uuid.toString() + "'", "name", new SqlCallback<Object>() {
            @Override
            public void onSuccess(Object done) {
                if (!(done instanceof String)) return;
                String str = String.valueOf(done);
                String playerName = PlayerUtils.getPlayerName(uuid);
                if (str.equalsIgnoreCase(playerName)) return;
                execute("INSERT INTO `csl` (UUID,name,shopCreated,maxShop) VALUES ('" + uuid.toString() + "','" + playerName + "','0','0');", true);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace(); // Just print
            }
        });
    }

    /**
     * Is this UUID available on the table?
     *
     * @param uuid : The UUID
     * @return true if available, false otherwise
     */
    public boolean isAvailable(UUID uuid) {
        try {
            return countRows(uuid) != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PlayerData getPlayerData(UUID uuid) {
        if (!playerData.containsKey(uuid)) {
            if (!isAvailable(uuid)) {
                insertNew(uuid); // Insert new data and then create
                return null;
            } else {
                // Get the data from database and then create
                return null;
            }
        } else {
            return playerData.get(uuid);
        }
    }

    @Override
    public void updatePlayerData(PlayerData playerData) {

    }

    @Override
    public void removePlayerData(UUID uuid, boolean delete) {

    }

    /**
     * Count how much row the key (UUID) has
     *
     * @param uuid : The UUID
     * @return 0 if there's none, greater than 0 otherwise
     * @throws SQLException : Oh shit error!
     */
    public int countRows(UUID uuid) throws SQLException {
        // select the number of rows in the table
        int rowCount;
        try (Statement stmt = getConnection().createStatement(); ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM `csl` WHERE `UUID` IS `" + uuid.toString() + "`")) {
            // get the number of rows from the result set
            rs.next();
            rowCount = rs.getInt(1);
        }
        return rowCount;
    }
}

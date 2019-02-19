package me.droreo002.cslimit.database.object;

import lombok.Getter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.oreocore.database.DatabaseManager;
import me.droreo002.oreocore.database.object.DatabaseMySQL;
import me.droreo002.oreocore.database.object.interfaces.SqlCallback;
import me.droreo002.oreocore.database.utils.MySqlConnection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MySQLData extends DatabaseMySQL implements DatabaseWrapper {

    @Getter
    private final Map<UUID, PlayerData> playerData;

    public MySQLData(JavaPlugin plugin, ConfigManager.Memory memory) {
        super(plugin, memory.getMySqlConnection(), memory.getMysqlSaveTime());
        this.playerData = new HashMap<>();
        loadData();
        DatabaseManager.registerDatabase(plugin, this);
    }

    @Override
    public void loadData() {
        // TODO : Continue
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

    public void insertNew(Player player) {
        // Check if the player contains on the db first
        queryValueAsync("SELECT * FROM `csl` WHERE `UUID` is '" + player.getUniqueId().toString() + "'", "name", new SqlCallback<Object>() {
            @Override
            public void onSuccess(Object done) {
                if (!(done instanceof String)) return;
                String str = String.valueOf(done);
                if (str.equalsIgnoreCase(player.getName())) return;
                execute("INSERT INTO `csl` (UUID,name,shopCreated,maxShop) VALUES ('" + player.getUniqueId().toString() + "','" + player.getName() + "','0','0');", true);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace(); // Just print
            }
        });
    }

    public void isAvailable(Player player) {
        queryValueAsync("SELECT * FROM `csl` WHERE `UUID` is '" + player.getUniqueId().toString() + "'", "name",new SqlCallback<Object>() {
            @Override
            public void onSuccess(Object done) {
                if (!(done instanceof String)) return;
                String str = String.valueOf(done);
                if (str.equalsIgnoreCase(player.getName())) return;
                execute("INSERT INTO `csl` (UUID,name,shopCreated,maxShop) VALUES ('" + player.getUniqueId().toString() + "','" + player.getName() + "','0','0');", true);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace(); // Just print
            }
        });
    }

    @Override
    public PlayerData getPlayerData(UUID uuid) {
        return null;
    }

    @Override
    public void updatePlayerData(PlayerData playerData) {

    }

    @Override
    public void removePlayerData(UUID uuid, boolean delete) {

    }
}

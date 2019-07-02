package me.droreo002.cslimit.listener;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.database.DatabaseType;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final ChestShopLimiter plugin;

    public PlayerConnectionListener(ChestShopLimiter plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        DatabaseWrapper wrp = plugin.getDatabase().getWrapper();

        try {
            wrp.load(player.getUniqueId());
            Debug.info("Data for player '" + player.getName() + "' has been loaded!", false, Debug.LogType.FILE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        DatabaseWrapper wrp = plugin.getDatabase().getWrapper();

        wrp.removePlayerData(player.getUniqueId(), false);
        Debug.info("Data for player '" + player.getName() + "' has been unloaded!", false, Debug.LogType.FILE);
    }
}

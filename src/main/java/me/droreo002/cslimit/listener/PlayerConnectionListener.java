package me.droreo002.cslimit.listener;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.oreocore.database.DatabaseType;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final ChestShopLimiter plugin;
    private final ConfigManager.Memory memory;

    public PlayerConnectionListener(ChestShopLimiter plugin) {
        this.plugin = plugin;
        this.memory = plugin.getConfigManager().getMemory();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        DatabaseWrapper wrp = plugin.getDatabase().getWrapper();
        wrp.load(player.getUniqueId());

        // Check the data
        PlayerData data = wrp.getPlayerData(player.getUniqueId());
        Validate.notNull(data, "Data cannot be null please contact dev!");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        DatabaseWrapper wrp = plugin.getDatabase().getWrapper();
        wrp.removePlayerData(player.getUniqueId(), false);
    }
}

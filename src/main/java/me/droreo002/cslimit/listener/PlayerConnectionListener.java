package me.droreo002.cslimit.listener;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.oreocore.configuration.ConfigMemory;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final ChestShopLimiter plugin;

    public PlayerConnectionListener(ChestShopLimiter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        DatabaseWrapper wrp = plugin.getDatabase().getWrapper();
        PlayerData data = wrp.getPlayerData(e.getPlayer().getUniqueId());
        Validate.notNull(data, "Data cannot be null!");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        DatabaseWrapper wrp = plugin.getDatabase().getWrapper();
        wrp.removePlayerData(player.getUniqueId(), false);
    }
}

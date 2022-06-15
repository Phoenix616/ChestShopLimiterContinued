package me.droreo002.cslimit.utils;

import com.Acrobot.ChestShop.Database.Account;
import com.Acrobot.ChestShop.UUIDs.NameManager;
import com.earth2me.essentials.User;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.hook.HookManager;
import me.droreo002.cslimit.hook.models.EssentialsHook;
import me.droreo002.oreocore.utils.misc.ThreadingUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

public final class PlayerUtils {

    private static final Map<String, UUID> CACHED_UUID = new HashMap<>();

    public static Future<UUID> getPlayerUuid(String name) {
        if (CACHED_UUID.containsKey(name)) return ThreadingUtils.makeFuture(() -> CACHED_UUID.get(name));

        return ThreadingUtils.makeFuture(() -> {
            final HookManager hookManager = ChestShopLimiter.getInstance().getHookManager();
            if (hookManager.isEssentials()) {
                User user = ((EssentialsHook) hookManager.getHookMap().get("Essentials")).getUser(name);
                return (user == null) ? null : user.getConfigUUID();
            }
            UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
            if (!Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
                // Player was never online, try getting it from ChestShop
                Account account = NameManager.getAccount(name);
                if (account == null)
                    return null;
                uuid = account.getUuid();
            }
            return uuid;
        });
    }

    /**
     * Get player name by UUID
     *
     * @param uuid The UUID
     * @return the player name if succeeded, empty string otherwise
     */
    public static String getPlayerName(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);
            if (!off.hasPlayedBefore()) {
                // Player was never online, try getting it from ChestShop
                Account account = NameManager.getAccount(uuid);
                if (account != null)
                    return account.getName();
                return "";
            }
            return off.getName();
        } else {
            return player.getName();
        }
    }
}

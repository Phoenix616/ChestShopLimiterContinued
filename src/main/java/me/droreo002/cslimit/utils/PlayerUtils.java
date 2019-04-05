package me.droreo002.cslimit.utils;

import co.aikar.taskchain.TaskChainTasks;
import com.Zrips.CMI.Containers.CMIUser;
import com.earth2me.essentials.User;
import jdk.nashorn.internal.objects.NativeUint8Array;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.hook.HookManager;
import me.droreo002.cslimit.hook.objects.CMIHook;
import me.droreo002.cslimit.hook.objects.EssentialsHook;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerUtils {

    private static final Map<String, UUID> CACHED_UUID = new HashMap<>();

    public static UUID getUUID(String name) {
        if (CACHED_UUID.containsKey(name)) return CACHED_UUID.get(name);
        final HookManager hookManager = ChestShopLimiter.getInstance().getHookManager();
        if (hookManager.isCMI()) {
            CMIUser user = ((CMIHook) hookManager.getHookMap().get("CMI")).getPlayerManager().getUser(name);
            return (user == null) ? null : user.getUniqueId();
        }
        if (hookManager.isEssentials()) {
            User user = ((EssentialsHook) hookManager.getHookMap().get("Essentials")).getUser(name);
            return (user == null) ? null : user.getConfigUUID();
        }
        OfflinePlayer[] off = new OfflinePlayer[5];
        me.droreo002.oreocore.utils.entity.PlayerUtils.getOfflinePlayer(name, input -> off[0] = input);
        UUID uuid = (off[0] == null) ? null : off[0].getUniqueId();
        if (uuid != null) CACHED_UUID.put(name, uuid);
        return uuid;
    }
}

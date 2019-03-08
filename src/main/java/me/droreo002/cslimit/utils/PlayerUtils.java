package me.droreo002.cslimit.utils;

import com.Zrips.CMI.Containers.CMIUser;
import com.earth2me.essentials.User;
import jdk.nashorn.internal.objects.NativeUint8Array;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.hook.HookManager;
import me.droreo002.cslimit.hook.objects.CMIHook;
import me.droreo002.cslimit.hook.objects.EssentialsHook;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public final class PlayerUtils {

    @SuppressWarnings("deprecation")
    public static UUID getUUID(String name) {
        final HookManager hookManager = ChestShopLimiter.getInstance().getHookManager();
        if (hookManager.isCMI()) {
            CMIUser user = ((CMIHook) hookManager.getHookMap().get("CMI")).getPlayerManager().getUser(name);
            return (user == null) ? null : user.getUniqueId();
        }
        if (hookManager.isEssentials()) {
            User user = ((EssentialsHook) hookManager.getHookMap().get("Essentials")).getUser(name);
            return (user == null) ? null : user.getConfigUUID();
        }
        OfflinePlayer off;
        try {
            off = me.droreo002.oreocore.utils.entity.PlayerUtils.getPlayer(name).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
        return (off == null) ? null : off.getUniqueId();
    }
}

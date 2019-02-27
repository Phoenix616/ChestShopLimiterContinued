package me.droreo002.cslimit.utils;

import jdk.nashorn.internal.objects.NativeUint8Array;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.hook.HookManager;
import me.droreo002.cslimit.hook.objects.CMIHook;
import me.droreo002.cslimit.hook.objects.EssentialsHook;

import java.util.UUID;

public final class PlayerUtils {

    public static boolean isPlayerAvailable(String name) {
        final HookManager hookManager = ChestShopLimiter.getInstance().getHookManager();
        if (hookManager.isCMI()) {
            return ((CMIHook) hookManager.getHookMap().get("CMI")).getPlayerManager().getUser(name) != null;
        }
        if (hookManager.isEssentials()) {
            return ((EssentialsHook) hookManager.getHookMap().get("Essentials")).getUser(name) != null;
        }
        return false;
    }
}

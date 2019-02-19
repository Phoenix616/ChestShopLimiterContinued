package me.droreo002.cslimit.utils;

import me.droreo002.cslimit.ChestShopLimiter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.UUID;

public final class CommonUtils {

    public static void registerCommand(String command, CommandExecutor clazz) {
        Bukkit.getPluginCommand(command).setExecutor(clazz);
    }

    public static void registerEvent(Listener clazz) {
        Bukkit.getPluginManager().registerEvents(clazz, ChestShopLimiter.getInstance());
    }

    public static boolean isPluginExists(String pluginName) {
        return Bukkit.getPluginManager().getPlugin(pluginName) != null;
    }

    public static String getPlayerName(UUID uuid) {
        if (Bukkit.getPlayer(uuid) != null) {
            return Bukkit.getPlayer(uuid).getName();
        } else {
            return (Bukkit.getOfflinePlayer(uuid) == null) ? "NULL" : Bukkit.getOfflinePlayer(uuid).getName();
        }
    }
}

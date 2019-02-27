package me.droreo002.cslimit.manager.logger;

import me.droreo002.cslimit.ChestShopLimiter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class Debug {

    /**
     * Log the text, into console or file
     *
     * @param text : The log message
     * @param addPrefix : Should we add prefix?
     * @param logToFile : Should we also log it to file?
     */
    public static void info(String text, boolean addPrefix, boolean logToFile) {
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        boolean allowFileLogging = plugin.getConfigManager().getMemory().isLogToFile();
        if (logToFile && allowFileLogging) {
            plugin.getLogFile().getLogger().log(Level.INFO, text);
        }
        if (addPrefix) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[ &bINFO &7] &7[ &aChestShop &7]&f " + text));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', text));
        }
    }

    /**
     * Log the text, into console or file
     *
     * @param text : The log message
     * @param addPrefix : Should we add prefix?
     * @param logToFile : Should we also log it to file?
     */
    public static void error(String text, boolean addPrefix, boolean logToFile) {
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        boolean allowFileLogging = plugin.getConfigManager().getMemory().isLogToFile();
        if (logToFile && allowFileLogging) {
            plugin.getLogFile().getLogger().log(Level.WARNING, text);
        }
        if (addPrefix) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[ &cERROR &7] &7[ &aChestShop &7]&f " + text));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', text));
        }
    }
}

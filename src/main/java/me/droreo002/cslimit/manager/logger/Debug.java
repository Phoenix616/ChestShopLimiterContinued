package me.droreo002.cslimit.manager.logger;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.oreocore.utils.strings.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class Debug {

    /**
     * Log the text, into console or file
     *
     * @param text : The log message
     * @param addPrefix : Should we add prefix?
     * @param logType : The log type
     */
    public static void info(String text, boolean addPrefix, LogType logType) {
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        boolean allowFileLogging = plugin.getCslConfig().isLogToFile();
        switch (logType) {
            case FILE:
                if (allowFileLogging) plugin.getLogFile().getLogger().log(Level.INFO, ChatColor.stripColor(StringUtils.color(text)));
                break;
            case CONSOLE:
                if (addPrefix) {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.color("&7[ &bINFO &7] &7[ &aChestShop &7]&f " + text));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.color(text));
                }
                break;
            case BOTH:
                if (allowFileLogging) plugin.getLogFile().getLogger().log(Level.INFO, ChatColor.stripColor(StringUtils.color(text)));

                if (addPrefix) {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.color("&7[ &bINFO &7] &7[ &aChestShop &7]&f " + text));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.color(text));
                }
                break;
        }
    }

    /**
     * Log the text, into console or file
     *
     * @param text : The log message
     * @param addPrefix : Should we add prefix?
     * @param logType : The log type
     */
    public static void error(String text, boolean addPrefix, LogType logType) {
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        boolean allowFileLogging = plugin.getCslConfig().isLogToFile();
        switch (logType) {
            case FILE:
                if (allowFileLogging) plugin.getLogFile().getLogger().log(Level.WARNING, ChatColor.stripColor(StringUtils.color(text)));
                break;
            case CONSOLE:
                if (addPrefix) {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.color("&7[ &cERROR &7] &7[ &aChestShop &7]&f " + text));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.color(text));
                }
                break;
            case BOTH:
                if (allowFileLogging) plugin.getLogFile().getLogger().log(Level.INFO, ChatColor.stripColor(StringUtils.color(text)));

                if (addPrefix) {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.color("&7[ &cERROR &7] &7[ &aChestShop &7]&f " + text));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.color(text));
                }
                break;
        }
    }

    public enum LogType {
        FILE,
        CONSOLE,
        BOTH;
    }
}

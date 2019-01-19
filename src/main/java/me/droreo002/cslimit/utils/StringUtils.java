package me.droreo002.cslimit.utils;

import org.bukkit.ChatColor;

public final class StringUtils {

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}

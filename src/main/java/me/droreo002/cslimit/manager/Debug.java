package me.droreo002.cslimit.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Debug {

    public static void log(String text, boolean addPrefix) {
        if (addPrefix) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[ &aChestShop &7]&f " + text));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', text));
        }
    }
}

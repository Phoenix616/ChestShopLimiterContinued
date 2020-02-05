package me.droreo002.cslimit.hook.models;

import me.droreo002.cslimit.hook.ChestShopHook;
import me.droreo002.cslimit.hook.papi.CSLPlaceholder;
import me.droreo002.cslimit.manager.logger.Debug;
import org.bukkit.Bukkit;

public class PlaceholderAPIHook implements ChestShopHook {

    @Override
    public String getPluginName() {
        return "PlaceholderAPI";
    }

    @Override
    public boolean process() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            // Register the expansion
            new CSLPlaceholder().register();
            return true;
        }
        return false;
    }

    @Override
    public void hookSuccess() {
        Debug.info("     &f> &bPlaceHolderAPI &fhas been hooked!", false, Debug.LogType.BOTH);
    }

    @Override
    public void hookFailed() {
        Debug.info("     &f> Cannot hook into &bPlaceholderAPI &fbecause the plugin cannot be found!", false, Debug.LogType.BOTH);
    }

    @Override
    public boolean disablePluginIfNotFound() {
        return false;
    }
}

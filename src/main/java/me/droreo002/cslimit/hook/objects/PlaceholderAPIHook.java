package me.droreo002.cslimit.hook.objects;

import me.droreo002.cslimit.hook.ChestShopHook;
import me.droreo002.cslimit.hook.papi.CSLPlaceholder;
import me.droreo002.cslimit.manager.Debug;
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
        Debug.log("     &f> &bPlaceHolderAPI &fhas been hooked!", false);
    }

    @Override
    public void hookFailed() {
        Debug.log("     &f> Cannot hook into &bPlaceholderAPI &fbecause the plugin cannot be found!", false);
    }

    @Override
    public boolean disablePluginIfNotFound() {
        return false;
    }
}

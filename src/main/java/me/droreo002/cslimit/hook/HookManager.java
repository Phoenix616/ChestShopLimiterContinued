package me.droreo002.cslimit.hook;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.config.ConfigMemory;
import me.droreo002.cslimit.hook.objects.CMIHook;
import me.droreo002.cslimit.hook.objects.EssentialsHook;
import me.droreo002.cslimit.hook.objects.LuckPermsHook;
import me.droreo002.cslimit.hook.objects.PlaceholderAPIHook;
import me.droreo002.cslimit.manager.Debug;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HookManager {

    private final ChestShopLimiter plugin = ChestShopLimiter.getInstance();

    private boolean isLuckPerms;
    private boolean isPlaceHolderAPI;
    private boolean isEssentials;
    private boolean isCMI;
    private Logger logger = Bukkit.getLogger();

    private Map<String, ChestShopHook> hookMap = new HashMap<>();

    public HookManager() {
        // Setup hook here
        if (plugin.getHookManager() != null) {
            throw new IllegalStateException("Cannot create a new hook manager instance anymore!. Please get this instance on the ChestShopLimiter's main class!");
        }
        // Setup all hooks here
        ConfigMemory mem = plugin.getConfigManager().getMemory();
        if (mem.isEnableEssentialsHook()) {
            Debug.log(" &fTrying to hook into &cEssentials", false);
            if (registerHook("Essentials", new EssentialsHook())) {
                setEssentials(true);
            }
        }
        if (mem.isEnableCMIHook()) {
            if (!isEssentials()) {
                Debug.log(" &fTrying to hook into &bCMI", false);
                if (registerHook("CMI", new CMIHook())) {
                    setCMI(true);
                }
            }
        }
        // Just in case
        if (!isCMI && !isEssentials) {
            Bukkit.getPluginManager().disablePlugin(ChestShopLimiter.getInstance());
            throw new IllegalStateException("Essentials or CMI dependency must be enabled an installed on the server if you want to run this plugin!. Install one of them and then run configure the config.yml to enable one of them!");
        }
        if (mem.isEnableLuckPermsHook()) {
            Debug.log(" &fTrying to hook into &aLuckperms", false);
            if (registerHook("LuckPerms", new LuckPermsHook())) {
                setLuckPerms(true);
            }
        }
        if (mem.isEnablePapiHook()) {
            Debug.log(" &fTrying to hook into &bPlaceholderAPI", false);
            if (registerHook("PlaceholderAPI", new PlaceholderAPIHook())) {
                setPlaceHolderAPI(true);
            }
        }
    }

    private boolean registerHook(String plugin, ChestShopHook hook) {
        if (hook.process()) {
            hook.hookSuccess();
            hookMap.put(plugin, hook);
            return true;
        } else {
            if (hook.disablePluginIfNotFound()) {
                hook.hookFailed();
                Bukkit.getPluginManager().disablePlugin(ChestShopLimiter.getInstance());
            } else {
                hook.hookFailed();
                return false;
            }
        }
        return false;
    }

    public Map<String, ChestShopHook> getHookMap() {
        return hookMap;
    }

    private Logger getLogger() {
        return logger;
    }

    public boolean isLuckPermsHooked() {
        return isLuckPerms;
    }

    public boolean isLuckPerms() {
        return isLuckPerms;
    }

    public boolean isPlaceHolderAPI() {
        return isPlaceHolderAPI;
    }

    public boolean isEssentials() {
        return isEssentials;
    }

    public boolean isCMI() {
        return isCMI;
    }

    public void setLuckPerms(boolean luckPerms) {
        isLuckPerms = luckPerms;
    }

    public void setPlaceHolderAPI(boolean placeHolderAPI) {
        isPlaceHolderAPI = placeHolderAPI;
    }

    public void setEssentials(boolean essentials) {
        isEssentials = essentials;
    }

    public void setCMI(boolean CMI) {
        isCMI = CMI;
    }

}

package me.droreo002.cslimit.hook;

import lombok.Getter;
import lombok.Setter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.CSLConfig;
import me.droreo002.cslimit.hook.models.EssentialsHook;
import me.droreo002.cslimit.hook.models.LuckPermsHook;
import me.droreo002.cslimit.hook.models.PlaceholderAPIHook;
import me.droreo002.cslimit.manager.logger.Debug;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HookManager {

    @Getter
    private final ChestShopLimiter plugin = ChestShopLimiter.getInstance();

    @Getter
    private final Map<String, ChestShopHook> hookMap = new HashMap<>();

    @Getter
    @Setter
    private boolean isLuckPerms;
    @Getter
    @Setter
    private boolean isPlaceHolderAPI;
    @Getter
    @Setter
    private boolean isEssentials;
    @Getter
    @Setter
    private boolean isCMI;
    @Getter
    private Logger logger = Bukkit.getLogger();

    public HookManager() {
        // Setup hook here
        if (plugin.getHookManager() != null) {
            throw new IllegalStateException("Cannot create a new hook manager instance anymore!. Please get this instance on the ChestShopLimiter's main class!");
        }
        // Setup all hooks here
        CSLConfig config = plugin.getCslConfig();
        if (config.isEnableEssentialsHook()) {
            Debug.info(" &fTrying to hook into &cEssentials", false, Debug.LogType.BOTH);
            if (registerHook("Essentials", new EssentialsHook())) {
                setEssentials(true);
            }
        }
        // Just in case
        if (!isEssentials) {
            Debug.error(" &cEssentials is not installed!, it's recommended to install one of them!", false, Debug.LogType.BOTH);
        }
        if (config.isEnableLuckPermsHook()) {
            Debug.info(" &fTrying to hook into &aLuckperms", false, Debug.LogType.BOTH);
            if (registerHook("LuckPerms", new LuckPermsHook())) {
                setLuckPerms(true);
            }
        }
        if (config.isEnablePapiHook()) {
            Debug.info(" &fTrying to hook into &bPlaceholderAPI", false, Debug.LogType.BOTH);
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
}

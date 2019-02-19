package me.droreo002.cslimit.hook.objects;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.hook.ChestShopHook;
import me.droreo002.cslimit.manager.Debug;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.UUID;

public class LuckPermsHook implements ChestShopHook {

    private LuckPermsApi luckPerms;

    @Override
    public String getPluginName() {
        return "LuckPerms";
    }

    @Override
    public boolean process() {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            luckPerms = LuckPerms.getApi();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void hookSuccess() {
        Debug.log("     &f> &aLuckPerms &fhas been hooked!", false);
    }

    @Override
    public void hookFailed() {
        Debug.log("     &f> Cannot hook into &aLuckPerms &fbecause the plugin cannot be found!", false);
    }

    @Override
    public boolean disablePluginIfNotFound() {
        return false;
    }

    public LuckPermsApi getLuckPerms() {
        return luckPerms;
    }

    public void setupData(UUID uuid, FileConfiguration config, boolean firstSetup) {
        final User user = getLuckPerms().getUser(uuid);
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        ConfigManager.Memory memory = plugin.getConfigManager().getMemory();
        ConfigurationSection lpLimit = memory.getShopLimitLuckPerms();
        DatabaseWrapper database = plugin.getDatabase().getWrapper();
        Validate.notNull(user, "LuckPerms user cannot be null!");
        String currGroup = user.getPrimaryGroup();

        if (firstSetup) {
            config.set("Data.lastRank", currGroup);
            config.set("Data.maxShop", lpLimit.getInt(currGroup + ".limit"));
        } else {
            PlayerData playerData = PlayerData.fromYaml(config);
            Validate.notNull(playerData, "Failed to retrieve player data!");
            String lastGroup = playerData.getLastRank();
            if (!lastGroup.equalsIgnoreCase(currGroup)) {
                // Not found. Setup using the default-value value
                if (!lpLimit.contains(currGroup)) {
                    playerData.setMaxShop(lpLimit.getInt("default-value.limit"));
                    plugin.getDatabase().getWrapper().updatePlayerData(playerData);
                } else {
                    // New group. Add then
                    if (!lastGroup.equalsIgnoreCase(currGroup)) {
                        playerData.setLastRank(currGroup);
                        playerData.setMaxShop(lpLimit.getInt(currGroup + ".limit"));
                        plugin.getDatabase().getWrapper().updatePlayerData(playerData);
                    }
                }
            }
        }
    }
}

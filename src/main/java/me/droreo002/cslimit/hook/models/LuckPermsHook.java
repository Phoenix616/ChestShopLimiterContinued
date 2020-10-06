package me.droreo002.cslimit.hook.models;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.CSLConfig;
import me.droreo002.cslimit.database.object.PlayerData;
import me.droreo002.cslimit.hook.ChestShopHook;
import me.droreo002.cslimit.manager.logger.Debug;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class LuckPermsHook implements ChestShopHook {

    @Getter
    private LuckPerms luckPerms;

    @Override
    public String getPluginName() {
        return "LuckPerms";
    }

    @Override
    public boolean process() {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            luckPerms = LuckPermsProvider.get();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void hookSuccess() {
        Debug.info("     &f> &aLuckPerms &fhas been hooked!", false, Debug.LogType.BOTH);
        // Subscribe to event
        EventBus eventBus = luckPerms.getEventBus();
        eventBus.subscribe(NodeAddEvent.class, event -> {
            boolean update = false;
            if (event.getNode() instanceof InheritanceNode) update = true;
            if (event.getNode() instanceof PermissionNode) update = true;
            if (event.getTarget() instanceof Group) update = false;

            if (update) {
                ChestShopLimiter plugin = ChestShopLimiter.getInstance();
                User user = (User) event.getTarget();
                PlayerData playerData = plugin.getDatabase().getWrapper().getPlayerData(user.getUniqueId());
                if (playerData == null) return;
                plugin.getDatabase().getWrapper().updatePlayerData(playerData);
            }
        });
    }

    @Override
    public void hookFailed() {
        Debug.info("     &f> Cannot hook into &aLuckPerms &fbecause the plugin cannot be found!", false, Debug.LogType.BOTH);
    }

    @Override
    public boolean disablePluginIfNotFound() {
        return false;
    }

    /**
     * Setup the data's 'ShopLimit'
     *
     * @param playerData The player data
     */
    public void setupData(PlayerData playerData) {
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        final UUID playerUUID = playerData.getPlayerUUID();
        User user = getLuckPerms().getUserManager().getUser(playerUUID);
        if (user == null) {
            user = luckPerms.getUserManager().loadUser(playerUUID).join();
            Debug.info("&eLuckPerms &fplayer data &7(&e" + playerData.toString() + "&7)&f has been force loaded, because plugin is trying to access a non cached player!", true, Debug.LogType.BOTH);
        }
        CSLConfig config = plugin.getCslConfig();
        ConfigurationSection lpLimit = config.getShopLimitLuckPerms();
        Validate.notNull(user, "LuckPerms user cannot be null!");
        String currGroup = user.getPrimaryGroup();
        if (lpLimit.contains(currGroup)) {
            playerData.setLastRank(currGroup);
            playerData.setMaxShop(lpLimit.getInt(currGroup + ".limit"));
        } else {
            playerData.setLastRank("none");
            playerData.setMaxShop(lpLimit.getInt("default-value.limit"));
        }
    }
}

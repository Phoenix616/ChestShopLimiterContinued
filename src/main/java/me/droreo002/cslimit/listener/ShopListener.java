package me.droreo002.cslimit.listener;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.ShopDestroyedEvent;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.api.ChestShopAPI;
import me.droreo002.cslimit.api.events.ShopMaxAmountReachedEvent;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.utils.bridge.ServerUtils;
import me.droreo002.oreocore.utils.item.helper.ItemMetaType;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import me.droreo002.oreocore.utils.world.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.material.Sign;

import java.util.UUID;

public class ShopListener implements Listener {

    private final ChestShopLimiter plugin;

    public ShopListener(ChestShopLimiter plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH) // Event call is of high importance, javadoc said so
    public void onCreate(PreShopCreationEvent e) {
        if (!e.isCancelled()) {
            final ChestShopAPI api = plugin.getChestShopAPI();
            final LangManager lang = plugin.getLangManager();
            Player player = e.getPlayer();
            String[] line = e.getSignLines();
            UUID uuid = player.getUniqueId();
            Sign signMaterial = (Sign) e.getSign().getBlock().getState().getData();
            Location shopLoc = e.getSign().getBlock().getRelative(signMaterial.getAttachedFace()).getLocation();
            // Ignore if the shop created is admin shop
            if (ChestShopSign.isAdminShop(line[0])) return;
            if (player.hasPermission("csl.limit.unlimited")) return;
            if (e.getOutcome().equals(PreShopCreationEvent.CreationOutcome.NOT_ENOUGH_MONEY)) return;
            final PlayerData data = api.getData(uuid);
            if (data == null) {
                player.kickPlayer("There was an unexpected error occurred when trying to get your data class");
                throw new NullPointerException("Error occurred when trying to get " + player.getName() + "'s data!. Please contact dev regarding this issue!");
            }
            int created = data.getShopCreated();
            int limit = data.getMaxShop();
            TextPlaceholder pl = new TextPlaceholder(ItemMetaType.NONE, "%created", String.valueOf(created)).add(ItemMetaType.NONE, "%max", String.valueOf(limit));

            if (created >= limit) {
                // TODO : Maybe action bar, title, sound?. Oh also, make custom event for this please << IGNORE FOR NOW
                ShopMaxAmountReachedEvent event = new ShopMaxAmountReachedEvent(player, data);
                ServerUtils.callEvent(event);

                if (!event.isCancelled()) {
                    player.sendMessage(lang.getLang(LangPath.ERROR_LIMIT_REACHED, pl, true));
                    e.setOutcome(PreShopCreationEvent.CreationOutcome.NO_PERMISSION);
                }
            } else {
                data.setShopCreated(data.getShopCreated() + 1);
                created = data.getShopCreated();
                limit = data.getMaxShop();
                pl = new TextPlaceholder(ItemMetaType.NONE, "%created", String.valueOf(created)).add(ItemMetaType.NONE, "%max", String.valueOf(limit));

                player.sendMessage(lang.getLang(LangPath.NORMAL_SHOP_CREATED, pl, true));
                data.setLastShopLocation(LocationUtils.convertToString(shopLoc));

                api.saveData(data);
            }
            Debug.info("Player " + player.getName() + ", has created a shop!. Shop created and max for the player is now (" + created + "/" + limit + ")", false, Debug.LogType.FILE);
        }
    }

    @EventHandler(priority = EventPriority.HIGH) // Event call is of high importance, javadoc said so
    public void onDestroy(ShopDestroyedEvent e) {
        final ConfigManager.Memory mem = plugin.getConfigManager().getMemory();
        final LangManager lang = plugin.getLangManager();
        Player player = e.getDestroyer();
        if (player == null) return; // Because player is null able here
        String[] lines = e.getSign().getLines();
        if (ChestShopSign.isAdminShop(lines[0])) return;

        if (!mem.isRefundOnUnlimited() && player.hasPermission("csl.limit.unlimited")) return;
        if (mem.isRefundOnRemove()) {
            final PlayerData data = plugin.getChestShopAPI().getData(player.getUniqueId());
            if (data == null) {
                player.kickPlayer("There was an unexpected error occurred when trying to get your data class");
                throw new NullPointerException("Error occurred when trying to get " + player.getName() + "'s data!. Please contact dev regarding this issue!");
            }
            if (data.getShopCreated() <= 0) return;
            data.setShopCreated(data.getShopCreated() - 1);
            int created = data.getShopCreated();
            int limit = data.getMaxShop();
            TextPlaceholder pl = new TextPlaceholder(ItemMetaType.NONE, "%created", String.valueOf(created)).add(ItemMetaType.NONE, "%max", String.valueOf(limit));
            player.sendMessage(lang.getLang(LangPath.NORMAL_SHOP_REMOVED, pl, true));

            // Remove last shop created
            Location lastShop = LocationUtils.toLocation(data.getLastShopLocation());
            Sign signMaterial = (Sign) e.getSign().getBlock().getState().getData();
            Location currentShopLocation = e.getSign().getBlock().getRelative(signMaterial.getAttachedFace()).getLocation();
            if (lastShop == null) {
                plugin.getChestShopAPI().saveData(data);
                return;
            }
            if (currentShopLocation.equals(lastShop)) {
                data.setLastShopLocation("empty");
                plugin.getChestShopAPI().saveData(data);
            }
            Debug.info("Successfully refunded 1 shop for player " + player.getName(), false, Debug.LogType.FILE);
        }
    }
}

package me.droreo002.cslimit.inventory;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.conversation.helper.ConversationType;
import me.droreo002.cslimit.conversation.helper.SessionDataKey;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.oreocore.inventory.api.CustomInventory;
import me.droreo002.oreocore.inventory.api.GUIButton;
import me.droreo002.oreocore.utils.item.CustomItem;
import me.droreo002.oreocore.utils.item.CustomSkull;
import me.droreo002.oreocore.utils.item.helper.ItemMetaType;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EditorInventory extends CustomInventory {

    public EditorInventory(Player player, ChestShopLimiter plugin) {
        super(9, plugin.getLangManager().getLang(LangPath.INVENTORY_EDITOR_TITLE, null, false));
        final LangManager lang = plugin.getLangManager();
        final ConfigManager.Memory mem = plugin.getConfigManager().getMemory();
        final PlayerData data = plugin.getChestShopAPI().getData(player.getUniqueId());

        if (data == null) throw new NullPointerException("Data cannot be null!. Please contact dev!");
        setSoundOnClick(mem.getEditorClickSound());
        setSoundOnOpen(mem.getEditorOpenSound());
        setSoundOnClose(mem.getEditorCloseSound());

        Map<ItemMetaType, TextPlaceholder> infoPlaceholder = new HashMap<>();
        infoPlaceholder.put(ItemMetaType.DISPLAY_NAME, new TextPlaceholder("%player", player.getName()));
        infoPlaceholder.put(ItemMetaType.LORE,
                new TextPlaceholder("%player", player.getName())
                .add("%maxShop", String.valueOf(data.getMaxShop()))
                .add("%shopCount", String.valueOf(data.getShopCreated())));

        ItemStack infoButton;
        try {
            infoButton = CustomSkull.toHeadAsync(CustomItem.fromSection(lang.asSection(LangPath.INVENTORY_EDITOR_INFO_BUTTON), infoPlaceholder), player).get();
        } catch (InterruptedException | ExecutionException e) {
            player.sendMessage("Something went wrong, please contact server admin!");
            e.printStackTrace();
            return;
        }
        ItemStack editMaxShopButton = CustomItem.fromSection(lang.asSection(LangPath.INVENTORY_EDITOR_EDIT_MAX_SHOP_BUTTON), null);
        ItemStack editShopCountButton = CustomItem.fromSection(lang.asSection(LangPath.INVENTORY_EDITOR_EDIT_SHOP_COUNT), null);

        addButton(0, new GUIButton(infoButton).setListener(GUIButton.CLOSE_LISTENER), true);
        addButton(1, new GUIButton(CustomItem.GRAY_GLASSPANE).setListener(GUIButton.CLOSE_LISTENER), true);
        addButton(2, new GUIButton(editMaxShopButton).setListener(inventoryClickEvent -> {
            ClickType click = inventoryClickEvent.getClick();
            if (click == ClickType.LEFT) {
                Map<SessionDataKey, Object> sessionData = new HashMap<>();
                sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.CHANGE_MAX_SHOP);
                sessionData.put(SessionDataKey.PLAYER_DATA, data);

                plugin.getConversationManager().sendConversation(player, ConversationType.CHANGE_MAX_SHOP, sessionData);
                close(player);
            }
            if (click == ClickType.RIGHT) {
                Map<SessionDataKey, Object> sessionData = new HashMap<>();
                sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.ADD_MAX_SHOP);
                sessionData.put(SessionDataKey.PLAYER_DATA, data);

                plugin.getConversationManager().sendConversation(player, ConversationType.ADD_MAX_SHOP, sessionData);
                close(player);
            }
        }), true);
        addButton(3, new GUIButton(editShopCountButton).setListener(inventoryClickEvent -> {
            ClickType click = inventoryClickEvent.getClick();
            if (click == ClickType.LEFT) {
                Map<SessionDataKey, Object> sessionData = new HashMap<>();
                sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.CHANGE_CURRENT_SHOP);
                sessionData.put(SessionDataKey.PLAYER_DATA, data);

                plugin.getConversationManager().sendConversation(player, ConversationType.CHANGE_CURRENT_SHOP, sessionData);
                close(player);
            }
            if (click == ClickType.RIGHT) {
                Map<SessionDataKey, Object> sessionData = new HashMap<>();
                sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.ADD_CURRENT_SHOP);
                sessionData.put(SessionDataKey.PLAYER_DATA, data);

                plugin.getConversationManager().sendConversation(player, ConversationType.ADD_CURRENT_SHOP, sessionData);
                close(player);
            }
        }), true);
    }

    @Override
    public void onClick(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }

    @Override
    public void onOpen(InventoryOpenEvent inventoryOpenEvent) {

    }
}

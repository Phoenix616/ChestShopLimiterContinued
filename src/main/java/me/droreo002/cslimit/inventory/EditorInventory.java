package me.droreo002.cslimit.inventory;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.conversation.helper.ConversationType;
import me.droreo002.cslimit.conversation.helper.SessionDataKey;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.oreocore.inventory.CustomInventory;
import me.droreo002.oreocore.inventory.button.GUIButton;
import me.droreo002.oreocore.utils.item.CustomItem;
import me.droreo002.oreocore.utils.item.CustomSkull;
import me.droreo002.oreocore.utils.item.helper.ItemMetaType;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EditorInventory extends CustomInventory {

    public EditorInventory(Player opener, Player targetPlayer, ChestShopLimiter plugin) {
        super(9, plugin.getLangManager().getLang(LangPath.INVENTORY_EDITOR_TITLE, null, false));
        final LangManager lang = plugin.getLangManager();
        final ConfigManager.Memory mem = plugin.getConfigManager().getMemory();
        final PlayerData data = plugin.getChestShopAPI().getData(targetPlayer.getUniqueId());

        if (data == null) throw new NullPointerException("Data cannot be null!. Please contact dev!");
        setSoundOnClick(mem.getEditorClickSound());
        setSoundOnOpen(mem.getEditorOpenSound());
        setSoundOnClose(mem.getEditorCloseSound());

        TextPlaceholder infoPlaceholder = new TextPlaceholder(ItemMetaType.DISPLAY_NAME, "%player", targetPlayer.getName())
                .add(ItemMetaType.LORE, "%player", targetPlayer.getName())
                .add(ItemMetaType.LORE, "%maxShop", String.valueOf(data.getMaxShop()))
                .add(ItemMetaType.LORE,"%shopCount", String.valueOf(data.getShopCreated()));

        ItemStack infoButton = CustomItem.applyFromSection(CustomSkull.getHead(targetPlayer.getUniqueId()), lang.asSection(LangPath.INVENTORY_EDITOR_INFO_BUTTON), infoPlaceholder);
        ItemStack editMaxShopButton = CustomItem.fromSection(lang.asSection(LangPath.INVENTORY_EDITOR_EDIT_MAX_SHOP_BUTTON), null);
        ItemStack editShopCountButton = CustomItem.fromSection(lang.asSection(LangPath.INVENTORY_EDITOR_EDIT_SHOP_COUNT), null);

        addButton(new GUIButton(infoButton, 0).setListener(GUIButton.CLOSE_LISTENER), true);
        addButton(new GUIButton(CustomItem.GRAY_GLASSPANE, 1).setListener(GUIButton.CLOSE_LISTENER), true);
        addButton(new GUIButton(editMaxShopButton, 2).setListener(inventoryClickEvent -> {
            ClickType click = inventoryClickEvent.getClick();
            if (click == ClickType.LEFT) {
                Map<SessionDataKey, Object> sessionData = new HashMap<>();
                sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.CHANGE_MAX_SHOP);
                sessionData.put(SessionDataKey.PLAYER_DATA, data);

                plugin.getConversationManager().sendConversation(opener, ConversationType.CHANGE_MAX_SHOP, sessionData);
                closeInventory(opener);
            }
            if (click == ClickType.RIGHT) {
                Map<SessionDataKey, Object> sessionData = new HashMap<>();
                sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.ADD_MAX_SHOP);
                sessionData.put(SessionDataKey.PLAYER_DATA, data);

                plugin.getConversationManager().sendConversation(opener, ConversationType.ADD_MAX_SHOP, sessionData);
                closeInventory(opener);
            }
        }), true);
        addButton(new GUIButton(editShopCountButton, 3).setListener(inventoryClickEvent -> {
            ClickType click = inventoryClickEvent.getClick();
            if (click == ClickType.LEFT) {
                Map<SessionDataKey, Object> sessionData = new HashMap<>();
                sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.CHANGE_CURRENT_SHOP);
                sessionData.put(SessionDataKey.PLAYER_DATA, data);

                plugin.getConversationManager().sendConversation(opener, ConversationType.CHANGE_CURRENT_SHOP, sessionData);
                closeInventory(opener);
            }
            if (click == ClickType.RIGHT) {
                Map<SessionDataKey, Object> sessionData = new HashMap<>();
                sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.ADD_CURRENT_SHOP);
                sessionData.put(SessionDataKey.PLAYER_DATA, data);

                plugin.getConversationManager().sendConversation(opener, ConversationType.ADD_CURRENT_SHOP, sessionData);
                closeInventory(opener);
            }
        }), true);
    }
}

package me.droreo002.cslimit.inventory;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.inventory.CustomInventory;
import me.droreo002.oreocore.inventory.button.GUIButton;
import me.droreo002.oreocore.utils.item.CustomItem;
import me.droreo002.oreocore.utils.item.complex.UMaterial;
import me.droreo002.oreocore.utils.misc.ThreadingUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class MenuInventory extends CustomInventory {

    public MenuInventory(ChestShopLimiter plugin) {
        super(9, plugin.getLangManager().getLang(LangPath.INVENTORY_MAIN_MENU_TITLE, null, false));
        final LangManager lang = plugin.getLangManager();
        final ConfigManager.Memory mem = plugin.getConfigManager().getMemory();

        ItemStack editorButton = CustomItem.fromSection(lang.asSection(LangPath.INVENTORY_MAIN_MENU_EDITOR_BUTTON), null);

        setSoundOnClick(mem.getMainMenuClickSound());
        setSoundOnClose(mem.getMainMenuCloseSound());
        setSoundOnOpen(mem.getMainMenuOpenSound());

        addButton(new GUIButton(CustomItem.fromSection(lang.asSection(LangPath.INVENTORY_MAIN_MENU_EXIT_BUTTON), null), 0).setListener(GUIButton.CLOSE_LISTENER), true);
        addButton(new GUIButton(CustomItem.GRAY_GLASSPANE, 1).setListener(GUIButton.CLOSE_LISTENER), true);
        addButton(new GUIButton(editorButton, 2).setListener(inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();
            closeInventory(player);

            ThreadingUtils.makeChain().asyncFirst(() -> new SelectorInventory(lang.getLang(LangPath.INVENTORY_PLAYER_SELECTOR_TITLE,
                    null,
                    false),
            (e, item, targetPlayer) -> {
                if (item.getType().equals(UMaterial.PLAYER_HEAD_ITEM.getMaterial())) {
                    closeInventory(player);
                    ThreadingUtils.makeChain().asyncFirst(() -> new EditorInventory(player, targetPlayer, plugin)).asyncLast(input -> input.open(player)).execute();
                }
            })).asyncLast(input -> input.open(player)).execute();

        }), true);
    }

    @Override
    public void onOpen(InventoryOpenEvent inventoryOpenEvent) {
        Player player = (Player) inventoryOpenEvent.getPlayer();
        Debug.info("Player " + player.getName() + " has opened the MenuInventory!", false, Debug.LogType.FILE);
    }
}

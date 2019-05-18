package me.droreo002.cslimit.inventory;

import com.sun.org.apache.bcel.internal.generic.LADD;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.enums.XMaterial;
import me.droreo002.oreocore.inventory.api.CustomInventory;
import me.droreo002.oreocore.inventory.api.GUIButton;
import me.droreo002.oreocore.utils.item.CustomItem;
import me.droreo002.oreocore.utils.item.CustomSkull;
import me.droreo002.oreocore.utils.item.complex.UMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
            new SelectorInventory(lang.getLang(LangPath.INVENTORY_PLAYER_SELECTOR_TITLE, null, false), (e, item, targetPlayer) -> {
                if (item.getType().equals(UMaterial.PLAYER_HEAD_ITEM.getMaterial())) {
                    closeInventory(player);
                    new EditorInventory(player, targetPlayer, plugin).openAsync(player); // In case that there's head
                }
            }).openAsync(player, 1);
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
        Player player = (Player) inventoryOpenEvent.getPlayer();
        Debug.info("Player " + player.getName() + " has opened the MenuInventory!", false, Debug.LogType.FILE);
    }
}

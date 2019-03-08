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

        addButton(0, new GUIButton(CustomItem.fromSection(lang.asSection(LangPath.INVENTORY_MAIN_MENU_EXIT_BUTTON), null)).setListener(GUIButton.CLOSE_LISTENER), true);
        addButton(1, new GUIButton(CustomItem.GRAY_GLASSPANE).setListener(GUIButton.CLOSE_LISTENER), true);
        addButton(2, new GUIButton(editorButton).setListener(inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();
            close(player);
            new SelectorInventory(lang.getLang(LangPath.INVENTORY_PLAYER_SELECTOR_TITLE, null, false), (e, item, p) -> {
                if (item.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())) {
                    close(player);
                    new EditorInventory(p, plugin).open(player);
                }
            }).open(player);
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

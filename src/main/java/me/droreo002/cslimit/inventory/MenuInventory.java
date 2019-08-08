package me.droreo002.cslimit.inventory;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.config.InventoryTemplates;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.inventory.CustomInventory;
import me.droreo002.oreocore.inventory.InventoryTemplate;
import me.droreo002.oreocore.inventory.animation.InventoryAnimation;
import me.droreo002.oreocore.inventory.button.GUIButton;
import me.droreo002.oreocore.utils.item.complex.UMaterial;
import me.droreo002.oreocore.utils.misc.ThreadingUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuInventory extends CustomInventory {

    public MenuInventory(InventoryTemplates templates, ChestShopLimiter plugin) {
        super(templates.getMainMenuTemplate());
        final ConfigManager.Memory mem = plugin.getConfigManager().getMemory();
        final InventoryTemplate template = templates.getMainMenuTemplate();
        GUIButton exitButton = template.getGUIButtons("C").get(0);

        setSoundOnClick(mem.getMainMenuClickSound());
        setSoundOnClose(mem.getMainMenuCloseSound());
        setSoundOnOpen(mem.getMainMenuOpenSound());

        exitButton.setListener(GUIButton.CLOSE_LISTENER);
        template.applyListener("E", inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();
            closeInventory(player);

            ThreadingUtils.makeChain().asyncFirst(() -> new SelectorInventory(templates.getPlayerSelectorTemplate(), plugin,
                (e, item, targetPlayer) -> {
                    if (item.getType().equals(UMaterial.PLAYER_HEAD_ITEM.getMaterial())) {
                        closeInventory(player);
                        ThreadingUtils.makeChain().asyncFirst(() -> new EditorInventory(player, targetPlayer, plugin, templates.getEditorInventoryTemplate())).asyncLast(input -> input.open(player)).execute();
                    }
                }
            )).asyncLast(input -> input.open(player)).execute();
        });
    }

    @Override
    public void onOpen(InventoryOpenEvent inventoryOpenEvent) {
        Player player = (Player) inventoryOpenEvent.getPlayer();
        Debug.info("Player " + player.getName() + " has opened the MenuInventory!", false, Debug.LogType.FILE);
    }
}

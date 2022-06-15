package me.droreo002.cslimit.inventory;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.CSLConfig;
import me.droreo002.cslimit.config.InventoryTemplates;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.inventory.InventoryTemplate;
import me.droreo002.oreocore.inventory.OreoInventory;
import me.droreo002.oreocore.inventory.button.GUIButton;
import me.droreo002.oreocore.utils.misc.ThreadingUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuInventory extends OreoInventory {

    public MenuInventory(InventoryTemplates templates, ChestShopLimiter plugin) {
        super(templates.getMainMenuTemplate());
        final CSLConfig config = plugin.getCslConfig();
        final InventoryTemplate template = templates.getMainMenuTemplate().clone();
        GUIButton exitButton = template.getGUIButtons("C").get(0);

        setSoundOnClick(config.getMainMenuClickSound());
        setSoundOnClose(config.getMainMenuCloseSound());
        setSoundOnOpen(config.getMainMenuOpenSound());

        exitButton.addListener(GUIButton.CLOSE_LISTENER);
        template.applyListener("E", inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();
            closeInventory(player);

            ThreadingUtils.makeChain().asyncFirst(() -> new SelectorInventory(templates.getPlayerSelectorTemplate().clone(), plugin,
                (e, item, targetPlayer) -> {
                    if (item.getType().equals(Material.PLAYER_HEAD)) {
                        closeInventory(player);
                        ThreadingUtils.makeChain().asyncFirst(() -> new EditorInventory(player, targetPlayer, plugin, templates.getEditorInventoryTemplate().clone()))
                                .asyncLast(input -> input.open(player)).execute();
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

package me.droreo002.cslimit.inventory;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.oreocore.enums.XMaterial;
import me.droreo002.oreocore.inventory.api.GUIButton;
import me.droreo002.oreocore.inventory.api.paginated.PaginatedInventory;
import me.droreo002.oreocore.utils.item.CustomItem;
import me.droreo002.oreocore.utils.item.CustomSkull;
import me.droreo002.oreocore.utils.item.helper.ItemMetaType;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SelectorInventory extends PaginatedInventory {

    // Callbacks
    @Getter
    private Selected selected;

    public SelectorInventory(String title, Selected selected) {
        super(45, title);
        this.selected = selected;
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        final LangManager lang = plugin.getLangManager();
        final ConfigManager.Memory mem = plugin.getConfigManager().getMemory();

        setOpenSound(mem.getPSelectorOpenSound());
        setClickSound(mem.getPSelectorClickSound());
        setCloseSound(mem.getPSelectorCloseSound());

        setSearchRow(4, true, new ItemStack(XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial()));
        setItemSlot(0, 1, 2, 3);

        // TODO : Try the conversation api!

        for (Player p : Bukkit.getOnlinePlayers()) {
            final Map<ItemMetaType, TextPlaceholder> placeholder = new HashMap<>();
            placeholder.put(ItemMetaType.DISPLAY_NAME, new TextPlaceholder("%player", p.getName()));
            ItemStack head;
            try {
                head = CustomSkull.toHeadAsync(CustomItem.fromSection(lang.asSection(LangPath.INVENTORY_PLAYER_SELECTOR_PLAYER_BUTTON), placeholder), p).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return;
            }
            addPaginatedButton(new GUIButton(head).setListener(inventoryClickEvent -> {
                ItemStack curr = inventoryClickEvent.getCurrentItem();
                Bukkit.getScheduler().scheduleSyncDelayedTask(ChestShopLimiter.getInstance(), () -> selected.selected(inventoryClickEvent, curr, p), 1L);
            }));
        }
    }

    public interface Selected {
        void selected(InventoryClickEvent e, ItemStack item, Player selected);
    }
}

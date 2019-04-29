package me.droreo002.cslimit.inventory;

import com.earth2me.essentials.textreader.TextPager;
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
import me.droreo002.oreocore.utils.item.ItemUtils;
import me.droreo002.oreocore.utils.item.helper.ItemMetaType;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import me.droreo002.oreocore.utils.strings.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
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
        setItemRow(0, 1, 2, 3);

        // INFO : NO NEED ASYNC. BECAUSE WE"RE ALREADY OPENED THIS INVENTORY VIA ASYNC WAY
        for (Player player : Bukkit.getOnlinePlayers()) {
            final TextPlaceholder placeholder = new TextPlaceholder(ItemMetaType.DISPLAY_NAME, "%player", player.getName());
            ItemStack head = CustomSkull.fromSection(lang.asSection(LangPath.INVENTORY_PLAYER_SELECTOR_PLAYER_BUTTON), placeholder, player.getUniqueId());
            addPaginatedButton(new GUIButton(head).setListener(inventoryClickEvent -> {
                ItemStack curr = inventoryClickEvent.getCurrentItem();
                Bukkit.getScheduler().scheduleSyncDelayedTask(ChestShopLimiter.getInstance(), () -> selected.selected(inventoryClickEvent, curr, player), 1L);
            }));
        }
    }

    public interface Selected {
        void selected(InventoryClickEvent e, ItemStack item, Player selected);
    }
}

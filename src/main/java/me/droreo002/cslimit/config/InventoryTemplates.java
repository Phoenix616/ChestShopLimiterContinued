package me.droreo002.cslimit.config;

import lombok.Getter;
import lombok.NonNull;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.configuration.ConfigurationMemory;
import me.droreo002.oreocore.configuration.CustomConfiguration;
import me.droreo002.oreocore.configuration.annotations.ConfigVariable;
import me.droreo002.oreocore.inventory.InventoryTemplate;
import me.droreo002.oreocore.inventory.button.GUIButton;

import java.io.File;

public class InventoryTemplates extends CustomConfiguration implements ConfigurationMemory {

    private static final String LATEST_VERSION = "1.0";

    @Getter
    @ConfigVariable(path = "Inventory.MainMenu", isSerializableObject = true)
    private InventoryTemplate mainMenuTemplate;

    @Getter
    @ConfigVariable(path = "Inventory.PlayerSelector", isSerializableObject = true)
    private InventoryTemplate playerSelectorTemplate;

    @Getter
    @ConfigVariable(path = "Inventory.EditorInventory", isSerializableObject = true)
    private InventoryTemplate editorInventoryTemplate;

    @Getter
    @ConfigVariable(path = "Inventory.PlayerSelector.playerHeadButton", isSerializableObject = true)
    private GUIButton playerHeadButton;

    public InventoryTemplates(ChestShopLimiter plugin) {
        super(plugin, new File(plugin.getDataFolder(), "inventory-settings.yml"));
        registerMemory(this);
        if (tryUpdate("ConfigVersion", LATEST_VERSION)) {
            Debug.info("&7inventory-settings.yml &fhas been updated!", true, Debug.LogType.BOTH);
        }
    }

    @Override
    public @NonNull CustomConfiguration getParent() {
        return this;
    }
}

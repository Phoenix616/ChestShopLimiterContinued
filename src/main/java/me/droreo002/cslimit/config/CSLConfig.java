package me.droreo002.cslimit.config;

import lombok.Getter;
import lombok.NonNull;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.configuration.Configuration;
import me.droreo002.oreocore.configuration.ConfigurationMemory;
import me.droreo002.oreocore.configuration.CustomConfiguration;
import me.droreo002.oreocore.configuration.annotations.ConfigVariable;
import me.droreo002.oreocore.database.DatabaseType;
import me.droreo002.oreocore.database.utils.SQLConfiguration;
import me.droreo002.oreocore.title.OreoTitle;
import me.droreo002.oreocore.utils.misc.SoundObject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public final class CSLConfig extends CustomConfiguration implements ConfigurationMemory {

    private static final String LATEST_VERSION = "1.2";

    /*
    Misc
     */
    @ConfigVariable(path = "Prefix")
    @Getter
    private String prefix;

    @ConfigVariable(path = "use-bstats")
    @Getter
    private boolean useBstats;

    @ConfigVariable(path = "Language")
    @Getter
    private String langFile;

    @ConfigVariable(path = "RefundOnShopRemove")
    @Getter
    private boolean refundOnRemove;

    @ConfigVariable(path = "RefundOnShopRemove-Unlimited")
    @Getter
    private boolean refundOnUnlimited;

    /*
    Dependency
     */
    @ConfigVariable(path = "Dependency.Essentials")
    @Getter
    private boolean enableEssentialsHook;

    @ConfigVariable(path = "Dependency.PlaceholderAPI")
    @Getter
    private boolean enablePapiHook;

    @ConfigVariable(path = "Dependency.LuckPerms")
    @Getter
    private boolean enableLuckPermsHook;

    @ConfigVariable(path = "Dependency.CMI")
    @Getter
    private boolean enableCMIHook;

    /*
    CommandSound
     */
    @ConfigVariable(path = "CommandSound.success", isSerializableObject = true)
    @Getter
    private SoundObject successSound;

    @ConfigVariable(path = "CommandSound.failure", isSerializableObject = true)
    @Getter
    private SoundObject failureSound;

        /*
        Inventory sound
         */

    // Main menu
    @ConfigVariable(path = "InventorySound.MainMenu.click", isSerializableObject = true)
    @Getter
    private SoundObject mainMenuClickSound;
    @ConfigVariable(path = "InventorySound.MainMenu.open", isSerializableObject = true)
    @Getter
    private SoundObject mainMenuOpenSound;
    @ConfigVariable(path = "InventorySound.MainMenu.close", isSerializableObject = true)
    @Getter
    private SoundObject mainMenuCloseSound;

    // PlayerSelector
    @ConfigVariable(path = "InventorySound.PlayerSelector.click", isSerializableObject = true)
    @Getter
    private SoundObject pSelectorClickSound;
    @ConfigVariable(path = "InventorySound.PlayerSelector.open", isSerializableObject = true)
    @Getter
    private SoundObject pSelectorOpenSound;
    @ConfigVariable(path = "InventorySound.PlayerSelector.close", isSerializableObject = true)
    @Getter
    private SoundObject pSelectorCloseSound;

    // Editor
    @ConfigVariable(path = "InventorySound.Editor.click", isSerializableObject = true)
    @Getter
    private SoundObject editorClickSound;
    @ConfigVariable(path = "InventorySound.Editor.close", isSerializableObject = true)
    @Getter
    private SoundObject editorCloseSound;
    @ConfigVariable(path = "InventorySound.Editor.open", isSerializableObject = true)
    @Getter
    private SoundObject editorOpenSound;

    /*
    TextEditorSound
     */
    @ConfigVariable(path = "TextEditorSound.success", isSerializableObject = true)
    @Getter
    private SoundObject tEditorSuccessSound;
    @ConfigVariable(path = "TextEditorSound.failure", isSerializableObject = true)
    @Getter
    private SoundObject tEditorFailureSound;

    /*
    Logging
     */
    @ConfigVariable(path = "Debugging.LogToFile")
    @Getter
    private boolean logToFile;

    @ConfigVariable(path = "Debugging.LogFormat")
    @Getter
    private String logFormat;

    /*
    ShopLimit
     */
    @ConfigVariable(path = "ShopLimit", errorWhenNull = true)
    @Getter
    private ConfigurationSection shopLimit;

    /*
    ShopLimitLuckPerms
     */
    @ConfigVariable(path = "ShopLimitLuckPerms", errorWhenNull = true)
    @Getter
    private ConfigurationSection shopLimitLuckPerms;

    /*
    Databases
     */
    @ConfigVariable(path = "Database.DatabaseType")
    @Getter
    private DatabaseType databaseType;

    @ConfigVariable(path = "Database.Sql", isSerializableObject = true)
    @Getter
    private SQLConfiguration sqlConfiguration;

    @ConfigVariable(path = "Database.FlatFile.DatabaseFolder")
    @Getter
    private String flatFileDatabaseFolder;

    /*
    Titles
     */
    // Booleans
    @ConfigVariable(path = "Titles.MaxShopReached.enable")
    @Getter
    private boolean tMaxShopReachedEnable;

    // Object
    @ConfigVariable(path = "Titles.MaxShopReached", isSerializableObject = true)
    @Getter
    private OreoTitle tMaxShopReached;

    @Getter
    private final ChestShopLimiter plugin;

    public CSLConfig(ChestShopLimiter plugin) {
        super(plugin, new File(plugin.getDataFolder(), "config.yml"));
        this.plugin = plugin;
        registerMemory(this);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (tryUpdate("ConfigVersion", LATEST_VERSION)) {
                Debug.info("&7config.yml &fhas been updated!", true, Debug.LogType.BOTH);
            }
        }, 40L);
    }

    @Override
    public @NonNull Configuration getParent() {
        return this;
    }
}

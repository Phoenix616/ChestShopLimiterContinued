package me.droreo002.cslimit.config;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.configuration.ConfigurationMemory;
import me.droreo002.oreocore.configuration.CustomConfiguration;
import me.droreo002.oreocore.configuration.annotations.ConfigVariable;
import me.droreo002.oreocore.database.DatabaseType;
import me.droreo002.oreocore.database.SQLType;
import me.droreo002.oreocore.database.utils.MySqlConnection;
import me.droreo002.oreocore.title.OreoTitle;
import me.droreo002.oreocore.utils.misc.SoundObject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public final class ConfigManager extends CustomConfiguration {

    private static final String LATEST_VERSION = "1.1";

    @Getter
    private Memory memory;
    @Getter
    private final ChestShopLimiter plugin;

    public ConfigManager(ChestShopLimiter plugin) {
        super(plugin, new File(plugin.getDataFolder(), "config.yml"));
        this.plugin = plugin;
        this.memory = new Memory(this);
        registerMemory(memory);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (tryUpdate("ConfigVersion", LATEST_VERSION)) {
                Debug.info("&7config.yml &fhas been updated!", true, Debug.LogType.BOTH);
            }
        }, 40L);
    }

    public static class Memory implements ConfigurationMemory {

        @Getter
        private final CustomConfiguration customConfig;

        // Only add @Setter for those variables that needed

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

        // MySQL
        @ConfigVariable(path = "Database.MySQL", isSerializableObject = true)
        @Getter
        private MySqlConnection mySqlConnection = new MySqlConnection();

        @ConfigVariable(path = "Database.MySQL.saveEvery")
        @Getter
        private int mysqlSaveTime;

        @ConfigVariable(path = "Database.MySQL.Type")
        @Getter
        private SQLType mySqlDatabaseType;

        @ConfigVariable(path = "Database.MySQL.CompletelyAsync")
        @Getter
        private boolean mySqlCompletelyAsync;

        // FLAT_FILE
        @ConfigVariable(path = "Database.FlatFile.DatabaseFolder")
        @Getter
        private String flatFileDatabaseFolder;

        // SQL
        @ConfigVariable(path = "Database.SQL.DatabaseName")
        @Getter
        private String sqlDatabaseName;

        @ConfigVariable(path = "Database.SQL.DatabaseFolder")
        @Getter
        private String sqlDatabaseFolder;

        @ConfigVariable(path = "Database.SQL.ForceUpdate")
        @Getter
        private boolean sqlForceUpdate;

        @ConfigVariable(path = "Database.SQL.Type")
        @Getter
        private SQLType sqlDatabaseType;

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

        Memory(CustomConfiguration customConfig) {
            this.customConfig = customConfig;
        }

        @Override
        public CustomConfiguration getParent() {
            return customConfig;
        }
    }
}

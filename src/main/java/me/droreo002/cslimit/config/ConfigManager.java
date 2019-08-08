package me.droreo002.cslimit.config;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.configuration.ConfigMemory;
import me.droreo002.oreocore.configuration.CustomConfig;
import me.droreo002.oreocore.configuration.annotations.ConfigVariable;
import me.droreo002.oreocore.database.DatabaseType;
import me.droreo002.oreocore.database.SQLType;
import me.droreo002.oreocore.database.utils.MySqlConnection;
import me.droreo002.oreocore.utils.misc.SoundObject;
import me.droreo002.oreocore.utils.misc.TitleObject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public final class ConfigManager extends CustomConfig {

    private static final String LATEST_VERSION = "1.0";

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

    public class Memory implements ConfigMemory {

        @Getter
        private final CustomConfig customConfig;

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
        private SoundObject successSound = new SoundObject();

        @ConfigVariable(path = "CommandSound.failure", isSerializableObject = true)
        @Getter
        private SoundObject failureSound = new SoundObject();

        /*
        Inventory sound
         */

        // Main menu
        @ConfigVariable(path = "InventorySound.MainMenu.click", isSerializableObject = true)
        @Getter
        private SoundObject mainMenuClickSound = new SoundObject();
        @ConfigVariable(path = "InventorySound.MainMenu.open", isSerializableObject = true)
        @Getter
        private SoundObject mainMenuOpenSound = new SoundObject();
        @ConfigVariable(path = "InventorySound.MainMenu.close", isSerializableObject = true)
        @Getter
        private SoundObject mainMenuCloseSound = new SoundObject();

        // PlayerSelector
        @ConfigVariable(path = "InventorySound.PlayerSelector.click", isSerializableObject = true)
        @Getter
        private SoundObject pSelectorClickSound = new SoundObject();
        @ConfigVariable(path = "InventorySound.PlayerSelector.open", isSerializableObject = true)
        @Getter
        private SoundObject pSelectorOpenSound = new SoundObject();
        @ConfigVariable(path = "InventorySound.PlayerSelector.close", isSerializableObject = true)
        @Getter
        private SoundObject pSelectorCloseSound = new SoundObject();

        // Editor
        @ConfigVariable(path = "InventorySound.Editor.click", isSerializableObject = true)
        @Getter
        private SoundObject editorClickSound = new SoundObject();
        @ConfigVariable(path = "InventorySound.Editor.close", isSerializableObject = true)
        @Getter
        private SoundObject editorCloseSound = new SoundObject();
        @ConfigVariable(path = "InventorySound.Editor.open", isSerializableObject = true)
        @Getter
        private SoundObject editorOpenSound = new SoundObject();

        /*
        TextEditorSound
         */
        @ConfigVariable(path = "TextEditorSound.success", isSerializableObject = true)
        @Getter
        private SoundObject tEditorSuccessSound = new SoundObject();
        @ConfigVariable(path = "TextEditorSound.failure", isSerializableObject = true)
        @Getter
        private SoundObject tEditorFailureSound = new SoundObject();

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
        private TitleObject tMaxShopReached = new TitleObject();

        Memory(CustomConfig customConfig) {
            this.customConfig = customConfig;
        }

        @Override
        public CustomConfig getParent() {
            return customConfig;
        }
    }
}

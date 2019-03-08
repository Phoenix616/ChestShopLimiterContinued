package me.droreo002.cslimit.config;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.oreocore.configuration.ConfigMemory;
import me.droreo002.oreocore.configuration.ConfigMemoryManager;
import me.droreo002.oreocore.configuration.CustomConfig;
import me.droreo002.oreocore.configuration.annotations.ConfigVariable;
import me.droreo002.oreocore.database.DatabaseType;
import me.droreo002.oreocore.database.SQLType;
import me.droreo002.oreocore.database.utils.MySqlConnection;
import me.droreo002.oreocore.utils.misc.SoundObject;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public final class ConfigManager extends CustomConfig {

    @Getter
    private Memory memory;
    @Getter
    private final ChestShopLimiter plugin;

    public ConfigManager(ChestShopLimiter plugin) {
        super(plugin, new File(plugin.getDataFolder(), "config.yml"));
        this.plugin = plugin;
        this.memory = new Memory(this);
        ConfigMemoryManager.registerMemory(plugin, memory);
    }

    public void reload() {
        super.reloadConfig();
        this.memory = new Memory(this);
        ConfigMemoryManager.reloadMemory(plugin, memory);
    }

    public class Memory implements ConfigMemory {

        @Getter
        private final CustomConfig customConfig;

        // Only add @Setter for those variables that needed

        /*
        Misc
         */
        @ConfigVariable(path = "Prefix", isSerializableObject = false)
        @Getter
        private String prefix;

        @ConfigVariable(path = "use-bstats", isSerializableObject = false)
        @Getter
        private boolean useBstats;

        @ConfigVariable(path = "Language", isSerializableObject = false)
        @Getter
        private String langFile;

        @ConfigVariable(path = "RefundOnShopRemove", isSerializableObject = false)
        @Getter
        private boolean refundOnRemove;

        @ConfigVariable(path = "RefundOnShopRemove-Unlimited", isSerializableObject = false)
        @Getter
        private boolean refundOnUnlimited;

        /*
        Dependency
         */
        @ConfigVariable(path = "Dependency.Essentials", isSerializableObject = false)
        @Getter
        private boolean enableEssentialsHook;

        @ConfigVariable(path = "Dependency.PlaceholderAPI", isSerializableObject = false)
        @Getter
        private boolean enablePapiHook;

        @ConfigVariable(path = "Dependency.LuckPerms", isSerializableObject = false)
        @Getter
        private boolean enableLuckPermsHook;

        @ConfigVariable(path = "Dependency.CMI", isSerializableObject = false)
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
        @ConfigVariable(path = "Debugging.LogToFile", isSerializableObject = false)
        @Getter
        private boolean logToFile;

        @ConfigVariable(path = "Debugging.LogFormat", isSerializableObject = false)
        @Getter
        private String logFormat;

        /*
        ShopLimit
         */
        @ConfigVariable(path = "ShopLimit", errorWhenNull = true, isSerializableObject = false)
        @Getter
        private ConfigurationSection shopLimit;

        @ConfigVariable(path = "ShopLimit.force-default", isSerializableObject = false)
        @Getter
        private boolean shopLimitForceDefault;

        /*
        ShopLimitLuckPerms
         */
        @ConfigVariable(path = "ShopLimitLuckPerms", errorWhenNull = true, isSerializableObject = false)
        @Getter
        private ConfigurationSection shopLimitLuckPerms;

        /*
        Databases
         */
        @ConfigVariable(path = "Database.DatabaseType", isSerializableObject = false)
        @Getter
        private DatabaseType databaseType;

        // MySQL
        @ConfigVariable(path = "Database.MySQL", isSerializableObject = true)
        @Getter
        private MySqlConnection mySqlConnection = new MySqlConnection();

        @ConfigVariable(path = "Database.MySQL.saveEvery", isSerializableObject = false)
        @Getter
        private int mysqlSaveTime;

        @ConfigVariable(path = "Database.MySQL.Type", isSerializableObject = false)
        @Getter
        private SQLType mySqlDatabaseType;

        @ConfigVariable(path = "Database.MySQL.CompletelyAsync", isSerializableObject = false)
        @Getter
        private boolean mySqlCompletelyAsync;

        // FLAT_FILE
        @ConfigVariable(path = "Database.FlatFile.DatabaseFolder", isSerializableObject = false)
        @Getter
        private String flatFileDatabaseFolder;

        // SQL
        @ConfigVariable(path = "Database.SQL.DatabaseName", isSerializableObject = false)
        @Getter
        private String sqlDatabaseName;

        @ConfigVariable(path = "Database.SQL.DatabaseFolder", isSerializableObject = false)
        @Getter
        private String sqlDatabaseFolder;

        @ConfigVariable(path = "Database.SQL.ForceUpdate",  isSerializableObject = false)
        @Getter
        private boolean sqlForceUpdate;

        @ConfigVariable(path = "Database.SQL.Type", isSerializableObject = false)
        @Getter
        private SQLType sqlDatabaseType;

        Memory(CustomConfig customConfig) {
            this.customConfig = customConfig;
        }

        @Override
        public CustomConfig getParent() {
            return customConfig;
        }
    }
}

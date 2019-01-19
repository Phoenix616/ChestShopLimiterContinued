package me.droreo002.cslimit.config;

import lombok.Getter;
import lombok.Setter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.database.DatabaseType;
import me.droreo002.cslimit.manager.Debug;
import me.droreo002.cslimit.objects.ChestShopSound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import sun.security.krb5.Config;

public final class ConfigMemory {

    // Only add @Setter for those variables that needed

    /*
    Misc
     */
    @Getter
    private String prefix;
    @Getter
    private boolean useBstats;
    @Getter
    private String langFile;
    @Getter
    private boolean refundOnRemove;
    @Getter
    private boolean refundOnUnlimited;

    /*
    Dependency
     */
    @Getter
    private boolean enableEssentialsHook;
    @Getter
    private boolean enablePapiHook;
    @Getter
    private boolean enableLuckPermsHook;
    @Getter
    private boolean enableCMIHook;

    /*
    CommandSound
     */
    @Getter
    private ChestShopSound successSound;
    @Getter
    private ChestShopSound failureSound;

    /*
    ShopLimit
     */
    @Getter
    private ConfigurationSection shopLimit;
    @Getter
    private boolean shopLimitForceDefault;

    /*
    ShopLimitLuckPerms
     */
    @Getter
    private ConfigurationSection shopLimitLuckPerms;

    /*
    Databases
     */
    @Getter
    private DatabaseType databaseType;
    @Getter
    private int saveEvery;
    @Getter
    private String clientHost;
    @Getter
    private int clientPort;
    @Getter
    private String databaseName;
    @Getter
    private String clientUsername;
    @Getter
    private String clientPassword;

    public ConfigMemory(FileConfiguration config) {
        this.prefix = config.getString("Prefix");
        this.useBstats = config.getBoolean("use-bstats");
        this.langFile = config.getString("Language");
        this.refundOnRemove = config.getBoolean("RefundOnShopRemove");
        this.refundOnUnlimited = config.getBoolean("RefundOnShopRemove-Unlimited");

        this.enableEssentialsHook = config.getBoolean("Dependency.Essentials");
        this.enablePapiHook = config.getBoolean("Dependency.PlaceholderAPI");
        this.enableLuckPermsHook = config.getBoolean("Dependency.LuckPerms");
        this.enableCMIHook = config.getBoolean("Dependency.CMI");

        this.successSound = new ChestShopSound(config.getConfigurationSection("CommandSound.success"));
        this.failureSound = new ChestShopSound(config.getConfigurationSection("CommandSound.failure"));

        this.shopLimit = config.getConfigurationSection("ShopLimit");
        this.shopLimitForceDefault = config.getBoolean("ShopLimit.force-default");

        this.shopLimitLuckPerms = config.getConfigurationSection("ShopLimitLuckPerms");

        try {
            this.databaseType = DatabaseType.valueOf(config.getString("Database.DatabaseType"));
        } catch (Exception e) {
            Debug.log("Cannot find that databse type!. Please check your config!", true);
            Bukkit.getPluginManager().disablePlugin(ChestShopLimiter.getInstance());
            return;
        }
        this.saveEvery = config.getInt("Database.SaveEvery");
        this.clientHost = config.getString("Database.ClientHost");
        this.clientPort = config.getInt("Database.ClientPort");
        this.databaseName = config.getString("Database.DatabaseName");
        this.clientUsername = config.getString("Database.ClientUsername");
        this.clientPassword = config.getString("Database.ClientPassword");
    }
}

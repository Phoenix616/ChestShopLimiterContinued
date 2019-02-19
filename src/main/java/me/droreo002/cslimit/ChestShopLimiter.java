package me.droreo002.cslimit;

import lombok.Getter;
import me.droreo002.cslimit.api.ChestShopAPI;
import me.droreo002.cslimit.commands.CommandListener;
import me.droreo002.cslimit.commands.CommandManager;
import me.droreo002.cslimit.commands.TabManager;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.CSLDatabase;
import me.droreo002.cslimit.hook.HookManager;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.listener.PlayerConnectionListener;
import me.droreo002.cslimit.manager.Debug;
import me.droreo002.cslimit.manager.LicenseManager;
import me.droreo002.cslimit.metrics.Metrics;
import me.droreo002.cslimit.objects.ChestShopLimiterHandler;
import me.droreo002.cslimit.utils.CommonUtils;
import me.droreo002.oreocore.OreoCore;
import me.droreo002.oreocore.database.utils.MySqlConnection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestShopLimiter extends JavaPlugin {

    @Getter
    private static ChestShopLimiter instance;
    @Getter
    private HookManager hookManager;
    @Getter
    private ConfigManager configManager;
    @Getter
    private LangManager langManager;
    @Getter
    private Metrics met;
    @Getter
    private ChestShopAPI chestShopAPI;
    @Getter
    private CSLDatabase database;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        configManager = new ConfigManager(this);
        langManager = new LangManager(this);
        chestShopAPI = new ChestShopLimiterHandler();
        database = new CSLDatabase(this, configManager.getMemory().getDatabaseType());
        OreoCore.getInstance().dependPlugin(this, true);

        Bukkit.getPluginCommand("chestshoplimiter").setExecutor(new CommandListener());
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
//        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
//        Bukkit.getPluginManager().registerEvents(new ShopCreateListener(this), this);
//        Bukkit.getPluginManager().registerEvents(new ShopDestroyListener(this), this);

        printInformation();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void printInformation() {
        Debug.log("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false);
        System.out.println(" ");
        // Dependency check (Hard depend)
        if (!CommonUtils.isPluginExists("ChestShop")) {
            Debug.log("&cChestShop plugin cannot be found!. This plugin will not working if there's no ChestShop plugin found!", false);
            System.out.println(" ");
            Debug.log("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!CommonUtils.isPluginExists("OreoCore")) {
            Debug.log("&cOreoCore plugin cannot be found!. This plugin cannot run without it!, plugin will now be disabled", false);
            System.out.println(" ");
            Debug.log("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Debug.log("&fSetting up the &econfig.yml", false);
        Debug.log("&fSetting up the &eLang &ffile", false);
        Debug.log("&fEnabling &eChestShopLimiter-API", false);
        Debug.log("&fHooking to some plugins...", false);
        hookManager = new HookManager();
        Debug.log("&fPlugin is currently using &b" + configManager.getMemory().getDatabaseType() + "&f database type!", false);
        Debug.log("&bDatabase &fhas been successfully initialized!", false);
        CommandManager.init();
        Bukkit.getPluginCommand("chestshoplimiter").setTabCompleter(new TabManager());
        if (configManager.getMemory().isUseBstats()) {
            Debug.log("&fConnecting to &bBSTATS", false);
            if (configManager.getConfig().getBoolean("use-bstats")) {
                Debug.log("&fPlugin has been connected to &bBSTATS &fserver", false);
                met = new Metrics(this);
                met.addCustomChart(new Metrics.SimplePie("plugintype", () -> "Premium"));
            } else {
                Debug.log("&bBSTATS&f seems to be disabled. Now disconnecting from the server...",false);
                met = null;
            }
        }
        Debug.log("&fSuccess!. We're now running on version " + Bukkit.getBukkitVersion() + " &7(&bServer&7) &fand " + getDescription().getVersion() + " &7(&bPlugin&7)", false);
        Debug.log(LicenseManager.getBuyerInformation(), false);

        System.out.println(" ");
        Debug.log("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false);
    }
}

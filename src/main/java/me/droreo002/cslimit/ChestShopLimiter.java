package me.droreo002.cslimit;

import lombok.Getter;
import me.droreo002.cslimit.api.ChestShopAPI;
import me.droreo002.cslimit.commands.ChestShopLimiterCommand;
import me.droreo002.cslimit.config.CSLConfig;
import me.droreo002.cslimit.config.InventoryTemplates;
import me.droreo002.cslimit.conversation.ConversationManager;
import me.droreo002.cslimit.database.CSLDatabase;
import me.droreo002.cslimit.hook.HookManager;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.listener.PlayerConnectionListener;
import me.droreo002.cslimit.listener.support.ShopListenerUniversal;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.cslimit.manager.LicenseManager;
import me.droreo002.cslimit.manager.logger.LogFile;
import me.droreo002.cslimit.metrics.Metrics;
import me.droreo002.cslimit.api.ChestShopLimiterHandler;
import me.droreo002.cslimit.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestShopLimiter extends JavaPlugin {

    @Getter
    private static ChestShopLimiter instance;
    @Getter
    private HookManager hookManager;
    @Getter
    private CSLConfig cslConfig;
    @Getter
    private LangManager langManager;
    @Getter
    private Metrics met;
    @Getter
    private ChestShopAPI chestShopAPI;
    @Getter
    private CSLDatabase database;
    @Getter
    private LogFile logFile;
    @Getter
    private ConversationManager conversationManager;
    @Getter
    private ChestShopLimiterCommand command;
    @Getter
    private InventoryTemplates inventoryTemplates;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        cslConfig = new CSLConfig(this);
        logFile = new LogFile();
        inventoryTemplates = new InventoryTemplates(this);
        Debug.info("&fStarting the plugin...", true, Debug.LogType.BOTH);

        if (!getServer().getPluginManager().isPluginEnabled("ChestShop")) {
            Debug.error("ChestShop is not enabled!. Disabling plugin...", true, Debug.LogType.BOTH);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        langManager = new LangManager(this);
        database = new CSLDatabase(this, cslConfig.getDatabaseType());
        chestShopAPI = new ChestShopLimiterHandler();
        conversationManager = new ConversationManager(this);
        command = new ChestShopLimiterCommand(this, this);

        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ShopListenerUniversal(this), this);

        printInformation();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Debug.info("&fDisabling &b" + getName() + " &ev" + getDescription().getVersion() + "&f!", true, Debug.LogType.BOTH);
    }

    private void printInformation() {
        Debug.info("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false, Debug.LogType.BOTH);
        System.out.println(" ");
        // Dependency check (Hard depend)
        if (!CommonUtils.isPluginExists("ChestShop")) {
            Debug.info("&cChestShop plugin cannot be found!. This plugin will not working if there's no ChestShop plugin found!", false, Debug.LogType.BOTH);
            System.out.println(" ");
            Debug.info("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false, Debug.LogType.BOTH);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!CommonUtils.isPluginExists("OreoCore")) {
            Debug.info("&cOreoCore plugin cannot be found!. This plugin cannot run without it!, plugin will now be disabled", false, Debug.LogType.BOTH);
            System.out.println(" ");
            Debug.info("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false, Debug.LogType.BOTH);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Debug.info("&fSetting up &econfig.yml", false, Debug.LogType.BOTH);
        Debug.info("&fSetting up &e" + langManager.getFileName(), false, Debug.LogType.BOTH);
        Debug.info("&fEnabling &eChestShopLimiter-API", false, Debug.LogType.BOTH);
        Debug.info("&fHooking to some plugins...", false, Debug.LogType.BOTH);
        hookManager = new HookManager();
        Debug.info("&fPlugin is currently using &b" + cslConfig.getDatabaseType() + "&f database type!", false, Debug.LogType.BOTH);
        Debug.info("&bDatabase &fhas been successfully initialized!", false, Debug.LogType.BOTH);
        Debug.info("&fRegistering &eCommands...", false, Debug.LogType.BOTH);
        command.getArgs().forEach(arg ->  Debug.info("     &fSub-Command with the id of &e" + arg.getTrigger() + " &fhas been registered!", false, Debug.LogType.BOTH));
        Debug.info("&fRegisteting &eTabCompleters...", false, Debug.LogType.BOTH);
        command.getTabComplete().forEach(s -> Debug.info("     &fTab completer with the id of &e" + s + " &fhas been registered!", false, Debug.LogType.BOTH));
        if (cslConfig.isUseBstats()) { // disable for now
            Debug.info("&fConnecting to &bBSTATS", false, Debug.LogType.BOTH);
            if (cslConfig.getConfig().getBoolean("use-bstats")) {
                Debug.info("&fPlugin has been connected to &bBSTATS &fserver", false, Debug.LogType.BOTH);
                met = new Metrics(this);
                met.addCustomChart(new Metrics.SimplePie("plugintype", () -> "Continued"));
            } else {
                Debug.info("&bBSTATS&f seems to be disabled. Now disconnecting from the server...",false, Debug.LogType.BOTH);
                met = null;
            }
        }
        Debug.info("&fSuccess!. We're now running on version " + Bukkit.getBukkitVersion() + " &7(&bServer&7) &fand " + getDescription().getVersion() + " &7(&bPlugin&7)", false, Debug.LogType.BOTH);

        System.out.println(" ");
        Debug.info("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false, Debug.LogType.BOTH);
    }
}

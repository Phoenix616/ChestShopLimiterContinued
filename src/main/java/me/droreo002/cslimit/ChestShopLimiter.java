package me.droreo002.cslimit;

import lombok.Getter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.hook.HookManager;
import me.droreo002.cslimit.lang.LangManager;
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

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        hookManager = new HookManager();
        configManager = new ConfigManager(this);
        langManager = new LangManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

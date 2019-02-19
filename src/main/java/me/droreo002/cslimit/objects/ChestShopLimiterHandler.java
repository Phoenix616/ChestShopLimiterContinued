package me.droreo002.cslimit.objects;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.api.ChestShopAPI;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.CSLDatabase;
import me.droreo002.cslimit.lang.LangManager;

import java.util.UUID;

public final class ChestShopLimiterHandler implements ChestShopAPI {

    private final ChestShopLimiter plugin = ChestShopLimiter.getInstance();

    @Override
    public int getShopCreated(UUID uuid) {
        return 0;
    }

    @Override
    public int getShopLimit(UUID uuid) {
        return 0;
    }

    @Override
    public ConfigManager.Memory getConfigMemory() {
        return plugin.getConfigManager().getMemory();
    }

    @Override
    public LangManager getLangManager() {
        return plugin.getLangManager();
    }

    @Override
    public CSLDatabase getDatabase() {
        return plugin.getDatabase();
    }
}

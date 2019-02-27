package me.droreo002.cslimit.objects;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.api.ChestShopAPI;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.CSLDatabase;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.oreocore.database.DatabaseType;

import java.util.UUID;

public final class ChestShopLimiterHandler implements ChestShopAPI {

    private final ChestShopLimiter plugin = ChestShopLimiter.getInstance();

    @Override
    public int getShopCreated(UUID uuid) {
        final PlayerData data = getDatabase().getWrapper().getPlayerData(uuid);
        if (data == null) return 0;
        return data.getShopCreated();
    }

    @Override
    public int getShopLimit(UUID uuid) {
        final PlayerData data = getDatabase().getWrapper().getPlayerData(uuid);
        if (data == null) return 0;
        return data.getMaxShop();
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

    @Override
    public PlayerData getData(UUID uuid) {
        return getDatabase().getWrapper().getPlayerData(uuid);
    }

    @Override
    public DatabaseType getDatabaseType() {
        return getDatabase().getWrapper().getType();
    }
}

package me.droreo002.cslimit.api;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.CSLConfig;
import me.droreo002.cslimit.database.CSLDatabase;
import me.droreo002.cslimit.database.object.PlayerData;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.oreocore.database.DatabaseType;
import org.apache.commons.lang.Validate;

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
    public void addShopCreated(UUID uuid, int amount) {
        final PlayerData data = getData(uuid);
        Validate.notNull(data, "Data for UUID " + uuid + " cannot be found!");
        data.setShopCreated(data.getShopCreated() + amount);
        saveData(data);
    }

    @Override
    public void addShopLimit(UUID uuid, int amount) {
        final PlayerData data = getData(uuid);
        Validate.notNull(data, "Data for UUID " + uuid + " cannot be found!");
        data.setMaxShop(data.getMaxShop() + amount);
        saveData(data);
    }

    @Override
    public void setShopCreated(UUID uuid, int value) {
        final PlayerData data = getData(uuid);
        Validate.notNull(data, "Data for UUID " + uuid + " cannot be found!");
        data.setShopCreated(value);
        saveData(data);
    }

    @Override
    public void setShopLimit(UUID uuid, int value) {
        final PlayerData data = getData(uuid);
        Validate.notNull(data, "Data for UUID " + uuid + " cannot be found!");
        data.setMaxShop(value);
        saveData(data);
    }

    @Override
    public CSLConfig getConfig() {
        return plugin.getCslConfig();
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

    @Override
    public void saveData(PlayerData data) {
        getDatabase().getWrapper().savePlayerData(data);
    }
}

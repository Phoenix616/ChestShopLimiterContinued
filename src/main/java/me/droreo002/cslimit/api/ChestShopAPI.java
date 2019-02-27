package me.droreo002.cslimit.api;

import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.CSLDatabase;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.oreocore.database.DatabaseType;

import java.util.UUID;

public interface ChestShopAPI {

    /**
     * Get the shop created amount for that player uuid
     *
     * @param uuid : Player uuid
     * @return The shop created amount
     */
    int getShopCreated(UUID uuid);

    /**
     * Get the shop limit for that player uuid
     *
     * @param uuid : Player uuid
     * @return The shop limit amount
     */
    int getShopLimit(UUID uuid);

    /**
     * Get the plugin's config memory. This will give access
     * to config datas
     *
     * @return The config memory
     */
    ConfigManager.Memory getConfigMemory();

    /**
     * Get the plugin's lang manager. This will give access
     * to send any lang message.
     *
     * @return The lang manager
     */
    LangManager getLangManager();

    /**
     * Get the plugin's database
     *
     * @return  The database
     */
    CSLDatabase getDatabase();

    /**
     * Get the player data
     *
     * @return the PlayerData if there's any, null otherwise
     */
    PlayerData getData(UUID uuid);

    /**
     * Get the current database type
     *
     * @return The database type
     */
    DatabaseType getDatabaseType();
}

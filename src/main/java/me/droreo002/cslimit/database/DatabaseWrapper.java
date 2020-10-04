package me.droreo002.cslimit.database;

import me.droreo002.cslimit.database.object.PlayerData;
import me.droreo002.oreocore.database.DatabaseType;

import java.util.UUID;

public interface DatabaseWrapper {

    /**
     * Get the player data
     *
     * @param uuid : The player's UUID
     * @return the player data if exists, null otherwise
     */
    PlayerData getPlayerData(UUID uuid);

    /**
     * Save the player data
     *
     * @param playerData : Player data
     */
    void savePlayerData(PlayerData playerData);

    /**
     * Update the player data
     *
     * @param playerData : Player data to update
     */
    void updatePlayerData(PlayerData playerData);

    /**
     * Remove the player data from the cache or maybe. Remove and delete it permanently
     *
     * @param uuid : Player UUID
     * @param delete : Should we delete it?
     */
    void removePlayerData(UUID uuid, boolean delete);

    /**
     * Get the database Type
     *
     * @return The database type
     */
    DatabaseType getType();

    /**
     * Just try to load the data
     *
     * @param uuid : The UUID
     */
    void load(UUID uuid) throws Exception;

    /**
     * Migrate the PlayerData object that is from old / different database type
     * into the current database
     *
     * @param playerData : The player data that will get migrated
     */
    void migrate(PlayerData playerData);
}

package me.droreo002.cslimit.database;

import me.droreo002.oreocore.database.DatabaseType;
import org.bukkit.entity.Player;

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
     * Update the player data
     *
     * @param playerData : Player data
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
    void load(UUID uuid);
}

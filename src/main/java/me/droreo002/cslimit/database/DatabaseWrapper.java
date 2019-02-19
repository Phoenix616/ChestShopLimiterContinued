package me.droreo002.cslimit.database;

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

    void updatePlayerData(PlayerData playerData);

    void removePlayerData(UUID uuid, boolean delete);
}

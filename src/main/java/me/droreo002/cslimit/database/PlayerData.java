package me.droreo002.cslimit.database;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private static final Map<UUID, PlayerData> DATA = new HashMap<>();

    private DataObject dataObject;

    private PlayerData(UUID uuid) {

    }

    /**
     * Setup the data for that UUID. Will create it there's no key with that uuid
     *
     * @param uuid : The player's uuid
     */
    public static void setupData(UUID uuid) {
        // TODO : Check if its exist
    }

    /**
     * Get the player data for that UUID
     *
     * @param uuid : The player's uuid
     * @return PlayerData if there's any. Null otherwise
     */
    public static PlayerData getData(UUID uuid) {
        return DATA.get(uuid);
    }

    public void update() {
        // TODO : Execute update statement
    }

    private class DataObject {

        private String playerName;
        private int maxShopCount;
        private int shopCreated;

        public DataObject(String playerName, int maxShopCount, int shopCreated) {
            this.playerName = playerName;
            this.maxShopCount = maxShopCount;
            this.shopCreated = shopCreated;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public void setMaxShopCount(int maxShopCount) {
            this.maxShopCount = maxShopCount;
        }

        public void setShopCreated(int shopCreated) {
            this.shopCreated = shopCreated;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getMaxShopCount() {
            return maxShopCount;
        }

        public int getShopCreated() {
            return shopCreated;
        }
    }
}

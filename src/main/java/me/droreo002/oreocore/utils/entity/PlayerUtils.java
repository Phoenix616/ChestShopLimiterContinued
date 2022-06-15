package me.droreo002.oreocore.utils.entity;

import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainTasks;
import com.Acrobot.ChestShop.Database.Account;
import com.Acrobot.ChestShop.UUIDs.NameManager;
import com.google.common.base.Charsets;
import me.droreo002.oreocore.utils.item.ItemStackBuilder;
import me.droreo002.oreocore.utils.misc.SoundObject;
import me.droreo002.oreocore.utils.misc.ThreadingUtils;
import me.droreo002.oreocore.utils.strings.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

@SuppressWarnings("deprecation")
public final class PlayerUtils {

    public static final UUID INVALID_USER_UUID = UUID.nameUUIDFromBytes("InvalidUsername".getBytes(Charsets.UTF_8));

    /**
     * Generate a new invalid OfflinePlayer by skipping
     * the original Bukkit's lookup. The UUID will be generated using
     * {@link PlayerUtils#INVALID_USER_UUID}
     *
     * @param name The user name
     * @return Invalid OfflinePlayer with name specified
     */
    public static OfflinePlayer getOfflinePlayerSkipLookup(String name) {
        Class<?> gameProfileClass;
        Constructor<?> gameProfileConstructor;
        Constructor<?> craftOfflinePlayerConstructor;
        try {
            try { // 1.7
                gameProfileClass = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
            } catch (ClassNotFoundException e) { // 1.8
                gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
                gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            }
            gameProfileConstructor = gameProfileClass.getDeclaredConstructor(UUID.class, String.class);
            gameProfileConstructor.setAccessible(true);
            Class<?> serverClass = Bukkit.getServer().getClass();
            Class<?> craftOfflinePlayerClass = Class.forName(serverClass.getName()
                    .replace("CraftServer", "CraftOfflinePlayer"));
            craftOfflinePlayerConstructor = craftOfflinePlayerClass.getDeclaredConstructor(
                    serverClass, gameProfileClass
            );
            craftOfflinePlayerConstructor.setAccessible(true);
            Object gameProfile = gameProfileConstructor.newInstance(INVALID_USER_UUID, name);
            Object craftOfflinePlayer = craftOfflinePlayerConstructor.newInstance(Bukkit.getServer(), gameProfile);
            return (OfflinePlayer) craftOfflinePlayer;
        } catch (Throwable t) { // Fallback if fail
            return Bukkit.getOfflinePlayer(name);
        }
    }

    /**
     * Get block's location that the player's looking at
     *
     * @param player The player to check
     * @param distance The distance
     * @return the Location of block the player's looking at
     */
    public static Location getPlayerLooking(Player player, int distance) {
        List<Block> sightBlock = player.getLineOfSight(null, distance);
        List<Location> sight = new ArrayList<Location>();
        for (Block block : sightBlock) {
            sight.add(block.getLocation());
        }
        // Get the last
        return sight.get(sight.size() - 1);
    }

    /**
     * Send a message to player
     *
     * @param message The message to send
     * @param target The target
     * @param clearChat Should we clear chat?
     * @param soundObject The sound to play, nullable
     */
    public static void sendMessage(Player target, String message, boolean clearChat, SoundObject soundObject) {
        if (clearChat) PlayerUtils.clearChat(target);
        if (soundObject != null) soundObject.send(target);
        sendMessage(target, message);
    }

    /**
     * Get the player ping (Via reflection)
     *
     * @param player The target player
     * @return the player ping
     */
    public static int getPing(Player player) {
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Get player name by UUID
     *
     * @param uuid The UUID
     * @return the player name if succeeded, empty string otherwise
     */
    public static String getPlayerName(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);
            if (!off.hasPlayedBefore()) {
                // Player was never online, try getting it from ChestShop
                Account account = NameManager.getAccount(uuid);
                if (account != null)
                    return account.getName();
                return "";
            }
            return off.getName();
        } else {
            return player.getName();
        }
    }

    /**
     * Get player's uuid by name (Not recommended for server that had 1k+ offline player data tho)
     *
     * @param playerName Player name
     * @return Player's uuid
     */
    @SuppressWarnings("deprecation")
    public static Future<UUID> getPlayerUuid(String playerName) {
        return ThreadingUtils.makeFuture(() -> {
            UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
            if (!Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
                // Player was never online, try getting it from ChestShop
                Account account = NameManager.getAccount(playerName);
                if (account == null)
                    return null;
                uuid = account.getUuid();
            }
            return uuid;
        });
    }

    /**
     * Clear player's chat
     *
     * @param player Target player
     */
    public static void clearChat(Player player) {
        for (int i = 0; i < 40; i++) player.sendMessage(" ");
    }

    /**
     * Check if player is vanished. Will prob only work on SuperVanish
     *
     * @param player : The target player
     * @return true if vanished, false otherwise
     */
    public static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    /**
     * Get a OfflinePlayer, this will always try it via async way
     *
     * @param name : The player name
     */
    @SuppressWarnings("deprecation")
    public static void getOfflinePlayer(String name, TaskChainTasks.LastTask<OfflinePlayer> callback) {
        TaskChain<OfflinePlayer> chain = ThreadingUtils.makeChain();
        chain.asyncFirst(() -> Bukkit.getOfflinePlayer(name)).asyncLast(callback).execute();
    }

    /**
     * Check if player has the expected amount of the target item
     *
     * @param expected The expected amount
     * @param playerInventory Player's inventory
     * @param item The item to check
     * @return true if amount is greater than expected, false otherwise
     */
    public static boolean has(int expected, Inventory playerInventory, ItemStack item) {
        int amount = 0;
        final List<ItemStack> items = new ArrayList<>(Arrays.asList(playerInventory.getContents()));
        for (ItemStack i : items) {
            if (ItemStackBuilder.isSimilar(i, item)) {
                amount += i.getAmount();
            }
        }

        return amount > expected;
    }

    /**
     * Send message to the player
     *
     * @param player The target player
     * @param msg The message to send (color code supported)
     */
    public static void sendMessage(Player player, String msg) {
        player.sendMessage(StringUtils.color(msg));
    }


}

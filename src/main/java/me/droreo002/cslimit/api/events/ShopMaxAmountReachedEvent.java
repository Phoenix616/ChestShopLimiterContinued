package me.droreo002.cslimit.api.events;

import lombok.Getter;
import me.droreo002.cslimit.database.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ShopMaxAmountReachedEvent extends CSLEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private Player player;
    @Getter
    private PlayerData playerData;

    private boolean cancelled;

    public ShopMaxAmountReachedEvent(Player player, PlayerData playerData) {
        super(player, playerData);
        this.cancelled = false;
        this.player = player;
        this.playerData = playerData;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}

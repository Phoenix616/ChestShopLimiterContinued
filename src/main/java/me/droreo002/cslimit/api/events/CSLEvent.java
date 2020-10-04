package me.droreo002.cslimit.api.events;

import lombok.Getter;
import me.droreo002.cslimit.database.object.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class CSLEvent extends Event {

    @Getter
    private Player player;
    @Getter
    private PlayerData playerData;

    public CSLEvent(Player player, PlayerData playerData) {
        this.player = player;
        this.playerData = playerData;
    }
}

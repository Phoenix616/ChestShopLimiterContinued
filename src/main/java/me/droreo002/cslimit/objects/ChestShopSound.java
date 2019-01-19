package me.droreo002.cslimit.objects;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.Debug;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ChestShopSound {

    @Getter
    private float volume;
    @Getter
    private float pitch;
    @Getter
    private Sound sound;

    public ChestShopSound(ConfigurationSection cs) {
        Validate.notNull(cs, "Configuration section cannot be null!");
        if (!cs.contains("volume") && !cs.contains("pitch") && !cs.contains("sound")) {
            throw new IllegalStateException("Invalid configuration section while serializing ChestShopSound!");
        }
        this.volume = (float) cs.getDouble("volume");
        this.pitch = (float) cs.getDouble("pitch");
        try {
            this.sound = Sound.valueOf(cs.getString("sound"));
        } catch (Exception e) {
            Debug.log("Cannot find sound with the name of " + cs.getString("sound") + ". Plugin will now be disabled!", true);
            Bukkit.getPluginManager().disablePlugin(ChestShopLimiter.getInstance());
        }
    }

    public void play(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}

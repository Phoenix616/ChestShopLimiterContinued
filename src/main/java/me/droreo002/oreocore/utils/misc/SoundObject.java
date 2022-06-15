package me.droreo002.oreocore.utils.misc;

import lombok.Getter;
import me.droreo002.oreocore.OreoCore;
import me.droreo002.oreocore.configuration.SerializableConfigVariable;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SoundObject implements SerializableConfigVariable {

    // Pre - Initialized
    public static SoundObject SUCCESS_SOUND = new SoundObject(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    public static SoundObject ERROR_SOUND = new SoundObject(Sound.BLOCK_ANVIL_FALL);

    @Getter
    private float volume;
    @Getter
    private float pitch;
    @Getter
    private Sound sound;

    /**
     * Allow null for @ConfigVariable support
     */
    public SoundObject() { }

    public SoundObject(Sound sound, float volume, float pitch) {
        this.volume = volume;
        this.pitch = pitch;
        this.sound = sound;
    }

    public SoundObject(Sound sound) {
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.sound = sound;
    }

    /**
     * Send the sound to the player
     *
     * @param player : The target player
     */
    public void send(Player player) {
        if (this.sound == null) throw new NullPointerException("Sound is invalid!");
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Send the sound to the player
     *
     * @param player : The target player
     * @param delay : The delay in second
     */
    public void send(Player player, int delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(OreoCore.getInstance(), () -> send(player), 20L * delay);
    }

    public static SoundObject deserialize(ConfigurationSection section) {
        float volume = (float) section.getDouble("volume", 1.0f);
        float pitch = (float) section.getDouble("pitch", 1.0f);
        Sound sound = Sound.valueOf(Objects.requireNonNull(section.getString("sound")).toUpperCase(Locale.ROOT));
        return new SoundObject(sound, volume, pitch);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("sound", sound.toString());
        if (volume != 1.0f) map.put("volume", volume);
        if (pitch != 1.0f) map.put("pitch", pitch);
        return map;
    }
}

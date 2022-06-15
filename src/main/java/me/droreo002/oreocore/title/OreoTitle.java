package me.droreo002.oreocore.title;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.droreo002.oreocore.configuration.SerializableConfigVariable;
import me.droreo002.oreocore.utils.misc.SoundObject;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static me.droreo002.oreocore.utils.strings.StringUtils.color;

public class OreoTitle implements SerializableConfigVariable, Cloneable {

    @Getter @Setter
    private String title;
    @Getter @Setter
    private String subTitle;
    @Getter @Setter
    private int fadeIn;
    @Getter @Setter
    private int stay;
    @Getter @Setter
    private int fadeOut;
    @Getter @Setter
    private SoundObject soundOnSend;

    public OreoTitle(String title) {
        this.title = title;
        this.subTitle = " ";
        this.fadeIn = 0;
        this.stay = 20;
        this.fadeOut = 0;
        this.soundOnSend = null;
    }

    public OreoTitle(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = 0;
        this.stay = 20;
        this.fadeOut = 0;
        this.soundOnSend = null;
    }

    public OreoTitle(String title, String subTitle, SoundObject soundOnSend) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = 0;
        this.stay = 20;
        this.fadeOut = 0;
        this.soundOnSend = soundOnSend;
    }

    public OreoTitle() {
        this.title = "Dummy";
        this.subTitle = "Dummy";
        this.fadeIn = 1;
        this.stay = 1;
        this.fadeOut = 1;
        this.soundOnSend = null;
    }

    public OreoTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut, SoundObject soundOnSend) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        this.soundOnSend = soundOnSend;
    }

    /**
     * Send this title to player
     *
     * @param player The target player
     */
    public void send(Player player) {
        if (soundOnSend != null) soundOnSend.send(player);
        player.sendTitle(color(title), color(subTitle), fadeIn, stay, fadeOut);
    }

    public static OreoTitle deserialize(ConfigurationSection section) {
        String title = section.getString("title");
        String subTitle = section.getString("sub-title");
        int fadeIn = section.getInt("fade-in");
        int stay = section.getInt("stay");
        int fadeOut = section.getInt("fade-out");
        SoundObject soundOnSend = null;
        if (section.isSet("soundOnSend")) {
            final Sound s = Sound.valueOf(Objects.requireNonNull(section.getString("soundOnSend")).toUpperCase(Locale.ROOT));
            if (s != null) soundOnSend = new SoundObject(s);
        }
        return new OreoTitle(title, subTitle, fadeIn, stay, fadeOut, soundOnSend);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("sub-title", subTitle);
        map.put("fade-in", fadeIn);
        map.put("stay", stay);
        map.put("fade-out", fadeOut);
        if (soundOnSend != null) map.put("soundOnSend", soundOnSend.getSound().toString());
        return map;
    }

    /**
     * Clone this OreoTitle
     *
     * @return The GUIButton
     */
    @Override
    public OreoTitle clone() {
        try {
            return (OreoTitle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}

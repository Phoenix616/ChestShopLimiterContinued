package me.droreo002.cslimit.lang;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.configuration.CustomConfig;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import me.droreo002.oreocore.utils.strings.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class LangManager extends CustomConfig {

    private static final String LATEST_VERSION = "1.0";

    // Object could be 2 things. List and String
    private final Map<LangPath, Object> values = new HashMap<>();
    private final ChestShopLimiter plugin;

    public LangManager(ChestShopLimiter plugin) {
        super(plugin, new File(plugin.getDataFolder(), plugin.getConfigManager().getMemory().getLangFile()));
        this.plugin = plugin;
        if (tryUpdate("ConfigVersion", LATEST_VERSION)) {
            Debug.info("&7" + getYamlFile().getName() + " &fhas been updated!", true, Debug.LogType.BOTH);
        }
        loadData();
    }

    public void loadData() {
        if (!values.isEmpty()) values.clear();
        for (LangPath path : LangPath.values()) {
            String exactPath = path.getPath();
            if (getConfig().contains(exactPath)) values.put(path, getConfig().get(exactPath));
        }
    }

    /**
     * Get the lang string
     *
     * @param path : The lang path
     * @param placeholder : The placeholder (null if there's none)
     * @param addPrefix : Should we add prefix to it?
     * @return the String if succeeded, null otherwise
     */
    public String getLang(LangPath path, TextPlaceholder placeholder, boolean addPrefix) {
        if (values.get(path) == null) return "Error. Lang " + path.getPath() + " cannot be found!";
        ConfigManager.Memory mem = plugin.getConfigManager().getMemory();
        if (placeholder != null) {
            // Process placeholder
            String result = (String) values.get(path);
            for (TextPlaceholder place : placeholder.getPlaceholders()) {
                String from = place.getFrom();
                String to = place.getTo();
                result = result.replace(from, to);
            }
            return (addPrefix) ? StringUtils.color(mem.getPrefix() + result) : StringUtils.color(result);
        }
        return (addPrefix) ? StringUtils.color(mem.getPrefix() + values.get(path)) : StringUtils.color((String) values.get(path));
    }

    /**
     * Get the lang as a list
     *
     * @param path : The path to the list
     * @param placeholder : The placeholder, null if there's none
     * @return List containing the string if there's any, empty string otherwise
     */
    public List<String> getLangList(LangPath path, TextPlaceholder placeholder) {
        if (values.get(path) == null) return new ArrayList<>();
        if (placeholder != null) {
            List<String> result = new ArrayList<>();
            for (String s : (List<String>) values.get(path)) {
                for (TextPlaceholder place : placeholder.getPlaceholders()) {
                    String from = place.getFrom();
                    String to = place.getTo();
                    if (s.contains(from)) {
                        s = s.replace(from, to);
                    }
                }
                result.add(s);
            }
            return result.stream().map(StringUtils::color).collect(Collectors.toList());
        }
        return ((List<String>) values.get(path)).stream().map(StringUtils::color).collect(Collectors.toList());
    }

    /**
     * Get the path as a ConfigurationSection object
     *
     * @param path : The path enum
     * @return a ConfigurationSection if there's any, null otherwise
     */
    public ConfigurationSection asSection(LangPath path) {
        return getConfig().getConfigurationSection(path.getPath());
    }
}

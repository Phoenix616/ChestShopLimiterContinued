package me.droreo002.cslimit.lang;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.manager.LicenseManager;
import me.droreo002.cslimit.utils.StringUtils;
import me.droreo002.oreocore.configuration.CustomConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class LangManager extends CustomConfig {

    // Object could be 2 things. List and String
    private final Map<LangPath, Object> values = new HashMap<>();
    private final ChestShopLimiter plugin;

    public LangManager(ChestShopLimiter plugin) {
        super(plugin, new File(plugin.getDataFolder(), "en_lang.yml"));
        this.plugin = plugin;
        loadData();
    }

    private void loadData() {
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
    public String getLang(LangPath path, Map<String, String> placeholder, boolean addPrefix) {
        if (values.get(path) == null) return null;
        ConfigManager.Memory mem = plugin.getConfigManager().getMemory();
        if (placeholder != null) {
            // Process placeholder
            String result = (String) values.get(path);
            for (Map.Entry ent : placeholder.entrySet()) {
                String from = (String) ent.getKey();
                String to = (String) ent.getValue();
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
    public List<String> getLangList(LangPath path, Map<String, String> placeholder) {
        if (values.get(path) == null) return new ArrayList<>();
        if (placeholder != null) {
            List<String> result = new ArrayList<>();
            for (String s : (List<String>) values.get(path)) {
                for (Map.Entry ent : placeholder.entrySet()) {
                    String from = (String) ent.getKey();
                    String to = (String) ent.getValue();
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

    public List<String> getAbout() {
        List<String> result = new ArrayList<>();
        result.add(StringUtils.color("&8&m--------------- &7[ &aChestShopLimiter &7] &8&m---------------"));
        result.add(" ");
        result.add(StringUtils.color("&fThis server is running on &bChestShopLimiter &fplugin version &c" + getPlugin().getDescription().getVersion()));
        result.add(LicenseManager.getBuyerInformation());
        result.add(" ");
        result.add(StringUtils.color("&8&m--------------- &7[ &aChestShopLimiter &7] &8&m---------------"));
        return result;
    }
}

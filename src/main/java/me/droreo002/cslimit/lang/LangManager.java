package me.droreo002.cslimit.lang;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.objects.abstracts.AbstractCustomConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class LangManager extends AbstractCustomConfig {

    private final Map<LangPath, Object> values = new HashMap<>();

    public LangManager(ChestShopLimiter plugin) {
        super(plugin);
    }

    public void loadData() {
        if (!values.isEmpty()) values.clear();
        for (LangPath path : LangPath.values()) {
            String exactPath = path.toString().toLowerCase().replace("_", ".");
        }
    }

    public static String getLang(LangPath path, Map<String, String> placeholder) {
        if (placeholder != null) {
            // Process placeholder
        }
        return "TODO : Continue this";
    }

    @Override
    public File getConfigFile() {
        return new File(getPlugin().getDataFolder(), getFileName());
    }

    @Override
    public String getFileName() {
        return getPlugin().getConfigManager().getMemory().getLangFile();
    }

    @Override
    public String getFilePath() {
        return "configuration" + File.separator + getFileName();
    }
}

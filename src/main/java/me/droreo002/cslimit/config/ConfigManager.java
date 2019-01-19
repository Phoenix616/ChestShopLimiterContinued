package me.droreo002.cslimit.config;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.objects.abstracts.AbstractCustomConfig;

import java.io.File;

public final class ConfigManager extends AbstractCustomConfig {

    @Getter
    private final ConfigMemory memory;

    public ConfigManager(ChestShopLimiter plugin) {
        super(plugin);
        this.memory = new ConfigMemory(getConfig());
    }

    @Override
    public File getConfigFile() {
        return new File(getPlugin().getDataFolder(), getFileName());
    }

    @Override
    public String getFileName() {
        return "config.yml";
    }

    @Override
    public String getFilePath() {
        return "configuration" + File.separator + getFileName();
    }
}

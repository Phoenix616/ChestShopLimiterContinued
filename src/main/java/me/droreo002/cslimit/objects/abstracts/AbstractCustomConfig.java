package me.droreo002.cslimit.objects.abstracts;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.Debug;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class AbstractCustomConfig {

    @Getter
    private ChestShopLimiter plugin;
    @Getter
    private FileConfiguration config;
    @Getter
    private File configFile;

    protected AbstractCustomConfig(ChestShopLimiter plugin) {
        this.plugin = plugin;
        setupConfig();
    }

    private void setupConfig() {
        this.configFile = getConfigFile();
        if (!configFile.exists()) {
            try {
                boolean success = configFile.createNewFile();
                if (success) {
                    plugin.saveResource(getFileName(), true);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Debug.log("Failed to create custom config file!", true);
                return;
            }
        }
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            Debug.log("Failed to save custom config file!", true);
        }
    }

    private void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        InputStream configData = plugin.getResource(getFileName());
        if (configData != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(configData)));
        }
    }

    public abstract File getConfigFile();
    public abstract String getFileName();
    public abstract String getFilePath();
}

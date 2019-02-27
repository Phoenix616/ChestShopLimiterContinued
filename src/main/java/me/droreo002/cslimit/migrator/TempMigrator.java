package me.droreo002.cslimit.migrator;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.logger.Debug;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO : Remove this when there's at least 5 purchases
 */
public class TempMigrator {

    /**
     * Get the old data.
     *
     * @return The list, will throw error if empty
     */
    public static List<FileConfiguration> getOldDatas() {
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        Debug.info("&fGetting old &bChestShopLimiter &fdata!. This might take a while!", true, true);
        final File dataFile = new File(plugin.getDataFolder(), "oldData");
        if (!dataFile.exists()) {
            throw new NullPointerException("[Migration] Could not find old data file!. Please place it inside the ChestShopLimiter data file and rename it to oldData!");
        }
        File[] files = dataFile.listFiles();
        if (files == null) throw new NullPointerException("[Migration] There's no data inside the OldData!");
        List<FileConfiguration> configs = new ArrayList<>();
        for (File f : files) {
            configs.add(YamlConfiguration.loadConfiguration(f));
        }
        if (configs.isEmpty()) throw new NullPointerException("[Migration] There's no data inside the OldData!");
        return configs;
    }
}

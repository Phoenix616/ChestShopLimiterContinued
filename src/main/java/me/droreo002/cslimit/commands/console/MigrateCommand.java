package me.droreo002.cslimit.commands.console;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.database.DatabaseWrapper;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import me.droreo002.oreocore.utils.bridge.ServerUtils;
import me.droreo002.oreocore.utils.io.FileUtils;
import me.droreo002.oreocore.utils.strings.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MigrateCommand extends CommandArg {

    private final ChestShopLimiter plugin;
    private final String prefix;

    public MigrateCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("migrate", parent);
        this.plugin = plugin;
        this.prefix = StringUtils.color(plugin.getConfigManager().getMemory().getPrefix());
        setConsoleOnly(true, plugin.getLangManager().getLang(LangPath.NORMAL_CONSOLE_ONLY, null, true));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Debug.info("Command migrate has been executed by " + commandSender.getName(), false, Debug.LogType.FILE);
        if (args.length == 1) {
            sendMessage(commandSender, prefix + "&bChestShopLimiter &fdata &eMigrator &fv1.0, please be aware that this command will get removed on the future version. Type &e/csl migrate start &fto continue, for more information please visit our &cWiki");
            return;
        }
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("start")) {
                if (!Bukkit.getOnlinePlayers().isEmpty()) {
                    sendMessage(commandSender, "Please make sure that there's no one on the server first!. Because this will edit player's data and it would be dangerous if there's a player online");
                    return;
                }
                DatabaseWrapper wrp = plugin.getDatabase().getWrapper();
                Map<File, FileConfiguration> configMap = getOldDatas(commandSender);
                if (configMap == null) return;
                Debug.info("Successfully loaded and updated &e" + configMap.size() + " &fold datas!. Now trying to migrate old &eYAML &fdatabase into the new &e" + wrp.getType() + "&f database type!", true, Debug.LogType.BOTH);
                List<PlayerData> datas = new ArrayList<>();
                for (Map.Entry ent : configMap.entrySet()) {
                    FileConfiguration config = (FileConfiguration) ent.getValue();
                    File file = (File) ent.getKey();
                    PlayerData pd = PlayerData.fromOldYaml(config);
                    if (pd == null) {
                        Debug.info("Failed to load into PlayerData object &7(&e" + file.getAbsolutePath() + "&7)", true, Debug.LogType.BOTH);
                        continue;
                    }
                    datas.add(pd);
                }
                datas.forEach(wrp::migrate);
                Debug.info("&fAll data has been migrated successfully!", true, Debug.LogType.BOTH);
            } else {
                sendMessage(commandSender, "Unknown sub command");
            }
        }
    }

    /**
     * Get the old data.
     *
     * @return The list, will throw error if empty
     */
    private Map<File, FileConfiguration> getOldDatas(CommandSender requester) {
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        Debug.info("&fGetting old &bChestShopLimiter &fdata!. This might take a while!", true, Debug.LogType.BOTH);
        final File dataFile = new File(plugin.getDataFolder(), "oldData");
        if (!dataFile.exists()) {
            sendMessage(requester, prefix + "[Migration] Could not find old data file " + dataFile.getAbsolutePath() + " please make sure that the data exists!. For tutorial please refer to spigot page!");
            return null;
        }
        File[] files = dataFile.listFiles();
        if (files == null) {
            sendMessage(requester, prefix + "[Migration] Could not find any old data inside " + dataFile.getAbsolutePath() + " please make sure that the data exists!. For tutorial please refer to spigot page!");
            return null;
        }
        Map<File, FileConfiguration> configs = new HashMap<>();
        for (File f : files) {
            configs.put(f, YamlConfiguration.loadConfiguration(f));
        }
        if (configs.isEmpty()) {
            sendMessage(requester, prefix + "[Migration] Could not find any old data inside " + dataFile.getAbsolutePath() + " please make sure that the data exists!. For tutorial please refer to spigot page!");
            return null;
        }
        for (Map.Entry ent : configs.entrySet()) {
            File file = (File) ent.getKey();
            FileConfiguration config = (FileConfiguration) ent.getValue();
            String fileName = FileUtils.getFileName(file, false); // File name is an UUID
            if (!StringUtils.isUUID(fileName)) {
                Debug.info("Failed to load &e" + fileName + " &fbecause its not a valid UUID!", true, Debug.LogType.BOTH);
                continue;
            }
            config.set("Updated.fileName", fileName);
            try {
                config.save(file);
            } catch (IOException e) {
                Debug.info("Failed to save &e" + fileName + " &ffile will get ignored &7(&bError Message &f: " + e.getMessage() + "&7)", true, Debug.LogType.BOTH);
            }
            Debug.info("Successfully loaded and updated old data file &e" + fileName + ".yml", true, Debug.LogType.BOTH);
        }
        return configs;
    }
}

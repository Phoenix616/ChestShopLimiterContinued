package me.droreo002.cslimit.commands.console;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import me.droreo002.oreocore.utils.strings.StringUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MigrateCommand extends CommandArg {

    private final ChestShopLimiter plugin;
    private final String prefix;

    public MigrateCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("migrate", parent);
        this.plugin = plugin;
        this.prefix = StringUtil.color(plugin.getConfigManager().getMemory().getPrefix());
        setConsoleOnly(true, plugin.getLangManager().getLang(LangPath.NORMAL_CONSOLE_ONLY, null, true));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            sendMessage(commandSender, prefix + "&bChestShopLimiter &fdata &eMigrator &fv1.0, please be aware that this command will get removed on the future version. Type &e/csl migrate start &fto continue");
            return;
        }
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("start")) {
                List<FileConfiguration> config = getOldDatas(commandSender);
                if (config == null) return;
                Debug.info("Successfully loaded &e" + config.size() + " &fold datas!", true, Debug.LogType.BOTH);

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
    private List<FileConfiguration> getOldDatas(CommandSender requester) {
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
        List<FileConfiguration> configs = new ArrayList<>();
        for (File f : files) {
            configs.add(YamlConfiguration.loadConfiguration(f));
        }
        if (configs.isEmpty()) {
            sendMessage(requester, prefix + "[Migration] Could not find any old data inside " + dataFile.getAbsolutePath() + " please make sure that the data exists!. For tutorial please refer to spigot page!");
            return null;
        }
        return configs;
    }
}

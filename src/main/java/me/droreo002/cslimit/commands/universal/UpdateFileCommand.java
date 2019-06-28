package me.droreo002.cslimit.commands.universal;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import me.droreo002.oreocore.configuration.ConfigUpdater;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class UpdateFileCommand extends CommandArg {

    private final ChestShopLimiter plugin;
    private final LangManager lang;
    private final ConfigManager configManager;

    public UpdateFileCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("update-file", parent);
        this.plugin = plugin;
        this.lang = plugin.getLangManager();
        this.configManager = plugin.getConfigManager();

        setPermission("csl.admin.update-file", lang.getLang(LangPath.NORMAL_NO_PERMISSION, null, true));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length != 1) {
            sendMessage(commandSender, lang.getLang(LangPath.NORMAL_TOO_MUCH_ARGS, null, true));
            error(commandSender);
            return;
        }
        try {
            ConfigUpdater.update(lang.getYamlFile(), plugin, "en_lang.yml");
            ConfigUpdater.update(configManager.getYamlFile(), plugin, "config.yml");

            configManager.reloadConfig();
            lang.reloadConfig();
            lang.loadData();

            String message = lang.getLang(LangPath.NORMAL_FILE_UPDATED, null, true);
            sendMessage(commandSender, (message.contains("Error.") ? configManager.getMemory().getPrefix() + "Files has been updated successfully!" : message));
            success(commandSender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

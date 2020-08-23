package me.droreo002.cslimit.commands.universal;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.CSLConfig;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import org.bukkit.command.CommandSender;

public class UpdateFileCommand extends CommandArg {

    private final ChestShopLimiter plugin;
    private final LangManager lang;
    private final CSLConfig configManager;

    public UpdateFileCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("update-file", parent);
        this.plugin = plugin;
        this.lang = plugin.getLangManager();
        this.configManager = plugin.getCslConfig();

        setPermission("csl.admin.update-file", lang.getLang(LangPath.NORMAL_NO_PERMISSION, null, true));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length != 1) {
            sendMessage(commandSender, lang.getLang(LangPath.NORMAL_TOO_MUCH_ARGS, null, true));
            error(commandSender);
            return;
        }
        configManager.saveConfig(true);
        lang.saveConfig(false);

        String message = lang.getLang(LangPath.NORMAL_FILE_UPDATED, null, true);
        sendMessage(commandSender, (message.contains("Error.") ? configManager.getPrefix() + "Files has been updated successfully!" : message));
        success(commandSender);
    }
}

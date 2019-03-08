package me.droreo002.cslimit.commands.universal;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import me.droreo002.oreocore.commands.object.CustomCommandArg;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends CommandArg {

    private final ChestShopLimiter plugin;
    private final LangManager lang;

    public ReloadCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("reload", parent);
        this.plugin = plugin;
        this.lang = plugin.getLangManager();

        setPermission("csl.admin.reload", lang.getLang(LangPath.NORMAL_NO_PERMISSION, null, true));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Debug.info("Command reload has been executed by " + commandSender.getName(), false, Debug.LogType.FILE);
        if (args.length == 1) {
            error(commandSender);
            sendMessage(commandSender, lang.getLang(LangPath.ERROR_USAGE_COMMAND_RELOAD, null, true));
            return;
        }
        if (args.length == 2) {
            String type = args[1];
            switch (type.toLowerCase()) {
                case "config":
                    success(commandSender);
                    plugin.getConfigManager().reloadConfig();
                    sendMessage(commandSender, lang.getLang(LangPath.NORMAL_CONFIG_RELOADED, null, true));
                    return;
                case "lang":
                    success(commandSender);
                    lang.reloadConfig();
                    sendMessage(commandSender, lang.getLang(LangPath.NORMAL_LANG_RELOADED, null, true));
                    return;
                default:
                    success(commandSender);
                    sendMessage(commandSender, lang.getLang(LangPath.ERROR_INVALID_RELOAD_TYPE, null, true));
            }
        } else {
            sendMessage(commandSender, lang.getLang(LangPath.NORMAL_TOO_MUCH_ARGS, null, true));
            error(commandSender);
        }
    }
}

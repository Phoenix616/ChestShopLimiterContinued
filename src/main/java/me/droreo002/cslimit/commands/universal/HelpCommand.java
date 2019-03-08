package me.droreo002.cslimit.commands.universal;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import org.bukkit.command.CommandSender;

public class HelpCommand extends CommandArg {

    private final ChestShopLimiter plugin;
    private final LangManager lang;

    public HelpCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("help", parent);
        this.plugin = plugin;
        this.lang = plugin.getLangManager();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Debug.info("Command help has been executed by " + commandSender.getName(), false, Debug.LogType.FILE);
        lang.getLangList(LangPath.LIST_HELP_MESSAGE, null).forEach(s -> sendMessage(commandSender, s));
        success(commandSender);
    }
}

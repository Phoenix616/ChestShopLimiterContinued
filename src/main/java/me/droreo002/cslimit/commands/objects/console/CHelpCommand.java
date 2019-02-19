package me.droreo002.cslimit.commands.objects.console;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.CSLCommand;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import org.bukkit.command.CommandSender;

public class CHelpCommand extends CSLCommand {

    public CHelpCommand() {
        super("help");
        setConsoleOnly(true);
    }

    @Override
    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {
        final LangManager langManager = main.getLangManager();
        langManager.getLangList(LangPath.LIST_HELP_MESSAGE, null).forEach(sender::sendMessage);
    }
}

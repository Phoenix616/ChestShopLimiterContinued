package me.droreo002.cslimit.commands.objects.player;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.CSLCommand;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends CSLCommand {

    public HelpCommand() {
        super("help");
        setConsoleOnly(false);
    }

    @Override
    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {
        final LangManager lang = main.getLangManager();
        Player player = (Player) sender;
        lang.getLangList(LangPath.LIST_HELP_MESSAGE, null).forEach(player::sendMessage);
        success(player);
    }
}

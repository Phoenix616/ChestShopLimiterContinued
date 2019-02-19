package me.droreo002.cslimit.commands.objects.console;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.CSLCommand;
import org.bukkit.command.CommandSender;

public class CReloadCommand extends CSLCommand {
    
    public CReloadCommand() {
        super("reload");
        setConsoleOnly(true);
    }

    @Override
    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {

    }

    //    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {
//        if (args.length == 1) {
//            sender.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_RELOAD));
//            return;
//        }
//        if (args.length == 2) {
//            String type = args[1];
//            switch (type.toLowerCase()) {
//                case "config":
//                    main.getConfigManager().reloadConfig();
//                    sender.sendMessage(main.getLangManager().getMessage(MessageType.CONFIG_RELOADED));
//                    return;
//                case "lang":
//                    main.getLangManager().reloadLangFile();
//                    sender.sendMessage(main.getLangManager().getMessage(MessageType.LANG_RELOADED));
//                    return;
//                default:
//                    sender.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_RELOAD_TYPE));
//            }
//        } else {
//            sender.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
//        }
//    }
}

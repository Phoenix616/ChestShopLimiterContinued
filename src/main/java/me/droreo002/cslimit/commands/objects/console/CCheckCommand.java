package me.droreo002.cslimit.commands.objects.console;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.CSLCommand;
import org.bukkit.command.CommandSender;

public class CCheckCommand extends CSLCommand {

    public CCheckCommand() {
        super("check");
        setConsoleOnly(true);
    }

    @Override
    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {

    }

    // TODO : Continue
//    @Override
//    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {
//        if (args.length == 1) {
//            sender.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_CHECK));
//            return;
//        }
//        if (args.length == 2) {
//            String name = args[1];
//            Player target = Bukkit.getPlayerExact(name);
//            if (target == null) {
//                if (main.getHookManager().isEssentials()) {
//                    EssentialsHook hook = (EssentialsHook) main.getHookManager().getHookMap().get("Essentials");
//                    if (hook.getUser(name) == null) {
//                        sender.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
//                        return;
//                    }
//                    OfflinePlayer off = Bukkit.getOfflinePlayer(hook.getUser(name).getConfigUUID());
//                    main.getLangManager().sendCheckFormat(sender, off);
//                    return;
//                } else {
//                    CMIHook hook = (CMIHook) main.getHookManager().getHookMap().get("CMI");
//                    if (hook.getPlayerManager().getUser(name) == null) {
//                        sender.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
//                        return;
//                    }
//                    OfflinePlayer off = Bukkit.getOfflinePlayer(hook.getPlayerManager().getUser(name).getUniqueId());
//                    main.getLangManager().sendCheckFormat(sender, off);
//                    return;
//                }
//            }
//            main.getLangManager().sendCheckFormat(sender, target);
//        } else {
//            sender.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
//        }
//    }
}

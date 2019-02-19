package me.droreo002.cslimit.commands.objects.player;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.CSLCommand;
import org.bukkit.command.CommandSender;

public class ResetCommand extends CSLCommand {

    public ResetCommand() {
        super("reset");
        setHasPermission(true);
        setPermission("csl.admin.reset");
        setConsoleOnly(false);
    }

    @Override
    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {

    }

    //    @Override
//    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {
//        Player player = (Player) sender;
//        if (args.length == 1) {
//            player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_RESET));
//            error(player);
//            return;
//        }
//        if (args.length == 2) {
//            String name = args[1];
//            Player target = Bukkit.getPlayerExact(name);
//            if (target == null) {
//                if (main.getHookManager().isEssentials()) {
//                    EssentialsHook hook = (EssentialsHook) main.getHookManager().getHookMap().get("Essentials");
//                    if (hook.getUser(name) == null) {
//                        error(player);
//                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
//                        return;
//                    }
//                    success(player);
//                    OfflinePlayer off = Bukkit.getOfflinePlayer(hook.getUser(name).getConfigUUID());
//                    main.getApi().resetShopCreated(off);
//                    player.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET, off.getPlayer()));
//                    return;
//                } else {
//                    CMIHook hook = (CMIHook) main.getHookManager().getHookMap().get("CMI");
//                    if (hook.getPlayerManager().getUser(name) == null) {
//                        error(player);
//                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
//                        return;
//                    }
//                    success(player);
//                    OfflinePlayer off = Bukkit.getOfflinePlayer(hook.getPlayerManager().getUser(name).getUniqueId());
//                    main.getApi().resetShopCreated(off);
//                    player.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET, off.getPlayer()));
//                    return;
//                }
//            }
//            success(player);
//            player.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET, target));
//            main.getApi().resetShopCreated(target);
//            target.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET_OTHER, player));
//        } else {
//            player.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
//            error(player);
//        }
//    }
}

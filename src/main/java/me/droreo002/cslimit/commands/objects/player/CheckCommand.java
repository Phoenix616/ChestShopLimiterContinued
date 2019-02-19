package me.droreo002.cslimit.commands.objects.player;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.CSLCommand;
import me.droreo002.cslimit.hook.objects.CMIHook;
import me.droreo002.cslimit.hook.objects.EssentialsHook;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckCommand extends CSLCommand {

    public CheckCommand() {
        super("check");
        setHasPermission(true);
        setPermission("csl.admin.check");
        setConsoleOnly(false);
    }

    @Override
    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {

    }

    //    @Override
//    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {
//        Player player = (Player) sender;
//        if (args.length == 1) {
//            error(player);
//            player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_CHECK));
//            return;
//        }
//        if (args.length == 2) {
//            String name = args[1];
//            Player target = Bukkit.getPlayerExact(name);
//            if (target == null) {
//                if (main.getHookManager().isEssentials()) {
//                    EssentialsHook hook = (EssentialsHook) main.getHookManager().getHookMap().get("Essentials");
//                    if (hook.getUser(name) == null) {
//                        success(player);
//                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
//                        return;
//                    }
//                    success(player);
//                    OfflinePlayer off = Bukkit.getOfflinePlayer(hook.getUser(name).getConfigUUID());
//                    main.getLangManager().sendCheckFormat(player, off);
//                    return;
//                } else {
//                    // Use CMI
//                    CMIHook hook = (CMIHook) main.getHookManager().getHookMap().get("CMI");
//                    if (hook.getPlayerManager().getUser(name) == null) {
//                        success(player);
//                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
//                        return;
//                    }
//                    success(player);
//                    OfflinePlayer off = Bukkit.getOfflinePlayer(hook.getPlayerManager().getUser(name).getUniqueId());
//                    main.getLangManager().sendCheckFormat(player, off);
//                    return;
//                }
//            }
//            success(player);
//            main.getLangManager().sendCheckFormat(player, target);
//        } else {
//            error(player);
//            player.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
//        }
//    }
}

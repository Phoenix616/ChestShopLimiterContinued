package me.droreo002.cslimit.commands.objects.player;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.CSLCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatusCommand extends CSLCommand {

    public StatusCommand() {
        super("status");
        setHasPermission(true);
        setPermission("csl.player.checkstatus");
        setConsoleOnly(false);
    }

    @Override
    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {

    }

    //    @Override
//    public void execute(ChestShopLimiter main, CommandSender sender, String[] args) {
//        Player player = (Player) sender;
//        if (args.length == 1) {
//            success(player);
//            for (String s : main.getLangManager().getMessageList(player, MessageType.PLAYER_STATUS)) {
//                player.sendMessage(s);
//            }
//        } else {
//            error(player);
//            player.sendMessage(LangManager.getInstance().getMessage(MessageType.ERROR_INVALID_USAGE_STATUS));
//        }
//    }
}

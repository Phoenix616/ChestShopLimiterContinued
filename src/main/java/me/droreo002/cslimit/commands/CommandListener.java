package me.droreo002.cslimit.commands;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.Debug;
import me.droreo002.oreocore.utils.misc.SoundObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {

    private final ChestShopLimiter main = ChestShopLimiter.getInstance();
    private final SoundObject success;
    private final SoundObject fail;

    public CommandListener() {
        this.success = main.getConfigManager().getMemory().getSuccessSound();
        this.fail = main.getConfigManager().getMemory().getFailureSound();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return handleConsole(sender, args);
        }
        final LangManager lang = main.getLangManager();
        Player player = (Player) sender;
        if (args.length > 0) {
            int success = 0;
            for (CSLCommand clazz : CommandManager.getCommands()) {
                if (clazz.getCommandKey().equalsIgnoreCase(args[0])) {
                    if (clazz.isHasPermission()) {
                        if (!player.hasPermission(clazz.getPermission())) {
                            clazz.error(player);
                            player.sendMessage(lang.getLang(LangPath.NORMAL_NO_PERMISSION, null, true));
                            return true;
                        }
                    }
                    clazz.execute(main, player, args);
                    success++;
                    break;
                }
            }
            if (success == 0) {
                fail.send(player);
                player.sendMessage(lang.getLang(LangPath.ERROR_USAGE_COMMAND_UNKNOWN, null, true));
                return true;
            }
        } else {
            success.send(player);
            lang.getAbout().forEach(player::sendMessage);
            return true;
        }
        return true;
    }

    private boolean handleConsole(CommandSender sender, String[] args) {
        final LangManager lang = main.getLangManager();
        if (args.length > 0) {
            int success = 0;
            for (CSLCommand clazz : CommandManager.getCommands()) {
                if (clazz.getCommandKey().equalsIgnoreCase(args[0])) {
                    if (clazz.isConsoleOnly()) {
                        clazz.execute(main, sender, args);
                    }
                    success++;
                    break;
                }
            }
            if (success == 0) {
                sender.sendMessage(lang.getLang(LangPath.ERROR_USAGE_COMMAND_UNKNOWN, null, true));
                return true;
            }
        } else {
            lang.getAbout().forEach(s -> Debug.log(s, false));
            return true;
        }
        return true;
    }
}

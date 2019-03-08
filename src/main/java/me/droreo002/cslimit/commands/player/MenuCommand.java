package me.droreo002.cslimit.commands.player;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.inventory.MenuInventory;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand extends CommandArg {

    private final ChestShopLimiter plugin;

    public MenuCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("menu", parent);
        this.plugin = plugin;
        LangManager lang = plugin.getLangManager();

        setPlayerOnly(true, lang.getLang(LangPath.NORMAL_PLAYER_ONLY, null, true));
        setPermission("csl.player.menu", lang.getLang(LangPath.NORMAL_NO_PERMISSION, null, true));
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        Debug.info("Command menu has been executed by " + commandSender.getName(), false, Debug.LogType.FILE);
        Player player = (Player) commandSender;
        new MenuInventory(plugin).open(player);
    }
}

package me.droreo002.cslimit.commands;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.manager.Debug;
import me.droreo002.oreocore.utils.misc.SoundObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TabManager implements TabCompleter {

    private final Collection<String> completions = new ArrayList<>();
    private final SoundObject success;

    public TabManager() {
        final ChestShopLimiter main = ChestShopLimiter.getInstance();
        final ConfigManager.Memory memory = main.getConfigManager().getMemory();
        this.success = memory.getSuccessSound();
         /*
        Tabs
         */
        Debug.log("&fRegistering tab completer for player commands...", false);
        completions.add("help");
        completions.add("check");
        completions.add("reload");
        completions.add("status");
        completions.add("reset");
        completions.forEach(s -> Debug.log("     &fTab completer with the id of &e" + s + " &fhas been registered!", false));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return createReturnList(new ArrayList<>(completions), args[0]);
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("reload")) {
                List<String> toReturn = new ArrayList<>();
                toReturn.add("config");
                toReturn.add("lang");
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    success.send(player);
                }
                return createReturnList(toReturn, args[1]);
            }
            return null;
        }
        else {
            return null;
        }
    }

    private List<String> createReturnList(List<String> list, String string) {
        if (string.equals("")) return list;

        List<String> returnList = new ArrayList<>();
        for (String item : list) {
            if (item.toLowerCase().startsWith(string.toLowerCase())) {
                returnList.add(item);
            }
        }
        return returnList;
    }
}

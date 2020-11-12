package me.droreo002.cslimit.commands.player;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.database.object.PlayerData;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.utils.PlayerUtils;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class EditorCommand extends CommandArg {

    private final ChestShopLimiter plugin;
    private final LangManager lang;

    public EditorCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("edit", parent);
        this.plugin = plugin;
        this.lang = plugin.getLangManager();

        setPlayerOnly(true, lang.getLang(LangPath.NORMAL_PLAYER_ONLY, null, true));
        setPermission("csl.admin.edit", lang.getLang(LangPath.NORMAL_NO_PERMISSION, null, true));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("set")) {
                if (args[2].equalsIgnoreCase("max-shop")) {
                    if (args.length != 5) {
                        error(commandSender);
                        sendMessage(commandSender, lang.getLang(LangPath.ERROR_USAGE_COMMAND_EDIT_MAX,null, true));
                        return;
                    }
                    process(commandSender, args, true);
                }
                if (args[2].equalsIgnoreCase("shop-count")) {
                    if (args.length != 5) {
                        error(commandSender);
                        sendMessage(commandSender, lang.getLang(LangPath.ERROR_USAGE_COMMAND_EDIT_COUNT,null, true));
                        return;
                    }
                    process(commandSender, args, false);
                }
            }
        } else {
            error(commandSender);
            sendMessage(commandSender, lang.getLang(LangPath.ERROR_USAGE_COMMAND_EDIT, null, true));
        }
    }

    private OfflinePlayer get(CommandSender commandSender, String name) {
        Player target = Bukkit.getPlayerExact(name);
        if (target == null) {
            UUID offlineUUID;
            try {
                offlineUUID = PlayerUtils.getPlayerUuid(name).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
            OfflinePlayer off = Bukkit.getOfflinePlayer(offlineUUID);
            if (!off.hasPlayedBefore()) {
                sendMessage(commandSender, lang.getLang(LangPath.ERROR_PLAYER_NEVER_PLAYED, null, true));
                error(commandSender);
                return null;
            }
            return off;
        }
        return target;
    }

    private void process(CommandSender commandSender, String[] args, boolean maxShop) {
        OfflinePlayer player = get(commandSender, args[3]);
        if (player == null) return;

        int value;
        try {
            value = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            error(commandSender);
            sendMessage(commandSender, lang.getLang(LangPath.TE_ERROR_NOT_INTEGER, null, true));
            return;
        }
        boolean add = args[1].equalsIgnoreCase("add");
        PlayerData data = plugin.getDatabase().getWrapper().getPlayerData(player.getUniqueId());
        TextPlaceholder placeholder = TextPlaceholder.of("%target%", player.getName()).add("%value%", value);
        String message;

        if (add) {
            if (maxShop) {
                message = lang.getLang(LangPath.TE_SUCCESS_ADD_MAX_SHOP, placeholder, true);
                data.addMaxShop(value);
            } else {
                message = lang.getLang(LangPath.TE_ADD_CURR_SHOP, placeholder, true);
                data.addShopCreated(value);
            }
        } else {
            if (maxShop) {
                message = lang.getLang(LangPath.TE_SUCCESS_CHANGE_MAX_SHOP, placeholder, true);
                data.setMaxShop(value);
            } else {
                message = lang.getLang(LangPath.TE_SUCCESS_CHANGE_CURR_SHOP, placeholder, true);
                data.setShopCreated(value);
            }
        }

        success(commandSender);
        sendMessage(commandSender, message);
        plugin.getDatabase().getWrapper().savePlayerData(data);
    }
}

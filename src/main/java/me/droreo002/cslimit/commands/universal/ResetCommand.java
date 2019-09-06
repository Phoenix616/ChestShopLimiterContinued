package me.droreo002.cslimit.commands.universal;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.hook.objects.CMIHook;
import me.droreo002.cslimit.hook.objects.EssentialsHook;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.cslimit.utils.PlayerUtils;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import me.droreo002.oreocore.utils.item.helper.ItemMetaType;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ResetCommand extends CommandArg {

    private final ChestShopLimiter plugin;
    private final LangManager lang;

    public ResetCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("reset", parent);
        this.plugin = plugin;
        this.lang = plugin.getLangManager();

        setPermission("csl.admin.reset", lang.getLang(LangPath.NORMAL_NO_PERMISSION, null, true));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Debug.info("Command reset has been executed by " + commandSender.getName(), false, Debug.LogType.FILE);
        if (args.length == 1) {
            sendMessage(commandSender, lang.getLang(LangPath.ERROR_USAGE_COMMAND_RESET, null, true));
            error(commandSender);
            return;
        }
        if (args.length == 2) {
            String name = args[1];
            Player target = Bukkit.getPlayerExact(name);
            if (target == null) {
                success(commandSender);
                UUID offUUID = null;
                try {
                    offUUID = PlayerUtils.getPlayerUuid(name).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                OfflinePlayer off = Bukkit.getOfflinePlayer(offUUID);
                if (!off.hasPlayedBefore()) {
                    sendMessage(commandSender, lang.getLang(LangPath.ERROR_PLAYER_NEVER_PLAYED, null, true));
                    error(commandSender);
                    return;
                }
                reset(commandSender, off.getUniqueId());
                sendMessage(commandSender, lang.getLang(LangPath.NORMAL_SHOP_CREATED_RESET, new TextPlaceholder(ItemMetaType.NONE, "%player%", off.getName()), true));
                return;
            }
            success(commandSender);
            reset(commandSender, target.getUniqueId());
            sendMessage(commandSender, lang.getLang(LangPath.NORMAL_SHOP_CREATED_RESET, new TextPlaceholder(ItemMetaType.NONE, "%player%", target.getName()), true));
            sendMessage(target, lang.getLang(LangPath.NORMAL_SHOP_CREATED_RESET_OTHER, new TextPlaceholder(ItemMetaType.NONE,"%executor%", commandSender.getName()), true));
        } else {
            sendMessage(commandSender, lang.getLang(LangPath.NORMAL_TOO_MUCH_ARGS, null, true));
            error(commandSender);
        }
    }

    private void reset(CommandSender commandSender, UUID uuid) {
        PlayerData data = plugin.getChestShopAPI().getData(uuid);
        if (data == null) {
            error(commandSender);
            sendMessage(commandSender, lang.getLang(LangPath.NORMAL_DATA_NOT_FOUND, null, true));
            return;
        }
        data.setShopCreated(0);
        plugin.getChestShopAPI().saveData(data);
    }
}

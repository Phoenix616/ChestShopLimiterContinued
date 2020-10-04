package me.droreo002.cslimit.commands.player;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.database.object.PlayerData;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import me.droreo002.oreocore.utils.item.helper.ItemMetaType;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class StatusCommand extends CommandArg {

    private final ChestShopLimiter plugin;
    private final LangManager lang;

    public StatusCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("status", parent);
        this.plugin = plugin;
        this.lang = plugin.getLangManager();

        setPlayerOnly(true, lang.getLang(LangPath.NORMAL_PLAYER_ONLY, null, true));
        setPermission("csl.player.checkstatus", lang.getLang(LangPath.NORMAL_NO_PERMISSION, null, true));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Debug.info("Command status has been executed by " + commandSender.getName(), false, Debug.LogType.FILE);
        Player player = (Player) commandSender;
        if (args.length == 1) {
            success(commandSender);
            sendStatus(player, player.getUniqueId());
            return;
        }
        error(commandSender);
        sendMessage(commandSender, lang.getLang(LangPath.ERROR_USAGE_COMMAND_STATUS, null, true));
    }

    private void sendStatus(Player sender, UUID target) {
        final PlayerData data = plugin.getChestShopAPI().getData(target);
        String shopLimit = String.valueOf(data.getMaxShop());
        if (sender.hasPermission("csl.limit.unlimited")) shopLimit = lang.getLang(LangPath.MISC_SHOP_LIMIT_UNLIMITED, null, false);
        List<String> message = lang.getLangList(LangPath.LIST_PLAYER_STATUS_MESSAGE,
                new TextPlaceholder(ItemMetaType.NONE, "%shopcreated%", String.valueOf(data.getShopCreated()))
                .add(ItemMetaType.NONE, "%shoplimit%", shopLimit));
        message.forEach(sender::sendMessage);
    }
}

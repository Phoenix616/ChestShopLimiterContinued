package me.droreo002.cslimit.commands.dev;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.conversation.helper.ConversationType;
import me.droreo002.cslimit.conversation.helper.SessionDataKey;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * REMOVE WHEN DONE OR UNREGISTER!
 */
public class DebugCommand extends CommandArg {

    private final ChestShopLimiter plugin;

    public DebugCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("debug", parent);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        Player player = (Player) commandSender;
        success(player);
        Map<SessionDataKey, Object> data = new HashMap<>();
        data.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.CHANGE_MAX_SHOP);
        data.put(SessionDataKey.PLAYER_DATA, plugin.getChestShopAPI().getData(player.getUniqueId()));
        
        plugin.getConversationManager().sendConversation(player, ConversationType.CHANGE_MAX_SHOP, data);
    }
}

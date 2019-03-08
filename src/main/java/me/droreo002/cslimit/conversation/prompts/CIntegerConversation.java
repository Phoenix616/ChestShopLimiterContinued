package me.droreo002.cslimit.conversation.prompts;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.conversation.helper.ConversationType;
import me.droreo002.cslimit.conversation.helper.SessionDataKey;
import me.droreo002.cslimit.database.PlayerData;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class CIntegerConversation extends StringPrompt {

    private final ChestShopLimiter plugin;
    private final LangManager lang;
    private final ConfigManager.Memory memory;

    public CIntegerConversation(ChestShopLimiter plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLangManager();
        this.memory = plugin.getConfigManager().getMemory();
    }

    @Override
    public String getPromptText(ConversationContext context) {
        Player forWhom = (Player) context.getForWhom();
        ConversationType type = (ConversationType) context.getSessionData(SessionDataKey.CONVERSATION_TYPE);
        PlayerData targetData = (PlayerData) context.getSessionData(SessionDataKey.PLAYER_DATA);
        String targetName = Bukkit.getOfflinePlayer(targetData.getPlayerUUID()).getName();
        TextPlaceholder pl = new TextPlaceholder("%target", targetName);
        String result;
        switch (type) {
            case CHANGE_MAX_SHOP:
                result = lang.getLang(LangPath.TE_CHANGE_MAX_SHOP, pl, true);
                break;
            case CHANGE_CURRENT_SHOP:
                result = lang.getLang(LangPath.TE_CHANGE_CURR_SHOP, pl, true);
                break;
            case ADD_MAX_SHOP:
                result = lang.getLang(LangPath.TE_ADD_MAX_SHOP, pl, true);
                break;
            case ADD_CURRENT_SHOP:
                result = lang.getLang(LangPath.TE_ADD_CURR_SHOP, pl, true);
                break;
            default:
                memory.getTEditorFailureSound().send(forWhom);
                return "An internal error occurred, please contact admin!";
        }
        memory.getTEditorSuccessSound().send(forWhom);
        return result;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        PlayerData targetData = (PlayerData) context.getSessionData(SessionDataKey.PLAYER_DATA);
        ConversationType type = (ConversationType) context.getSessionData(SessionDataKey.CONVERSATION_TYPE);
        int numberInput;
        try {
            numberInput = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            player.sendMessage(lang.getLang(LangPath.TE_ERROR_NOT_INTEGER, null, true));
            memory.getTEditorFailureSound().send(player);
            return Prompt.END_OF_CONVERSATION;
        }
        TextPlaceholder pl = new TextPlaceholder("%value", String.valueOf(numberInput));
        switch (type) {
            case CHANGE_MAX_SHOP:
                player.sendMessage(lang.getLang(LangPath.TE_SUCCESS_CHANGE_MAX_SHOP, pl, true));
                targetData.setMaxShop(numberInput);
                break;
            case CHANGE_CURRENT_SHOP:
                if (numberInput > targetData.getShopCreated()) {
                    player.sendMessage(lang.getLang(LangPath.TE_SHOP_CREATED_GREATER, null, true));
                    memory.getTEditorFailureSound().send(player);
                    return Prompt.END_OF_CONVERSATION;
                }
                player.sendMessage(lang.getLang(LangPath.TE_SUCCESS_CHANGE_CURR_SHOP, pl, true));
                targetData.setShopCreated(numberInput);
                break;
            case ADD_CURRENT_SHOP:
                if ((numberInput + targetData.getShopCreated()) > targetData.getShopCreated()) {
                    player.sendMessage(lang.getLang(LangPath.TE_SHOP_CREATED_GREATER, null, true));
                    memory.getTEditorFailureSound().send(player);
                    return Prompt.END_OF_CONVERSATION;
                }
                player.sendMessage(lang.getLang(LangPath.TE_ADD_CURR_SHOP, pl, true));
                targetData.addShopCreated(numberInput);
                break;
            case ADD_MAX_SHOP:
                player.sendMessage(lang.getLang(LangPath.TE_SUCCESS_ADD_MAX_SHOP, pl, true));
                targetData.addMaxShop(numberInput);
                break;
        }
        memory.getTEditorSuccessSound().send(player);
        plugin.getDatabase().getWrapper().updatePlayerData(targetData);
        return Prompt.END_OF_CONVERSATION;
    }
}

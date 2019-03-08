package me.droreo002.cslimit.conversation;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.conversation.helper.ConversationType;
import me.droreo002.cslimit.conversation.helper.SessionDataKey;
import me.droreo002.cslimit.conversation.prompts.CIntegerConversation;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ConversationManager implements ConversationAbandonedListener {

    @Getter
    private final ConversationFactory conversationFactory;
    @Getter
    private final List<Conversation> conversations;

    private final ChestShopLimiter plugin;
    private final LangManager langManager;

    public ConversationManager(ChestShopLimiter plugin) {
        this.plugin = plugin;
        this.conversations = new ArrayList<>();
        this.langManager = plugin.getLangManager();
        this.conversationFactory = new ConversationFactory(plugin)
                .withModality(false)
                .withLocalEcho(false)
                .addConversationAbandonedListener(this)
                .withEscapeSequence("exit")
                .thatExcludesNonPlayersWithMessage(langManager.getLang(LangPath.TE_PLAYER_ONLY, null, true));
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent abandonedEvent) {
        Player player = (Player) abandonedEvent.getContext().getForWhom();
        remove(player);
        player.sendMessage(langManager.getLang(LangPath.TE_CLOSED, null, true));
    }

    /**
     * Send the conversation to the player with the specified data
     *
     * @param player : The player
     * @param type : The type of this conversation
     * @param sessionData : The session data in HashMap
     */
    public void sendConversation(Player player, ConversationType type, Map<SessionDataKey, Object> sessionData) {
        Conversation conversation;
        switch (type) {
            case CHANGE_MAX_SHOP:
                conversation = conversationFactory
                        .withFirstPrompt(new CIntegerConversation(plugin))
                        .buildConversation(player);
                break;
            case CHANGE_CURRENT_SHOP:
                conversation = conversationFactory
                        .withFirstPrompt(new CIntegerConversation(plugin))
                        .buildConversation(player);
                break;
            case ADD_MAX_SHOP:
                conversation = conversationFactory
                        .withFirstPrompt(new CIntegerConversation(plugin))
                        .buildConversation(player);
                break;
            case ADD_CURRENT_SHOP:
                conversation = conversationFactory
                        .withFirstPrompt(new CIntegerConversation(plugin))
                        .buildConversation(player);
                break;
            default:
                return;
        }
        if (!isOnConversation(player)) conversations.add(conversation);
        for (Map.Entry ent : sessionData.entrySet()) {
            SessionDataKey key = (SessionDataKey) ent.getKey();
            conversation.getContext().setSessionData(key, ent.getValue());
        }
        conversation.begin();
    }

    /**
     * Check if player is cached on the conversations list
     *
     * @param player : The player to check
     * @return true if available, false otherwise
     */
    public boolean isOnConversation(Player player) {
        for (Conversation s : conversations) {
            if (s.getContext().getForWhom().equals(player)) return true;
        }
        return false;
    }

    /**
     * Remove the player from the conversations list cache
     *
     * @param player : The player
     */
    public void remove(Player player) {
        if (!isOnConversation(player)) return;
        List<Conversation> conver = new ArrayList<>();
        for (Conversation con : conversations) {
            if (con.getContext().getForWhom().equals(player)) continue;
            conver.add(con);
        }
        conversations.clear();
        conversations.addAll(conver);
    }
}

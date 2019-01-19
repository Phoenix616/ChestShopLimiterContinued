package me.droreo002.cslimit.hook.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.droreo002.cslimit.ChestShopLimiter;
import org.bukkit.entity.Player;

public class CSLPlaceholder extends PlaceholderExpansion {

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "csl";
    }

    @Override
    public String getAuthor() {
        return "DrOreo002";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        // TODO : Continue this

//        ChestShopLimiter main = ChestShopLimiter.get();
//        if (params.equalsIgnoreCase("player_maxshop")) {
//            return String.valueOf(main.getApi().getShopLimitValue(p));
//        }
//        if (params.equalsIgnoreCase("player_shopcreated")) {
//            return String.valueOf(main.getApi().getShopCreated(p));
//        }
        return "Invalid placeholder.";
    }
}

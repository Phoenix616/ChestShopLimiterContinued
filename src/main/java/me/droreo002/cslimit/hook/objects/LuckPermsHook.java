package me.droreo002.cslimit.hook.objects;

import me.droreo002.cslimit.hook.ChestShopHook;
import me.droreo002.cslimit.manager.Debug;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.Bukkit;

public class LuckPermsHook implements ChestShopHook {

    private LuckPermsApi luckPerms;

    @Override
    public String getPluginName() {
        return "LuckPerms";
    }

    @Override
    public boolean process() {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            luckPerms = LuckPerms.getApi();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void hookSuccess() {
        Debug.log("     &f> &aLuckPerms &fhas been hooked!", false);
    }

    @Override
    public void hookFailed() {
        Debug.log("     &f> Cannot hook into &aLuckPerms &fbecause the plugin cannot be found!", false);
    }

    @Override
    public boolean disablePluginIfNotFound() {
        return false;
    }

    public LuckPermsApi getLuckPerms() {
        return luckPerms;
    }

//    public void setupShopLimitValue(PlayerData data, Player player, ChestShopLimiter main) {
//        ConfigurationSection cs = main.getConfigManager().getConfig().getConfigurationSection("ShopLimitLuckperms");
//        User user = getLuckPerms().getUser(player.getUniqueId());
//        if (user == null) {
//            player.sendMessage(main.getPrefix() + main.getLangManager().getMessage(MessageType.ERROR_CANNOT_LOAD_DATA));
//            return;
//        }
//        if (data.isSet("Info.normalPlayerPermission")) {
//            data.set("Info.normalPlayerPermission", null);
//        }
//        if (!data.isSet("Info.LuckPermsPlayerPermission")) {
//            data.set("Info.LuckPermsPlayerPermission", "firstTime");
//        }
//        data.save();
//        String currPermission = data.getString("Info.LuckPermsPlayerPermission");
//        for (String s : cs.getKeys(false)) {
//            if (s.equalsIgnoreCase(user.getPrimaryGroup())) {
//                if (currPermission.equalsIgnoreCase(s)) {
//                        /*
//                        Make a checker so it wont be laggy
//                         */
//                    if (data.isSet("Info.shopLimit")) {
//                        int shopLimit = main.getConfigManager().getConfig().getInt("ShopLimitLuckperms." + s + ".limit");
//                        int shopLimitPlayer = data.getInt("Info.shopLimit");
//                            /*
//                            That mean the shop limit is different than the default one
//                             */
//                        if (shopLimit != shopLimitPlayer) {
//                            data.set("Info.shopLimit", shopLimit);
//                            data.save();
//                        }
//                    }
//                    return;
//                }
//                data.set("Info.shopLimit", main.getConfigManager().getConfig().getInt("ShopLimitLuckperms." + s + ".limit"));
//                data.set("Info.LuckPermsPlayerPermission", s);
//                break;
//            }
//        }
//        data.save();
//    }
}

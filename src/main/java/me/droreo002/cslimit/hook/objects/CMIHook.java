package me.droreo002.cslimit.hook.objects;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.PlayerManager;
import me.droreo002.cslimit.hook.ChestShopHook;
import me.droreo002.cslimit.manager.logger.Debug;
import org.bukkit.Bukkit;

public class CMIHook implements ChestShopHook {

    private CMI cmi;
    private PlayerManager manager;

    @Override
    public String getPluginName() {
        return "CMI";
    }

    @Override
    public boolean process() {
        if (Bukkit.getPluginManager().getPlugin("CMI") != null) {
            cmi = (CMI) Bukkit.getPluginManager().getPlugin("CMI");
            manager = cmi.getPlayerManager();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void hookSuccess() {
        Debug.info("     &f> &bCMI &fhas been hooked!", false, Debug.LogType.BOTH);
    }

    @Override
    public void hookFailed() {
        Debug.info("     &f> Cannot hook into &bCMI &fbecause the plugin cannot be found!", false, Debug.LogType.BOTH);
    }

    @Override
    public boolean disablePluginIfNotFound() {
        return false;
    }

    public CMI getCmi() {
        return cmi;
    }

    public PlayerManager getPlayerManager() {
        return manager;
    }
}

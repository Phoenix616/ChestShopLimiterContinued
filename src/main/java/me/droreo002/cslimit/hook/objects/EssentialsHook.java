package me.droreo002.cslimit.hook.objects;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.droreo002.cslimit.hook.ChestShopHook;
import me.droreo002.cslimit.manager.logger.Debug;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EssentialsHook implements ChestShopHook {

    private Essentials essentials;

    @Override
    public String getPluginName() {
        return "Essentials";
    }

    @Override
    public boolean process() {
        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void hookSuccess() {
        Debug.info("     &f> &cEssentials &fhas been hooked!", false, Debug.LogType.BOTH);
    }

    @Override
    public void hookFailed() {
        Debug.info("     &f> Cannot hook into &cEssentials &fbecause the plugin cannot be found!", false, Debug.LogType.BOTH);
    }

    @Override
    public boolean disablePluginIfNotFound() {
        return false;
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public User getUser(String name) {
        return essentials.getUser(name);
    }

    public User getUser(Player name) {
        return essentials.getUser(name);
    }

    public User getUser(UUID name) {
        return essentials.getUser(name);
    }
}

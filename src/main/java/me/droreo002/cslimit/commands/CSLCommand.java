package me.droreo002.cslimit.commands;

import lombok.Getter;
import lombok.Setter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.utils.misc.SoundObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CSLCommand {

    /*
    Sounds
     */
    @Getter
    private SoundObject success;
    @Getter
    private SoundObject fail;

    /*
    Variables
     */
    @Getter
    private String commandKey;

    @Setter
    @Getter
    private String permission;

    @Setter
    @Getter
    private boolean hasPermission;

    @Setter
    @Getter
    private boolean consoleOnly;

    public CSLCommand(String commandKey) {
        final ChestShopLimiter main = ChestShopLimiter.getInstance();
        final ConfigManager.Memory mem = main.getConfigManager().getMemory();

        this.commandKey = commandKey;
        this.success = mem.getSuccessSound();
        this.fail = mem.getFailureSound();
        this.hasPermission = false;
        this.consoleOnly = false;

        // Setup
        register();
    }

    private void register() {
        Debug.info("     &fCommand with the name of &e" + commandKey + "&f has been registered!", false, true);
        CommandManager.registerCommand(this);
    }

    public void error(Player player) {
        fail.send(player);
    }

    public void success(Player player) {
        success.send(player);
    }

    public abstract void execute(ChestShopLimiter main, CommandSender sender, String[] args);
}

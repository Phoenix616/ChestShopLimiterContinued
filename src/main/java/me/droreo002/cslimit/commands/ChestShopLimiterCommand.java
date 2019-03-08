package me.droreo002.cslimit.commands;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.console.MigrateCommand;
import me.droreo002.cslimit.commands.dev.DebugCommand;
import me.droreo002.cslimit.commands.player.MenuCommand;
import me.droreo002.cslimit.commands.player.StatusCommand;
import me.droreo002.cslimit.commands.universal.CheckCommand;
import me.droreo002.cslimit.commands.universal.HelpCommand;
import me.droreo002.cslimit.commands.universal.ReloadCommand;
import me.droreo002.cslimit.commands.universal.ResetCommand;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.oreocore.commands.CustomCommand;
import me.droreo002.oreocore.commands.CustomCommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ChestShopLimiterCommand extends CustomCommand {

    @Getter
    private final ChestShopLimiter plugin;
    @Getter
    private final List<String> tabComplete = new ArrayList<>();

    public ChestShopLimiterCommand(JavaPlugin owner, ChestShopLimiter plugin) {
        super(owner, "chestshoplimiter", "csl");
        this.plugin = plugin;
        final ConfigManager.Memory memory = plugin.getConfigManager().getMemory();
        final LangManager langManager = plugin.getLangManager();

        setErrorSound(memory.getFailureSound());
        setSuccessSound(memory.getSuccessSound());
        setArgumentNotFoundMessage(langManager.getLang(LangPath.ERROR_USAGE_COMMAND_UNKNOWN, null, true));

        /*
        Add args
         */
        addArgument(new CheckCommand(this, plugin));
        addArgument(new HelpCommand(this, plugin));
        addArgument(new ReloadCommand(this, plugin));
        addArgument(new ResetCommand(this, plugin));
        addArgument(new StatusCommand(this, plugin));
        addArgument(new MenuCommand(this, plugin));
        addArgument(new MigrateCommand(this, plugin));
        //addArgument(new DebugCommand(this, plugin));

        /*
        Add tab completer
         */
        tabComplete.add("check");
        tabComplete.add("help");
        tabComplete.add("reload");
        tabComplete.add("reset");
        tabComplete.add("status");
        tabComplete.add("menu");

        // Last > Register it
        CustomCommandManager.registerCommand(owner, this);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        Debug.info("Command csl or chestshoplimiter has been executed by " + commandSender.getName(), false, Debug.LogType.FILE);
        plugin.getLangManager().getAbout().forEach(s -> sendMessage(commandSender, s));
        successSound(commandSender);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return createReturnList(new ArrayList<>(tabComplete), args[0]);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("reload")) {
                List<String> toReturn = new ArrayList<>();
                toReturn.add("config");
                toReturn.add("lang");
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    successSound(player);
                }
                return createReturnList(toReturn, args[1]);
            }
            return null;
        } else {
            return null;
        }
    }
}

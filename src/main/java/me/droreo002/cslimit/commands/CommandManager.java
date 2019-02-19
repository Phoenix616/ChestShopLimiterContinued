package me.droreo002.cslimit.commands;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.objects.console.CCheckCommand;
import me.droreo002.cslimit.commands.objects.console.CHelpCommand;
import me.droreo002.cslimit.commands.objects.console.CReloadCommand;
import me.droreo002.cslimit.commands.objects.console.CResetCommand;
import me.droreo002.cslimit.commands.objects.player.*;
import me.droreo002.cslimit.manager.Debug;

import java.util.ArrayList;
import java.util.List;

public final class CommandManager {

    private static final List<CSLCommand> COMMANDS = new ArrayList<>();

    public static List<CSLCommand> getCommands() {
        return COMMANDS;
    }

    public static void registerCommand(CSLCommand command) {
        if (isContains(command)) return;
        COMMANDS.add(command);
    }

    public static void unregisterCommand(CSLCommand command) {
        if (!isContains(command)) return;
        COMMANDS.remove(command);
    }

    public static boolean isContains(CSLCommand command) {
        for (CSLCommand c : COMMANDS) {
            if (c.getCommandKey().equalsIgnoreCase(command.getCommandKey())) return true;
        }
        return false;
    }

    public static void init() {
        /*
        Register commands (Player Only)
         */
        Debug.log(" &fRegistering player commands", false);
        new HelpCommand();
        new CheckCommand();
        new ReloadCommand();
        new ResetCommand();
        new StatusCommand();
        /*
        Register commands (Console Only)
         */
        Debug.log(" &fRegistering console commands", false);
        new CCheckCommand();
        new CHelpCommand();
        new CReloadCommand();
        new CResetCommand();
    }
}

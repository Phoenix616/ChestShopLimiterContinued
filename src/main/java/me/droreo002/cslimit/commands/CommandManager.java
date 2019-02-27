package me.droreo002.cslimit.commands;

import me.droreo002.cslimit.commands.objects.console.CCheckCommand;
import me.droreo002.cslimit.commands.objects.console.CHelpCommand;
import me.droreo002.cslimit.commands.objects.console.CReloadCommand;
import me.droreo002.cslimit.commands.objects.console.CResetCommand;
import me.droreo002.cslimit.commands.objects.player.*;
import me.droreo002.cslimit.manager.logger.Debug;

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
        Debug.info(" &fRegistering player commands", false, true);
        new HelpCommand();
        new CheckCommand();
        new ReloadCommand();
        new ResetCommand();
        new StatusCommand();
        /*
        Register commands (Console Only)
         */
        Debug.info(" &fRegistering console commands", false, true);
        new CCheckCommand();
        new CHelpCommand();
        new CReloadCommand();
        new CResetCommand();
    }
}

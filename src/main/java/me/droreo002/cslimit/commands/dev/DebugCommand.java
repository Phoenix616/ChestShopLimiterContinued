package me.droreo002.cslimit.commands.dev;

import co.aikar.taskchain.TaskChain;
import com.google.common.base.Stopwatch;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import me.droreo002.oreocore.enums.XMaterial;
import me.droreo002.oreocore.utils.item.CustomSkull;
import me.droreo002.oreocore.utils.misc.ThreadingUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * INFO : REMOVE WHEN DONE OR UNREGISTER!
 */
public class DebugCommand extends CommandArg {

    private final ChestShopLimiter plugin;

    public DebugCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("debug", parent);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        Player player = (Player) commandSender;
        success(player);
        Stopwatch timer = Stopwatch.createStarted();
        TaskChain<ItemStack> chain = ThreadingUtils.makeChain();
        chain.asyncFirst(() -> CustomSkull.getHead(player.getUniqueId())).abortIfNull().syncLast((item) -> {
            player.sendMessage("Sending item...");
            player.getInventory().addItem(item);
        }).execute();
        player.sendMessage("Took " + timer.stop());
    }
}

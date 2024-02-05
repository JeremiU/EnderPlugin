package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * Makes a countdown for an amount of seconds.
 *
 * @author Jeremi
 * @version 25.7.5
 * @since 1.0.7
 */
public class CountCommand implements EndExecutor {

    private int currentTicks, maxTicks;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return msg(sender, this.getSyntax(label));

        try {
            currentTicks = Integer.parseInt(args[0]);
            maxTicks = currentTicks;
        } catch (NumberFormatException e) {
            return msg(sender, serverLang().getNumFormatMsg());
        }

        if (maxTicks < 1) return msg(sender, serverLang().getErrorMsg() + "That's not a high enough number!");
        if (currentTicks > 300) return msg(sender, serverLang().getErrorMsg() + "That's too high of a number!");

        Collection<? extends Player> players = (sender instanceof Entity || sender instanceof BlockCommandSender)
                ? ((sender instanceof Entity) ? ((Entity) sender).getWorld().getPlayers() : ((BlockCommandSender) sender).getBlock().getWorld().getPlayers()) : Bukkit.getOnlinePlayers();

        EnderPlugin.scheduler().runRepeatingTask(() -> {
            for (Player p : players) {
                if (currentTicks > 0) p.sendMessage(getColor(currentTicks, maxTicks) + "" + currentTicks + " second" + (currentTicks == 1 ? "" : "s") + " left.");
                else p.sendMessage("GO!");
            }
            set();
        }, "COUNT_COMMAND", 0, 1, maxTicks + 1, serverLang().getPlugMsg());
        return true;
    }

    private void set() {
        currentTicks--;
    }

    private ChatColor getColor(int current, int max) {
        double ratio = ((double) current) / max;
        if (ratio > 0.8) return ChatColor.AQUA;
        if (ratio > 0.6) return ChatColor.GREEN;
        if (ratio > 0.4) return ChatColor.YELLOW;
        if (ratio > 0.2) return ChatColor.RED;
        if (ratio > 0) return ChatColor.DARK_RED;
        return ChatColor.WHITE;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return args.length == 1 ? tabOption(args[0], "5", "10", "15", "25", "30", "45", "60", "90", "120", "180") : null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr("seconds", true);
    }

    public List<String> commandNames() {
        return List.of("countdown", "endercount");
    }
}
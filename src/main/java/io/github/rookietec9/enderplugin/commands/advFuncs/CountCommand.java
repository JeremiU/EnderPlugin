package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Makes a countdown for an amount of seconds.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 1.0.7
 */
public class CountCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length != 1) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }

        int i;
        try {
            i = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(l.getNumFormatMsg());
            return true;
        }

        if (i < 1) {
            sender.sendMessage(l.getErrorMsg() + "That's not a high enough number! ");
            return true;
        }
        if (i > 300) {
            sender.sendMessage(l.getErrorMsg() + "That's to high of a number! ");
            return true;
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(EnderPlugin.getInstance(), () -> {
            Collection<? extends Player> players = null;

            if (sender instanceof Entity || sender instanceof BlockCommandSender) {
                if (sender instanceof Entity) players = ((Entity) sender).getWorld().getPlayers();
                if (sender instanceof BlockCommandSender) players = ((BlockCommandSender) sender).getBlock().getWorld().getPlayers();
            } else players = Bukkit.getOnlinePlayers();

            for (int j = i; j > -1; j--) {
                if (j > 60) {
                    int min = 1, sec = 0;
                    for (Player player : players) {
                        player.sendMessage("[" + getColor(i, j) + min + ChatColor.WHITE + ":" + getColor(i, j) + sec + "]" + " till start.");
                        player.sendTitle("[" + getColor(i, j) + min + ChatColor.WHITE + ":" + getColor(i, j) + sec + "]" + " till start.", "Get Ready");
                    }
                    doTick(players, i, j);
                } else if (j > 1) {
                    doTick(players, i, j);
                } else if (j == 1) {
                    for (Player player : players) {
                        player.sendMessage("[" + ChatColor.DARK_RED + j + "]" + " second till start.");
                        player.sendTitle("[" + ChatColor.DARK_RED + j + "]" + " second till start.", "Get Ready!");
                    }
                } else {
                    for (Player player : players) {
                        player.sendMessage("Countdown finished");
                        player.sendTitle("GOOOOO!", "Countdown finished.");
                    }
                }
            }
        }, 0L, 20L);
        return true;
    }

    private void doTick(Collection<? extends Player> players, int i, int j) {
        for (Player player : players) {

            player.sendMessage("[" + getColor(i, j) + j + "]" + " seconds till start.");
            player.sendTitle("[" + j + "]" + " seconds till start.", "Get Ready");
        }
    }

    public ChatColor getColor(int i, int j) {
        ChatColor color;
        if (j / i > 0.8) color = ChatColor.AQUA;
        else if (j / i > 0.6 && (j / i < 0.8)) color = ChatColor.GREEN;
        else if (j / i > 0.4 && (j / i < 0.6)) color = ChatColor.YELLOW;
        else if (j / i > 0.2 && (j / i < 0.4)) color = ChatColor.RED;
        else if (j / i > 0 && (j / i < 0.2)) color = ChatColor.DARK_RED;
        else color = ChatColor.WHITE;

        return color;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> l = new ArrayList<>();
            for (int i = 5; i <= 60; i++) {
                l.add("" + i);
            }
            return l;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() +
                        "seconds" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "countDown";
    }
}
package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.Warp;
import io.github.rookietec9.enderplugin.EnderPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Warps a player to one of the warps from the server.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 5.7.0
 */
public class WarpCommand implements EndExecutor {

    private final File[] files = new File(EnderPlugin.getInstance().getDataFolder().getPath() + File.separator + "Warp").listFiles();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        StringBuilder sb = new StringBuilder();
        sb.append(l.getDarkColor()).append("Warps").append(ChatColor.WHITE).append(": ");

        if (files != null)
            for (File f : files) {
                sb.append(l.getLightColor()).append(f.getName().replace(".yml", "")).append(ChatColor.WHITE).append(" , ");
            }

        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(sb.toString().substring(0, sb.toString().length() - " , ".length()));
                return true;
            }
            if (args.length == 1) {
                Player p = (Player) sender;
                if (files == null || files.length == 0) {
                    sender.sendMessage(l.getErrorMsg() + "No warps!");
                    return true;
                }
                if (new File(EnderPlugin.getInstance().getDataFolder().getPath() + "\\Warp\\" + args[0] + ".yml").exists()) {
                    Warp w = new Warp(args[0]);
                    if (w.getWorld() == null) {
                        sender.sendMessage(l.getErrorMsg() + "That warp is invalid!");
                        return true;
                    }
                    p.teleport(new Location(w.getWorld(), w.getX(), w.getY(), w.getZ(), w.getYaw(), w.getPitch()));
                    sender.sendMessage(l.getPlugMsg() + "Warped you to " + l.getDarkColor() + w.getName());
                    return true;
                } else {
                    sender.sendMessage(l.getErrorMsg() + "That warp doesn't exist!");
                    return true;
                }
            }
        } else {
            sender.sendMessage(sb.toString().substring(0, sb.toString().length() - " , ".length()));
            return true;
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> l = new ArrayList<>();
            if (files != null)
                for (File f : files) {
                    l.add(f.getName().replace(".yml", ""));
                }
            return l;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "warp";
    }
}
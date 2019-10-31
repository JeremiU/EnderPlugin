package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.dataFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Get's a player's UUID.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 1.9.6
 */
public class UUIDCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length != 1) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }
        if (Bukkit.getOfflinePlayer(args[0]) == null) {
            sender.sendMessage(l.getErrorMsg() + "That player is not registered!");
            return true;
        }
        sender.sendMessage(ChatColor.WHITE + Bukkit.getOfflinePlayer(args[0]).getName() + "'s uuid: ");
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            if (op.getName().equalsIgnoreCase(args[0])) {
                if (!op.hasPlayedBefore()) {
                    return true;
                }
                if (op.isBanned()) sender.sendMessage(l.getPlugMsg() + op.getName() + " has been" + l.getLightColor() + " banned" + ChatColor.RESET + "!");
                if (new User(op).wasOnline()) sender.sendMessage(l.getLightColor() + op.getUniqueId().toString());
                if (!new User(op).wasOnline())sender.sendMessage(ChatColor.RED + op.getUniqueId().toString());
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> tab = new ArrayList<>();
            for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                if (p.hasPlayedBefore()) {
                    tab.add(p.getName());
                }
            }
            return tab;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "uuid";
    }
}
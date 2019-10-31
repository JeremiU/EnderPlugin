package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.setFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Mutes a player, preventing them from speaking.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 4.8.7
 */
public class MuteCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length < 1) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(l.getOfflineMsg());
            return true;
        }

        User u = new User(Bukkit.getPlayer(args[0]));

        boolean setMute = false;
        if (args.length == 1) setMute = !u.getGod();
        if (args.length >= 2) {
            if (!args[1].equalsIgnoreCase("off") && !args[1].equalsIgnoreCase("on") &&
                    !args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
                sender.sendMessage(this.getSyntax(command, l));
                return true;
            }
            if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true")) setMute = true;
        }
        u.setMute(setMute);
        if (args.length > 2) {
            if (sender instanceof Player)
                u.getBase().sendMessage(l.getPlugMsg() + "You have been muted by " + new User((Player) sender).getTabName() + " for " + ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, " ", 1, args.length)));
            else
                u.getBase().sendMessage(l.getPlugMsg() + "You have been muted for " + ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, " ", 1, args.length)));
        } else {
            if (sender instanceof Player)
                u.getBase().sendMessage(l.getPlugMsg() + "You have been muted by " + new User((Player) sender).getTabName());
            else u.getBase().sendMessage(l.getPlugMsg() + "You have been muted.");
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor()
                        + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " "
                        + Utils.Reference.OPEN_OPTIONAL_CHAR + l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR + " "
                        + Utils.Reference.OPEN_OPTIONAL_CHAR + l.getLightColor() + Utils.Reference.MESSAGE + l.getCmdExColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR
        };
    }

    public String commandName() {
        return "mute";
    }
}
package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Casts a message to a player. Similiar to the <b>TELL</b> command, but uses '&' character for colors and has an option to not use the plugin prefix.
 *
 * @author Jeremi
 * @version 11.6.0
 * @since 0.3.5
 */
public class AnonCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length < 3) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }
        Player target = sender.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(l.getOfflineMsg());
            return true;
        }
        if (args[1].equalsIgnoreCase("log")) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', l.getPlugMsg() + StringUtils.join(args, ' ', 2, args.length)));
            if (target != sender) {
                sender.sendMessage(l.getPlugMsg() + "Sent a logged message to " + new User(target).getTabName() + l.getTxtColor() + ".");
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("nolog")) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 2, args.length)));
            if (target != sender) {
                sender.sendMessage(l.getPlugMsg() + "Sent a message to " + new User(target).getTabName() + l.getTxtColor() + ".");
            }
            return true;
        }
        sender.sendMessage(getSyntax(command, l));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 2) {
            l.add("nolog");
            l.add("log");
            return l;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " "
                        + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.MESSAGE + l.getCmdExColor() +
                        Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "anon";
    }
}
package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Tell a player that you hugged them.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 0.0.4
 */

public class HugCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length != 1) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }
        Player target = sender.getServer().getPlayer(args[0]);
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        if (target == null) {
            sender.sendMessage(l.getOfflineMsg());
            return true;
        }
        target.sendMessage(l.getPlugMsg() + new User((Player) sender).getTabName() + l.getTxtColor() + " Hugged you.");
        sender.sendMessage(l.getPlugMsg() + new User(Bukkit.getPlayer(args[0])).getTabName() + l.getTxtColor() + " was successfully hugged. :D");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR +
                        l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "hug";
    }
}
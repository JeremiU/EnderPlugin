package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

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
 * @author Jeremi
 * @version 13.4.4
 * @since 13.3.5
 */
public class ExtinguishCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));

        if (args.length != 1 && args.length != 0) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }

        Player player;

        if (args.length == 1) {
            player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(l.getOfflineMsg());
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(l.getOnlyUserMsg());
                return true;
            }
            player = (Player) sender;
        }
        player.setFireTicks(-20);
        if (sender != player && sender instanceof Player) player.sendMessage(l.getPlugMsg() + "Extinguished by " + new User(((Player) sender)).getTabName() + ".");
        if (sender != player && !(sender instanceof Player)) player.sendMessage(l.getPlugMsg() + "Extinguished by the console / a command block.");
        if (sender == player) sender.sendMessage(l.getPlugMsg() + "Extinguished.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_OPTIONAL_CHAR + l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR
        };
    }

    public String commandName() {
        return "ext";
    }
}
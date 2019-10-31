package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.editFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Clear the given player's inventory.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 0.5.2
 */
public class ClearCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if ((!(sender instanceof Player)) && (args.length == 0)) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }

        if (args.length == 1 || args.length == 0) {
            Player player;
            if (args.length == 0) player = (Player) sender;
            else player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(l.getOfflineMsg());
                return true;
            }
            int i = new User(player).clearCount();
            if (i == 0) {
                sender.sendMessage(l.getPlugMsg() + "Clear nothing.");
                return true;
            }
            if (i == 1)
                sender.sendMessage(l.getPlugMsg() + "Cleared inventory of " + new User(player).getTabName() + l.getTxtColor() + ", removing " + l.getDarkColor() + i + l.getTxtColor() + " item.");
            else
                sender.sendMessage(l.getPlugMsg() + "Cleared inventory of " + new User(player).getTabName() + l.getTxtColor() + ", removing " + l.getDarkColor() + i + l.getTxtColor() + " items.");
            return true;
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "clear";
    }
}
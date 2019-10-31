package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Kill a player.
 *
 * @author Jeremi
 * @version 13.4.5
 * @since 0.0.4
 */
public class KillCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length != 1 && args.length != 0) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }
        Player target;
        if (args.length == 1) target = sender.getServer().getPlayer(args[0]);
        else {
            if (sender instanceof Player) target = (Player) sender; else {
                sender.sendMessage(l.getOnlyUserMsg());
                return true;
            }
        }

        if (target == null) {
            sender.sendMessage(l.getOfflineMsg());
            return true;
        }
        target.setHealth(0.0D);
        sender.sendMessage(l.getPlugMsg() + new User(target).getTabName() + l.getTxtColor() + " was successfully murdered.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() +
                        Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "kill";
    }
}
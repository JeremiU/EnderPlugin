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

import java.util.ArrayList;
import java.util.List;

/**
 * Prevents damage to a player if GOD mode is on.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 1.2.7
 */
public class GodCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));

        if (args.length <= 2) {
            if (args.length != 2) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(l.getOnlyUserMsg());
                    return true;
                }
            }
            Player player;
            if (args.length == 2) player = Bukkit.getPlayer(args[1]);
            else player = (Player) sender;

            if (player == null) {
                sender.sendMessage(l.getOfflineMsg());
                return true;
            }

            User user = new User(player);
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("on")) user.setGod(true);
                if (args[0].equalsIgnoreCase("off")) {
                    user.setGod(false);
                } else {
                    sender.sendMessage(l.getErrorMsg() + "that's not a valid switch!");
                    return true;
                }
                sender.sendMessage(l.getPlugMsg() + "Set " + user.getTabName() + "'s god mode to " + user.getGod());
            } else {
                user.setGod(!user.getGod());
                sender.sendMessage(l.getPlugMsg() + "Set " + user.getTabName() + "'s god mode to " + user.getGod());
            }
        } else sender.sendMessage(this.getSyntax(command, l));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("on");
            l.add("off");
            return l;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_OPTIONAL_CHAR + l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR + " " + Utils.Reference.OPEN_OPTIONAL_CHAR +
                        l.getLightColor() + Utils.Reference.USER + l.getTxtColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR
        };
    }

    public String commandName() {
        return "god";
    }
}
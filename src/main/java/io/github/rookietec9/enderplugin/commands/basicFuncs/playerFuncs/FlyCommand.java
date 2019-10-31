package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to decide whether or not to allow flight.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 0.9.6
 */
public class FlyCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (sender instanceof BlockCommandSender) {
            sender.sendMessage(l.getPlugMsg() + "Up and Up and away!");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        if (args.length >= 2) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            if (player.getAllowFlight()) {
                set(l, sender, player, false);
                return true;
            }
            if (!player.getAllowFlight()) {
                set(l, sender, player, true);
                return true;
            }
        }
        if (args.length == 1) {
            if ((args[0].equalsIgnoreCase("ON")) || (args[0].equalsIgnoreCase("TRUE"))) {
                set(l, sender, player, true);
                return true;
            }
            if ((args[0].equalsIgnoreCase("OFF")) || (args[0].equalsIgnoreCase("FALSE"))) {
                set(l, sender, player, false);
                return true;
            }
            return true;
        }
        return true;
    }

    public void set(Lang l, CommandSender sender, Player player, boolean flying) {
        player.setAllowFlight(flying);
        player.setFlying(flying);
        if (flying)
            sender.sendMessage(l.getPlugMsg() + "Turned " + l.getDarkColor() + "on" + l.getTxtColor() + " flying mode for " + new User(player).getTabName());
        else
            sender.sendMessage(l.getPlugMsg() + "Turned " + l.getDarkColor() + "off" + l.getTxtColor() + " flying mode for " + new User(player).getTabName());
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        l.add("off");
        l.add("on");
        if (args.length == 1) return l;
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " +
                        Utils.Reference.OPEN_OPTIONAL_CHAR + l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR
        };
    }

    public String commandName()  {
        return "fly";
    }
}
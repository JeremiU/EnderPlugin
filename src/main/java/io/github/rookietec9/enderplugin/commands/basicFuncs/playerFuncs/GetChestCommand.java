package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Gets the items from a chest.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 10.3.7
 */
public class GetChestCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 4) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }
        int x, y, z = 0;
        boolean message = false;
        try {
            x = Integer.valueOf(args[0]);
            y = Integer.valueOf(args[1]);
            z = Integer.valueOf(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(l.getNumFormatMsg());
            return true;
        }
        if (!args[3].equalsIgnoreCase("on") && args[3].equalsIgnoreCase("off")) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }

        if (args[3].equalsIgnoreCase("on")) message = true;
        new User(player).fromChest(player.getWorld(), x, y, z);

        if (message)
            sender.sendMessage(l.getTxtColor() + "Gave you chest from " + l.getDarkColor() + x + " " + y + " " + z);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) list.add(0, "x");
        if (args.length == 2) list.add(0, "y");
        if (args.length == 3) list.add(0, "z");

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) list.add(0, "" + player.getEyeLocation().getBlockX());
            if (args.length == 2) list.add(0, "" + player.getEyeLocation().getBlockY());
            if (args.length == 3) list.add(0, "" + player.getEyeLocation().getBlockZ());
        }
        if (args.length == 4) {
            list.add(0, "on");
            list.add(1, "off");
        }
        return list;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "x" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "y" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "z" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "getChest";
    }
}
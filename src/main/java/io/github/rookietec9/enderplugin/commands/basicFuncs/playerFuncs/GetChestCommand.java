package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.MODE;

/**
 * Gets the items from a chest.
 *
 * @author Jeremi
 * @version 25.5.4
 * @since 10.3.7
 */
public class GetChestCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
        if (!Java.isInRange(args.length, 3, 4)) return msg(sender, getSyntax(label));

        Player player = (Player) sender;

        int x, y, z;
        try {
            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            z = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            return msg(sender, serverLang().getNumFormatMsg());
        }

        boolean log = !(args.length > 3 && Java.argWorks(args[3], "off", "false"));

        if (args.length > 3 && !Java.argWorks(args[3], "on", "off", "log", "no-log")) return msg(sender, getSyntax(label));
        if (!DataPlayer.get(player).fromChest(player.getWorld(), x, y, z)) return true;

        return (!log) || msg(sender, serverLang().getTxtColor() + "Gave you chest from " + serverLang().getDarkColor() + x + " " + y + " " + z);
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) return tabOption(args[0], "" + player.getEyeLocation().getBlockX());
            if (args.length == 2) return tabOption(args[1], "" + player.getEyeLocation().getBlockY());
            if (args.length == 3) return tabOption(args[2],"" + player.getEyeLocation().getBlockZ());
        }
        return args.length == 4 ? tabOption(args[3],"log","no-log") : null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr("x", true) + " " + helpBr("y", true) + " " + helpBr("z", true) + " " + helpBr(MODE,false);
    }

    public List<String> commandNames() {
        return List.of("getChest");
    }
}
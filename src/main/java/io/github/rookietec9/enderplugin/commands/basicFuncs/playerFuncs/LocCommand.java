package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

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
 * Get the location of either yourself or another player.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 4.1.4
 */
public class LocCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if ((!(sender instanceof Player)) && (args.length == 0)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        if (args.length <= 1) {
            Player p = null;
            if (args.length == 0) p = (Player) sender;
            if (args.length == 1) p = Bukkit.getPlayer(args[0]);

            if (p == null) {
                sender.sendMessage(l.getOfflineMsg());
                return true;
            }

            sender.sendMessage("Location of " + new User(p).getTabName());
            sender.sendMessage(l.getDarkColor() + "WORLD: " + l.getLightColor() + p.getWorld().getName());
            sender.sendMessage(l.getDarkColor() + "X: " + l.getLightColor() + p.getLocation().getBlockX());
            sender.sendMessage(l.getDarkColor() + "Y: " + l.getLightColor() + p.getLocation().getBlockY());
            sender.sendMessage(l.getDarkColor() + "Z: " + l.getLightColor() + p.getLocation().getBlockZ());
            sender.sendMessage(l.getDarkColor() + "YAW: " + l.getLightColor() + p.getLocation().getYaw());
            sender.sendMessage(l.getDarkColor() + "PITCH: " + l.getLightColor() + p.getLocation().getPitch());
            if (sender instanceof Player && ((Player) sender).getWorld() == p.getWorld() && sender != p)
                sender.sendMessage(l.getDarkColor() + "DISTANCE: " + l.getLightColor() + p.getLocation().distance(((Player) sender).getLocation()));
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
        return "loc";
    }
}
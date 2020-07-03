package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.USER;

/**
 * Get the location of either yourself or another player.
 *
 * @author Jeremi
 * @version 22.8.0
 * @since 4.1.4
 */
public class LocCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) && (args.length == 0)) return msg(sender, getSyntax(label));
        if (args.length > 1) return msg(sender, getSyntax(label));

        Player target = (args.length == 0) ? (Player) sender : Bukkit.getPlayer(args[0]);
        if (target == null) return msg(sender, serverLang().getOfflineMsg());

        sender.sendMessage("Location of " + DataPlayer.getUser(target).getTabName());
        sender.sendMessage(serverLang().getDarkColor() + "WORLD: " + serverLang().getLightColor() + target.getWorld().getName());
        sender.sendMessage(serverLang().getDarkColor() + "X: " + serverLang().getLightColor() + target.getLocation().getBlockX());
        sender.sendMessage(serverLang().getDarkColor() + "Y: " + serverLang().getLightColor() + target.getLocation().getBlockY());
        sender.sendMessage(serverLang().getDarkColor() + "Z: " + serverLang().getLightColor() + target.getLocation().getBlockZ());
        sender.sendMessage(serverLang().getDarkColor() + "YAW: " + serverLang().getLightColor() + target.getLocation().getYaw());
        sender.sendMessage(serverLang().getDarkColor() + "PITCH: " + serverLang().getLightColor() + target.getLocation().getPitch());

        return !(sender instanceof Player && ((Player) sender).getWorld() == target.getWorld() && sender != target) || msg(sender, serverLang().getDarkColor() + "DISTANCE: " + serverLang().getLightColor() + target.getLocation().distance(((Player) sender).getLocation()));
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(USER, false);
    }

    public List<String> commandNames() {
        return List.of("loc");
    }
}
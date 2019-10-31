package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Fakes a player message using a non-player entity.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 0.6.8
 */
public class FakeCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String String, String[] args) {
        if (args.length < 1) {
            return false;
        }
        Player target = sender.getServer().getPlayer(args[0]);
        String message = StringUtils.join(args, ' ', 1, args.length);
        Bukkit.broadcastMessage(new User(target).getTabName() + " | " + ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', message));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "fake";
    }
}
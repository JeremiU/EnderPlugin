package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
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
 * Kick a player for a reason or no reason at all.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 1.2.0
 */
public class KickCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length == 0) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }
        Player player = Bukkit.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(l.getOfflineMsg());
            return true;
        }
        if ((args.length == 1) && (sender instanceof Player)) {
            player.kickPlayer(new User((Player) sender).getTabName() + l.getTxtColor() + " had nothing else to do.");
            return true;
        }
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 1, args.length)));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " +
                        Utils.Reference.OPEN_OPTIONAL_CHAR + l.getLightColor() + "reason" + l.getCmdExColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR
        };
    }

    public String commandName() {
        return "kick";
    }
}
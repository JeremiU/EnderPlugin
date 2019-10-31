package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Casts a message to the server. Similiar to the <b>TELLRAW</b> command, but uses '&' character for colors.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 1.2.0
 */
public class AnonCastCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length < 1) {
            sender.sendMessage(getSyntax(command,l));
            return true;
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 0, args.length)));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[] {
                l.getSyntaxMsg() + l.getCmdExColor() +  "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.MESSAGE  + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "anoncast";
    }
}
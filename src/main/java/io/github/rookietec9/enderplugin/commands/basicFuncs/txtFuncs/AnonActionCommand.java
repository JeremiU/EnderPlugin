package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import io.github.rookietec9.enderplugin.API.ActionBar;
import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Casts a message to a player. Similiar to the <b>ENDERANON</b> command, but sends the message to the acion bar.
 *
 * @author Jeremi
 * @version 13.4.4
 * @see ActionBar
 * @since 3.7.5
 */
public class AnonActionCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length < 2) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }

        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(l.getOfflineMsg());
            return true;
        }

        Player p = Bukkit.getPlayer(args[0]);

        ActionBar.send(ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, " ", 1, args.length)), p);
        if (p != sender) {
            sender.sendMessage(l.getPlugMsg() + "Sent actionbar message to " + new User(p).getTabName() + ".");
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " + Utils.Reference.OPEN_MANDATORY_CHAR
                        + l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.MESSAGE +
                        l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "action";
    }
}
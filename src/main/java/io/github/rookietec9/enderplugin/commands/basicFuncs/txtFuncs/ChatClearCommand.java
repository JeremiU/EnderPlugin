package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Clear the chat by sending every player a ton of empty messages.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 1.1.3
 */
public class ChatClearCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        String[] toClr = new String[125];
        for (int i = 0; i < toClr.length; i++)
            toClr[i] = " ";
        if (args.length == 0) {
            for (Player target : Bukkit.getOnlinePlayers())
                target.sendMessage(toClr);
            Bukkit.broadcastMessage(l.getPlugMsg() + "Cleared chat.");
            return true;
        }
        if (args.length == 1) {
            Player player = Bukkit.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(l.getOfflineMsg());
                return true;
            }
            player.sendMessage(toClr);
            sender.sendMessage("Cleared " + new User(player).getTabName() + "'s chat");
            return true;
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() + Utils.Reference.OPEN_OPTIONAL_CHAR
                        + l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR
        };
    }

    public String commandName() {
        return "chatclear";
    }
}
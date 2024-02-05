package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.USER;

/**
 * Clear the chat by sending every player a ton of empty messages.
 *
 * @author Jeremi
 * @version 25.2.0
 * @since 1.1.3
 *
 */
public class ChatClearCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            for (Player target : Bukkit.getOnlinePlayers()) target.sendMessage(StringUtils.repeat(" \n", 100));
            return msg(sender, serverLang().getPlugMsg() + "Cleared chat.");
        }
        if (args.length == 1) {
            Player player = Bukkit.getServer().getPlayer(args[0]);
            if (player == null) return msg(sender, serverLang().getOfflineMsg());
            player.sendMessage(StringUtils.repeat(" \n", 100));
            return msg(sender, "Cleared " + DataPlayer.getUser(player).getTabName() + "'s chat");
        }
        return msg(sender, getSyntax(label));
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(USER, false);
    }

    public List<String> commandNames() {
        return List.of("chatclear");
    }
}
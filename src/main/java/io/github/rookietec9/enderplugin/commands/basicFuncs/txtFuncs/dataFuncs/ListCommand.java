package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.dataFuncs;

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
 * Lists all the players online.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 1.0.9
 */
public class ListCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if ((!(sender instanceof Player)) && (Bukkit.getOnlinePlayers().size() == 0)) {
            sender.sendMessage(l.getPlugMsg() + "There are  " + l.getLightColor() + Bukkit.getServer().getOnlinePlayers().size()
                    + l.getTxtColor() + " out of " + l.getDarkColor() + Bukkit.getMaxPlayers() + l.getTxtColor() + " maximum players");
            return true;
        }
        StringBuilder str = new StringBuilder();
        sender.sendMessage(l.getPlugMsg() + "There are  " + l.getLightColor() + Bukkit.getServer().getOnlinePlayers().size()
                + l.getTxtColor() + " out of " + l.getDarkColor() + Bukkit.getMaxPlayers() + l.getTxtColor() + " maximum players");
        for (Player s : Bukkit.getServer().getOnlinePlayers()) {
            str.append(new User(s).getTabName());
            str.append(" , ");
        }
        if (Bukkit.getServer().getOnlinePlayers().size() == 1) {
            sender.sendMessage(l.getPlugMsg() + "You are the only one on the server.");
            return true;
        }
        sender.sendMessage(l.getDarkColor() + "Online Players: " + l.getLightColor() + str.toString().substring(0, str.length() - " , ".length()));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "list";
    }
}
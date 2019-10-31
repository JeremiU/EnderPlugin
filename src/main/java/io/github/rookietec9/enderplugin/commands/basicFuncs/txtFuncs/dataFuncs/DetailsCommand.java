package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.dataFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Identifies the Server's IP and Port.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 2.0.0
 */
public class DetailsCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        sender.sendMessage(l.getPlugMsg() + "This server is being run on: " + l.getDarkColor() + new Games().getUniversalIP() + ChatColor.WHITE + ":" + l.getLightColor() + Bukkit.getServer().getPort());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "details";
    }
}
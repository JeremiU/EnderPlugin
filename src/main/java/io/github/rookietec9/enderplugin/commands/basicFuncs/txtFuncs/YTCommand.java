package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Links to our now outdated youtube channel. Quite a shame really.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 0.0.5
 */
public class YTCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        sender.sendMessage(l.getLightColor() + "Look at our old YT channel:");
        sender.sendMessage(l.getDarkColor() + "http://bit.ly/1ruZdZs");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "yt";
    }
}
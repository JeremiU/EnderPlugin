package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Sends a Title and a Subtitle.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 6.4.5
 */
public class TitleCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length < 3) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(l.getOfflineMsg());
            return true;
        }
        Player p = Bukkit.getPlayer(args[0]);
        StringBuilder title = new StringBuilder();
        StringBuilder subtitle = new StringBuilder();
        boolean currentForm = false;

        for (int i = 1; i < args.length; i++) {
            if (args[i].contains(",[],")) {
                currentForm = true;
                subtitle.append(args[i].replace(",[],", ""));
            } else {
                if (!currentForm) title.append(" ").append(args[i]);
                if (currentForm) subtitle.append(" ").append(args[i]);
            }
        }
        p.sendTitle(ChatColor.translateAlternateColorCodes('&', title.toString()), ChatColor.translateAlternateColorCodes('&', subtitle.toString()));
        sender.sendMessage(l.getPlugMsg() + "Sent message.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " + Utils.Reference.OPEN_MANDATORY_CHAR +
                        l.getLightColor() + "Title" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " ,[], " + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "Subtitle" +
                        l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "sendtitle";
    }
}
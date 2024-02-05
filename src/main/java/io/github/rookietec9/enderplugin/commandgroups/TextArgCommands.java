package io.github.rookietec9.enderplugin.commandgroups;

import io.github.rookietec9.enderplugin.events.main.MainTalkEvent;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 21.4.5
 */
public class TextArgCommands implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        label = label != null ? label : command.getName();
        if (args.length < 2 && !label.equalsIgnoreCase("kick")) return msg(sender, getSyntax(label));

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) return msg(sender, serverLang().getOfflineMsg());
        switch (label) {
            case "anonaction", "action" -> DataPlayer.get(player).sendActionMsg(Minecraft.tacc(StringUtils.join(args, " ", 1, args.length)));
            case "anon" -> {
                boolean log = !args[1].equalsIgnoreCase("-nolog");
                if (!log && args.length == 2) return msg(sender, getSyntax(label));
                String toSend = Minecraft.tacc(StringUtils.join(args, ' ', (log) ? 1 : 2, args.length));
                player.sendMessage(toSend);
            }
            case "kick" -> player.kickPlayer((args.length == 1 && sender instanceof Player) ? DataPlayer.getUser((OfflinePlayer) sender).getTabName() + serverLang().getTxtColor() + " had nothing else to do." : Minecraft.tacc(StringUtils.join(args, ' ', 1, args.length)));
            case "fake" -> MainTalkEvent.chat(player, StringUtils.join(args, " ", 1, args.length));
            case "sudo" -> player.performCommand(StringUtils.join(args, ' ', 1, args.length));
            case "title" -> {
                String total = StringUtils.join(args, " ", 1, args.length);
                String title = total.contains(",[],") ? total.substring(0, total.indexOf(",[],")) : total;
                String subtitle = total.contains(",[],") ? total.substring(total.indexOf(",[],") + ",[],".length()) : "";

                player.sendTitle(Minecraft.tacc(title), Minecraft.tacc(subtitle));
            }
        }

        if (!player.equals(sender)) return msg(sender, serverLang().getPlugMsg() + switch (label) {
            case "anon", "action", "anonaction", "title" -> "sent a " + label + " to ";
            case "kick", "sudo" -> label + "ed ";
            default -> "faked a message from ";
        } + DataPlayer.getUser(player).getTabName() + ".");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (alias.equalsIgnoreCase("anon") && args.length == 2) return tabOption(args[1], "-nolog", "");
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + switch (label.toLowerCase()) {
            case "anonaction", "action", "fake", "kick" -> helpBr(USER, true) + " " + helpBr(MSG, !label.equalsIgnoreCase("kick"));
            case "title" -> helpBr(USER, true) + " " + helpBr("title", false) + " " + helpBr(",[],", false) + " " + helpBr("subtitle", false);
            default -> helpBr(USER, true) + " " + helpBr(MODE, false) + " " + helpBr(MSG, true);
        };
    }

    public List<String> commandNames() {
        return List.of("action","anon","fake","kick","sudo","title");
    }
}
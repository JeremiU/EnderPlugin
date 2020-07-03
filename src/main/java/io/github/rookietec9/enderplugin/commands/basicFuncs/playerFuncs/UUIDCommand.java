package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.USER;

/**
 * Get's a player's UUID.
 *
 * @author Jeremi
 * @version 22.8.0
 * @since 1.9.6
 */
public class UUIDCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1) return msg(sender, getSyntax(label));
        if (Bukkit.getOfflinePlayer(args[0]) == null) return msg(sender, serverLang().getErrorMsg() + "That player is not registered!");

        sender.sendMessage(ChatColor.WHITE + Bukkit.getOfflinePlayer(args[0]).getName() + "'s uuid: ");
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            if (op.getName().equalsIgnoreCase(args[0])) {
                if (!op.hasPlayedBefore()) return msg(sender, serverLang().getErrorMsg() + "That player has no joined before!");
                if (op.isBanned()) return msg(sender, serverLang().getPlugMsg() + op.getName() + " has been" + serverLang().getLightColor() + " banned" + serverLang().getTxtColor() + "!");
                if (DataPlayer.getUser(op).wasOnline()) return msg(sender, serverLang().getLightColor() + op.getUniqueId().toString());
                if (!DataPlayer.getUser(op).wasOnline()) return msg(sender, ChatColor.RED + op.getUniqueId().toString());
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> tab = new ArrayList<>();
            for (OfflinePlayer p : Bukkit.getOfflinePlayers()) if (p.hasPlayedBefore() && !tab.contains(p.getName())) tab.add(p.getName());
            return tabOption(args[0], tab);
        }
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(USER, true);
    }

    public List<String> commandNames() {
        return List.of("uuid");
    }
}
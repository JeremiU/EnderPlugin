package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.configs.associates.Warp;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * Warps a player to one of the warps from the server.
 *
 * @author Jeremi
 * @version 22.8.0
 * @since 5.7.0
 *
 */
public class WarpCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append(serverLang().getDarkColor()).append("Warps").append(ChatColor.WHITE).append(": ");

        for (int i = 0; i < Warp.getWarps().size(); i++) {
            sb.append(serverLang().getLightColor()).append(Warp.getWarps().get(i));
            sb.append((i + 1 < Warp.getWarps().size()) ? ChatColor.WHITE + ", " : "");
        }

        if (sender instanceof Player) {
            if (args.length == 0) return msg(sender, sb.toString());
            if (args.length == 1) {
                Player p = (Player) sender;
                if (Warp.getWarps().size() == 0) return msg(sender, serverLang().getErrorMsg() + "No warps!");
                if (Warp.exists(args[0])) {
                    Warp w = new Warp(args[0]);
                    if (w.getWorld() == null) return msg(sender, serverLang().getErrorMsg() + "That warp is invalid!");
                    p.teleport(w.location(), PlayerTeleportEvent.TeleportCause.COMMAND);
                    return msg(sender, serverLang().getPlugMsg() + "Warped you to " + serverLang().getDarkColor() + w.getName());
                }
                return msg(sender, serverLang().getErrorMsg() + "That warp doesn't exist!");
            }
        }
        return msg(sender, sb.toString());
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) tabOption(args[0], Warp.getWarps());
        return null;
    }

    public String getSyntax(String label) {
        return null;
    }

    public List<String> commandNames() {
        return List.of("warp");
    }
}
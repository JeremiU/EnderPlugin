package io.github.rookietec9.enderplugin.commandgroups;

import io.github.rookietec9.enderplugin.configs.associates.Warp;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 25.7.2
 * @since 25.7.0
 */
public class WarpCommands implements EndExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        label = label != null ? label : command.getName();
        Player player = (sender instanceof Player) ? (Player) sender : null;

        switch (label.toLowerCase()) {
            case "warp", "enderwarp" -> {
                StringBuilder sb = new StringBuilder();
                sb.append(serverLang().getDarkColor()).append("Warps").append(ChatColor.WHITE).append(": ");

                for (int i = 0; i < Warp.getWarps().size(); i++) {
                    sb.append(serverLang().getLightColor()).append(Warp.getWarps().get(i));
                    sb.append((i + 1 < Warp.getWarps().size()) ? ChatColor.WHITE + ", " : "");
                }

                if (sender instanceof Player && args.length == 1) {
                    if (Warp.exists(args[0].toLowerCase())) {
                        Warp w = new Warp(args[0].toLowerCase());
                        if (w.getWorld() == null) return msg(sender, serverLang().getErrorMsg() + "That warp is invalid!");
                        player.teleport(w.location(), PlayerTeleportEvent.TeleportCause.COMMAND);
                        return msg(sender, serverLang().getPlugMsg() + "Warped you to " + serverLang().getDarkColor() + w.getName());
                    } else return msg(sender, serverLang().getErrorMsg() + "That warp doesn't exist!");
                }
                return msg(sender, Warp.getWarps().size() == 0 ? serverLang().getErrorMsg() + "No warps!" : sb.toString());
            }
            case "warpcreate", "warpc", "enderwarpcreate" -> {
                if (!Java.isInRange(args.length, 1, 7)) return msg(sender, getSyntax(label));
                if (args.length < 5 && !(sender instanceof Player)) return msg(sender, serverLang().getErrorMsg() + "You must have at least 5 arguments if you are not a player!");
                Warp w = new Warp(args[0].toLowerCase());

                World world = args.length > 1 ? Bukkit.getWorld(args[1]) : player.getWorld();
                double x = args.length > 2 ? Double.parseDouble(args[2]) : player.getLocation().getX();
                double y = args.length > 3 ? Double.parseDouble(args[3]) : player.getLocation().getY();
                double z = args.length > 4 ? Double.parseDouble(args[4]) : player.getLocation().getZ();
                float yaw = args.length > 5 ? ((sender instanceof Player) ? player.getLocation().getYaw() : Float.parseFloat(args[5])) : 0;
                float pitch = args.length > 6 ? ((sender instanceof Player) ? player.getLocation().getPitch() : Float.parseFloat(args[6])) : 0;

                w.setWorld(world);
                w.setX(x);
                w.setY(y);
                w.setZ(z);
                w.setYaw(yaw);
                w.setPitch(pitch);

                return msg(sender, serverLang().getPlugMsg() + "Created warp " + args[0].toLowerCase());
            }
            case "warpd", "warpdelete", "enderwarpdelete" -> {
                if (args.length != 1) return msg(sender, getSyntax(label));
                if (Warp.exists(args[0].toLowerCase())) {
                    return msg(sender, Warp.delete(args[0].toLowerCase()) ? serverLang().getPlugMsg() + "Deleted warp " + args[0].toLowerCase() + "." : serverLang().getErrorMsg() + "Unable to delete warp!");
                } else return msg(sender, serverLang().getErrorMsg() + "That warp doesn't exist!");
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (Java.argWorks(alias, "warp", "enderwarp", "warpd", "warpdelete", "enderwarpdelete") && args.length == 1) return tabOption(args[0], Warp.getWarps());
        return null;
    }

    public String getSyntax(String label) {
        return switch (label.toLowerCase()) {
            case "warpcreate", "warpc", "enderwarpcreate" -> helpBr("name", true) + " " + helpBr("world", false) + " " + helpBr("x", false) + " " + helpBr("y", false) + " " + helpBr("z", false) + " " + helpBr("yaw", false) + " " + helpBr("pitch", false);
            default -> helpBr("world", true);
        };
    }

    public List<String> commandNames() {
        return List.of("warp", "warpc", "warpd");
    }
}

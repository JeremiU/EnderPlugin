package io.github.rookietec9.enderplugin.commandgroups;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.configs.associates.Hub;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.USER;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Jeremi
 * @version 21.9.7
 * @since 21.4.5
 */
public class TeleportCommands implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        label = label != null ? label : command.getName();

        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
        if (args.length > 1 && !label.equalsIgnoreCase("wtp")) return msg(sender, getSyntax(label));
        Player player = (Player) sender;

        switch (label.toLowerCase()) {
            case "hub","lobby" -> {
                if (args.length == 1 && (args[0].equalsIgnoreCase("set"))) {
                    new Hub().setLocation(player.getLocation());
                    return msg(sender, serverLang().getPlugMsg() + "Set hub location!");
                }
                if (args.length == 0) {
                    if (null == new Hub().getLoc()) return msg(sender, serverLang() + "The world is incorrect for the hub.");
                    DataPlayer.getUser(player).hub();
                    return msg(sender, serverLang().getPlugMsg() + "You were teleported to the hub.");
                }
                return msg(sender, getSyntax(label));
            }
            case "jump" -> {
                Location loc;
                try {
                    loc = player.getTargetBlock(Minecraft.HOLLOW_MATERIALS(), 300).getLocation();
                } catch (IllegalStateException ex) {
                    return msg(sender, serverLang().getErrorMsg() + "Nowhere2go");
                }
                loc.setPitch(player.getLocation().getPitch());
                loc.setYaw(player.getLocation().getYaw());
                player.teleport(loc, TeleportCause.COMMAND);
                return msg(sender, serverLang().getPlugMsg() + "Jumped.");
            }
            case "top" -> {
                int y = player.getLocation().getBlockY();

                for (int i = y; i < 256; i++) {
                    Location location = player.getLocation().clone();
                    location.setY(i);
                    if (player.getWorld().getBlockAt(location).getType() != Material.AIR) y = i + 1;
                }

                Location location = player.getLocation().clone();
                if (y != location.getBlockY()) location.setY(y);
                player.teleport(location, TeleportCause.COMMAND);
                return msg(sender, serverLang().getPlugMsg() + "Teleported you to the top.");
            }
            case "tpall" -> {
                if (Bukkit.getOnlinePlayers().size() < 2) return msg(sender, serverLang().getErrorMsg() + "You're the only one here.");
                for (Player player1 : Bukkit.getOnlinePlayers()) player1.teleport((Player) sender);
                return msg(sender, serverLang().getPlugMsg() + "Teleported everyone to you.");
            }
            case "tphere" -> {
                Player target = args.length > 0 ? Bukkit.getPlayer(args[0]) : null;
                if (args.length != 1) return msg(sender, getSyntax(label));
                if (target == null) return msg(sender, serverLang().getOfflineMsg());
                target.teleport(player);
                return msg(sender, serverLang().getPlugMsg() + "Teleported " + DataPlayer.getUser(target).getTabName() + serverLang().getTxtColor() + " to you.");
            }
            case "wtp" -> {
                if (args.length == 1 || args.length == 2) {
                    Player target = (args.length == 1) ? (Player) sender : Bukkit.getPlayer(args[1]);

                    if (target == null) return msg(sender, serverLang().getOfflineMsg());
                    World w = Bukkit.getWorld(args[0]);
                    if (w == null) return msg(sender, serverLang().getErrorMsg() + "That world doesn't exist!");

                    Spawn spawn = new Spawn(w.getName());
                    player.teleport(spawn.location(), PlayerTeleportEvent.TeleportCause.COMMAND);
                    String name = (target == sender) ? "you " : DataPlayer.getUser(player).getTabName() + serverLang().getTxtColor() + " ";
                    return msg(sender, serverLang().getPlugMsg() + "Sent " + name + "to " + serverLang().getDarkColor() + w.getName());
                }
                return msg(sender, getSyntax(label));
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + switch (label.toLowerCase()) {
            case "hub","lobby" -> helpBr("set", false);
            case "tphere" -> helpBr(USER, true);
            default -> helpBr("world", true) + " " + helpBr(USER, false);
        };
    }

    public List<String> commandNames() {
        return List.of("hub","jump","top","tpall","tphere","wtp");
    }
}
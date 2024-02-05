package io.github.rookietec9.enderplugin.commandgroups;

import io.github.rookietec9.enderplugin.configs.associates.Hub;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.USER;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Jeremi
 * @version 25.7.3
 * @since 21.4.5
 */
public class TeleportCommands implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) return msg(sender, serverLang().getOnlyUserMsg());
        if (args.length > 1) return msg(sender, getSyntax(label));

        switch (label.toLowerCase()) {
            case "hub", "lobby" -> {
                if (args.length == 1 && (args[0].equalsIgnoreCase("set"))) {
                    new Hub().setLocation(player.getLocation());
                    return msg(sender, serverLang().getPlugMsg() + "Set hub location!");
                }
                if (args.length == 0) {
                    if (null == new Hub().getLoc()) return msg(sender, serverLang() + "The world is incorrect for the hub.");
                    DataPlayer.get(player).hub();
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
            case "back" -> {
                Location location = locInt(player);
                if (location == null) return true;
                player.teleport(location, TeleportCause.SPECTATE);
                player.sendMessage(serverLang().getPlugMsg() + "Teleported you to your previous location.");
            }
        }
        return true;
    }

    public Location locInt(Player player) {
        if (DataPlayer.get(player).teleportationList.size() == 0) {
            msg(player, serverLang().getErrorMsg() + "No available locations.");
            return null;
        }

        if (DataPlayer.get(player).teleportIndex <= 0) DataPlayer.get(player).teleportIndex = DataPlayer.get(player).teleportationList.size() - 1;

        Location location = DataPlayer.get(player).teleportationList.get(DataPlayer.get(player).teleportIndex--);
        if (!location.getWorld().equals(player.getWorld())) return location;

        while (DataPlayer.get(player).teleportIndex > 0) {
            if (!location.getWorld().equals(player.getWorld())) return location;
            if (location.distance(player.getLocation()) > 2) return location;
            location = DataPlayer.get(player).teleportationList.remove(DataPlayer.get(player).teleportIndex--);
        }

        if (location.getWorld().equals(player.getWorld()) && location.distance(player.getLocation()) <= 2) DataPlayer.get(player).teleportIndex = DataPlayer.get(player).teleportationList.size() -1;

        while (DataPlayer.get(player).teleportIndex > 0) {
            if (!location.getWorld().equals(player.getWorld())) return location;
            if (location.distance(player.getLocation()) > 2) return location;
            location = DataPlayer.get(player).teleportationList.remove(DataPlayer.get(player).teleportIndex--);
        }
        return !location.getWorld().equals(player.getWorld()) || location.distance(player.getLocation()) > 2 ? DataPlayer.get(player).teleportationList.remove(DataPlayer.get(player).teleportationList.indexOf(location)) : null;
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + switch (label.toLowerCase()) {
            case "hub", "lobby" -> helpBr("set", false);
            default -> helpBr(USER, true);
        };
    }

    public List<String> commandNames() {
        return List.of("back", "hub", "jump", "top", "tpall", "tphere");
    }
}
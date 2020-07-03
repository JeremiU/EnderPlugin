package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 17.7.4
 */
public class WorldSpawnCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());

        Player player = (Player) sender;
        Spawn spawn = new Spawn(player.getWorld().getName());

        spawn.setX(player.getLocation().getX());
        spawn.setY(player.getLocation().getY());
        spawn.setZ(player.getLocation().getZ());
        spawn.setPitch(player.getLocation().getPitch());
        spawn.setYaw(player.getLocation().getYaw());
        spawn.setWorld(player.getLocation().getWorld());

        return msg(sender, serverLang().getPlugMsg() + "Set your world's spawn to your location!");
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return null;

        Player player = (Player) sender;
        if (args.length == 1) return tabOption(args[0], player.getLocation().getBlockX() + "");
        if (args.length == 2) return tabOption(args[1], player.getLocation().getBlockY() + "");
        if (args.length == 3) return tabOption(args[2], player.getLocation().getBlockZ() + "");
        return null;
    }

    public String getSyntax(String label) {
        return null;
    }

    public List<String> commandNames() {
        return List.of("wsp");
    }
}
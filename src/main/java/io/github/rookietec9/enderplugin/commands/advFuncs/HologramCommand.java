package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.MODE;
import static io.github.rookietec9.enderplugin.Reference.MSG;

/**
 * @author Jeremi
 * @version 25.5.4
 * @since 9.3.6
 */
public class HologramCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 1) return msg(sender, getSyntax(label));
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());

        if (!args[0].equalsIgnoreCase("floor") && !args[0].equalsIgnoreCase("eyelevel")) return msg(sender, getSyntax(label));
        spawnHologram(StringUtils.join(args, " ", 1, args.length), args[0].equalsIgnoreCase("floor"), ((Player) sender).getLocation());
        return true;
    }

    public static ArmorStand spawnHologram(String text, boolean floor, Location location) {
        location = location.clone();
        location.setY(location.getY() + (floor ? 0.5 : 1));

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setSmall(floor);
        armorStand.setMarker(true);
        armorStand.setVisible(false);
        armorStand.setCustomName(Minecraft.tacc(text));
        armorStand.setCustomNameVisible(true);

        return armorStand;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? tabOption(args[0], "floor", "eyelevel") : null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(MODE, true) + helpBr(MSG, true);
    }

    public List<String> commandNames() {
        return List.of("hologram");
    }
}
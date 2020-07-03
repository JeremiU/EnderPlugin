package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.MODE;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.MSG;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 9.3.6
 */
public class HologramCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 1) return msg(sender, getSyntax(label));
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
        if (!args[0].equalsIgnoreCase("floor") && !args[0].equalsIgnoreCase("eyelevel")) return msg(sender, getSyntax(label));

        boolean baby = args[0].equalsIgnoreCase("floor");
        Player player = (Player) sender;
        Location toSpawn = player.getLocation();
        if (!baby) toSpawn.setY(toSpawn.getY() + 1);

        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(toSpawn, EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setSmall(baby);
        armorStand.setMarker(true);
        armorStand.setVisible(false);
        armorStand.setCustomName(Minecraft.tacc(StringUtils.join(args, " ", 1, args.length)));
        armorStand.setCustomNameVisible(true);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return tabOption(args[0], "floor", "eyelevel");
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(MODE, true) + helpBr(MSG, true);
    }

    public List<String> commandNames() {
        return List.of("hologram");
    }
}
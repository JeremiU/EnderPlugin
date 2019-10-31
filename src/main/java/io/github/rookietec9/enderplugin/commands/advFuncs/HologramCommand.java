package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremi
 * @version 13.4.4
 * @since 9.3.6
 */
public class HologramCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang lang = new Lang(Langs.fromSender(sender));
        if (args.length <= 1) {
            sender.sendMessage(getSyntax(command, lang));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(lang.getOnlyUserMsg());
            return true;
        }

        if (!args[0].equalsIgnoreCase("baby") && !args[0].equalsIgnoreCase("man")) {
            sender.sendMessage(getSyntax(command, lang));
            return true;
        }

        boolean baby = false;
        if (args[0].equalsIgnoreCase("baby")) baby = true;

        Player player = (Player) sender;
        Location toSpawn = player.getEyeLocation();
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(toSpawn, EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setSmall(baby);
        armorStand.setMarker(true);
        armorStand.setVisible(false);
        armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, " ", 1, args.length)));
        armorStand.setCustomNameVisible(true);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tabList = new ArrayList<>();
        if (args.length == 1) {
            tabList.add("baby");
            tabList.add("man");
            return tabList;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() +
                        Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " + Utils.Reference.OPEN_MANDATORY_CHAR + Utils.Reference.MESSAGE +
                        Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "hologram";
    }
}
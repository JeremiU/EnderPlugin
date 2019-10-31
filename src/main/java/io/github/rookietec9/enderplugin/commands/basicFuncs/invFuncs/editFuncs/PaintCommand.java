package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.editFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

/**
 * Sets RGB values for leather armor.
 *
 * @author Jeremi
 * @version 1.3.4.4 // 11.6.0
 * @since 5.2.8
 */

public class PaintCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length != 3) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        int r, g, b;
        try {
            r = Integer.valueOf(args[0]);
            g = Integer.valueOf(args[1]);
            b = Integer.valueOf(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(l.getErrorMsg() + "That's not a number!");
            return true;
        }

        Player p = (Player) sender;
        ItemStack inHand = p.getInventory().getItem(p.getInventory().getHeldItemSlot());

        if (r > 255 || g > 255 || b > 255 || r < 0 || g < 0 || b < 0) {
            sender.sendMessage(l.getErrorMsg() + "An RGB value has to be in the range of 0-255!");
            return true;
        }
        if (inHand == null) {
            sender.sendMessage(l.getErrorMsg() + "Please hold something.");
            return true;
        }

        if (inHand.getType() != Material.LEATHER_HELMET
                && inHand.getType() != Material.LEATHER_CHESTPLATE
                && inHand.getType() != Material.LEATHER_LEGGINGS
                && inHand.getType() != Material.LEATHER_BOOTS) {
            sender.sendMessage(l.getErrorMsg() + "Please a dyable item.");
            return true;
        }
        LeatherArmorMeta pam = (LeatherArmorMeta) inHand.getItemMeta();
        pam.setColor(Color.fromBGR(b, g, r));
        inHand.setItemMeta(pam);
        sender.sendMessage(l.getPlugMsg() + "Set the color to: §c" + r + " §a" + g + " §b" + b + ChatColor.WHITE + ".");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "red" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "green" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "blue" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " "
        };
    }

    public String commandName() {
        return "paint";
    }
}
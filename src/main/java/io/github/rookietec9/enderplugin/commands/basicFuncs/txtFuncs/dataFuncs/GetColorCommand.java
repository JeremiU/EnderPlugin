package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.dataFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

/**
 * Gets the color of an item, or a potion effect.
 *
 * @author Jeremi
 * @version 16.2.6
 * @since 8.0.1
 *
 * How did it take 13.7.1 -> 16.2.6 to notice a pretty obvious error??
 */
public class GetColorCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        ItemStack inHand = ((Player) sender).getInventory().getItem(((Player) sender).getInventory().getHeldItemSlot());
        if (inHand == null || (inHand.getType() != Material.LEATHER_HELMET && inHand.getType() != Material.LEATHER_CHESTPLATE && inHand.getType() != Material.LEATHER_LEGGINGS
                && inHand.getType() != Material.LEATHER_BOOTS)) {
            sender.sendMessage(l.getErrorMsg() + "Please hold a dyable item.");
            return true;
        }
        LeatherArmorMeta pam = (LeatherArmorMeta) inHand.getItemMeta();
        if (pam.getColor() == null) {
            sender.sendMessage(l.getErrorMsg() + "That isn't dyed!");
            return true;
        }
        sender.sendMessage(l.getPlugMsg() + "§c" + pam.getColor().getRed() + "§a " + pam.getColor().getGreen() + "§b " + pam.getColor().getBlue());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "getcolor";
    }
}
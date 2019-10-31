package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.editFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Sets the item in a player's hand to the helmet slot.
 *
 * @author Jeremi
 * @version 11.6.9
 * @since 4.6.2
 */
public class HatCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        Player p = (Player) sender;
        ItemStack helmet = p.getInventory().getHelmet();
        ItemStack inHand = p.getInventory().getItem(p.getInventory().getHeldItemSlot());
        if (inHand == null || inHand.getType() == Material.AIR) {
            sender.sendMessage(l.getErrorMsg() + "Please hold something.");
            return true;
        }

        p.getInventory().setHelmet(inHand);
        p.getInventory().setItemInHand(helmet);
        p.updateInventory();
        p.sendMessage(l.getPlugMsg() + "Enjoy the hat.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "hat";
    }
}
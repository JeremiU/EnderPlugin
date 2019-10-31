package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.editFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author Jeremi
 * @version 13.4.4
 * @since 9.9.6
 */
public class UnbreakableCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));

        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }

        if (!args[0].equalsIgnoreCase("off") && !args[0].equalsIgnoreCase("on")
                && !args[0].equalsIgnoreCase("true") && !args[0].equalsIgnoreCase("false")) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }

        boolean unbreakable = true;

        if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("false")) unbreakable = false;

        Player player = (Player) sender;
        ItemStack inHand = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
        if (inHand == null) {
            player.sendMessage(l.getErrorMsg() + "Please Hold something!");
            return true;
        }
        ItemMeta meta = inHand.getItemMeta();
        inHand.setDurability((short) 0);
        meta.spigot().setUnbreakable(unbreakable);
        inHand.setItemMeta(meta);
        player.updateInventory();
        if (unbreakable) sender.sendMessage(l.getPlugMsg() + "Set the item to unbreakable.");
        else sender.sendMessage(l.getPlugMsg() + "Set the item to breakable.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " "
        };
    }

    public String commandName() {
        return "unbreakable";
    }
}
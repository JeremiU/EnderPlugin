package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.editFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Repairs an item based of the player's request.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 5.1.1
 */
public class RepairCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Lang l = new Lang(Langs.fromSender(sender));

        if (args.length != 1 && args.length != 0) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItem(p.getInventory().getHeldItemSlot());
        if (item == null || item.getType() == Material.AIR && (args.length == 1 && !args[0].equalsIgnoreCase("All"))) {
            sender.sendMessage(l.getErrorMsg() + "Nothing in your hand!");
            return true;
        }
        if (args.length == 0) {
            item.setDurability((short) 0);
            sender.sendMessage(l.getPlugMsg() + "repaired the item in your hand.");
            return true;
        }
        if (args[0].equalsIgnoreCase("All")) {
            for (ItemStack it : p.getInventory().getArmorContents()) {
                if (it == null || it.getType().equals(Material.AIR)) {
                    continue;
                }
                it.setDurability(it.getType().getMaxDurability());
            }
            p.getInventory().getBoots().setDurability((short) 0);
            p.getInventory().getLeggings().setDurability((short) 0);
            p.getInventory().getChestplate().setDurability((short) 0);
            p.getInventory().getHelmet().setDurability((short) 0);
            sender.sendMessage(l.getPlugMsg() + "repaired every item in your inventory.");
            return true;
        }

        if (args[0].equalsIgnoreCase("Boots")) {
            item = p.getInventory().getBoots();
            item.setDurability((short) 0);
            sender.sendMessage(l.getPlugMsg() + "repaired your boots.");
            return true;
        }

        if (args[0].equalsIgnoreCase("Chestplate")) {
            item = p.getInventory().getChestplate();
            item.setDurability((short) 0);
            sender.sendMessage(l.getPlugMsg() + "repaired your chestplate.");
            return true;
        }

        if (args[0].equalsIgnoreCase("Helmet")) {
            item = p.getInventory().getHelmet();
            item.setDurability((short) 0);
            sender.sendMessage(l.getPlugMsg() + "repaired your helmet.");
            return true;
        }

        if (args[0].equalsIgnoreCase("Leggings")) {
            item = p.getInventory().getLeggings();
            item.setDurability((short) 0);
            sender.sendMessage(l.getPlugMsg() + "repaired your leggings.");
            return true;
        }
        sender.sendMessage(getSyntax(command, l));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("All");
            l.add("Boots");
            l.add("Chestplate");
            l.add("Helmet");
            l.add("Leggings");
            return l;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "item" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "repair";
    }
}
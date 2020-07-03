package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.configs.associates.Lang;
import io.github.rookietec9.enderplugin.utils.datamanagers.Item;
import io.github.rookietec9.enderplugin.utils.reference.Syntax;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * Repairs an item based of the player's request.
 *
 * @author Jeremi
 * @version 22.8.0
 * @since 5.1.1
 */
public class RepairCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1 && args.length != 0) return msg(sender, this.getSyntax(command, serverLang()));
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());

        Player p = (Player) sender;
        HashMap<Integer, Item> itemMap = new HashMap<>();

        Item item = Item.fromItemStack(p.getInventory().getItem(p.getInventory().getHeldItemSlot()));

        if (item.isEmpty() && args.length == 1 && !args[0].equalsIgnoreCase("all")) return msg(sender, serverLang().getErrorMsg() + "Nothing in your hand!");
        if (args.length == 0) itemMap.put(p.getInventory().getHeldItemSlot(), Item.fromItemStack(p.getItemInHand()));
        if (args.length == 1) switch (args[0].toLowerCase()) {
            case "all" -> {
                for (int i = 0; i < p.getInventory().getArmorContents().length; i++) if (p.getInventory().getArmorContents()[i] != null) itemMap.put(i, Item.fromItemStack(p.getInventory().getArmorContents()[i]));
                for (int i = 0; i < p.getInventory().getContents().length; i++) if (p.getInventory().getContents()[i] != null) itemMap.put(i, Item.fromItemStack(p.getInventory().getContents()[i]));
            }
            case "cap", "helmet" -> itemMap.put(35, Item.fromItemStack(p.getInventory().getHelmet()));
            case "tunic", "chestplate" -> itemMap.put(34, Item.fromItemStack(p.getInventory().getChestplate()));
            case "pants", "leggings" -> itemMap.put(33, Item.fromItemStack(p.getInventory().getLeggings()));
            case "boots" -> itemMap.put(32, Item.fromItemStack(p.getInventory().getBoots()));
            default -> {
                return msg(sender, getSyntax(command, serverLang()));
            }
        }

        for (int i : itemMap.keySet()) {
            itemMap.get(i).setDurability((short) 0);
            p.getInventory().setItem(i, itemMap.get(i).toItemStack());
        }
        return msg(sender, serverLang().getPlugMsg() + "repaired" + ((args.length == 1) ? (args[0].equalsIgnoreCase("all") ? " everything." : " your " + args[0].toLowerCase() + ".") : " the item in your hand."));
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return tabOption(args[0],"all","boots","chestplate","helmet","leggings");
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Syntax.OMC + l.getLightColor() + "item" + l.getCmdExColor() + Syntax.CMC
        };
    }

    public String getSyntax(String label) {
        return null;
    }

    public List<String> commandNames() {
        return List.of("repair");
    }
}
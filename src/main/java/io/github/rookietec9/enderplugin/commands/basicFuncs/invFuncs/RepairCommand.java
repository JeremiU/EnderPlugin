package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
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
 * @version 25.5.4
 * @since 5.1.1
 */
public class RepairCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1 && args.length != 0) return msg(sender, getSyntax(label));
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());

        Player p = (Player) sender;
        HashMap<Integer, ItemWrapper<?>> itemMap = new HashMap<>();

        ItemWrapper<?> itemWrapper = ItemWrapper.fromItemStack(p.getInventory().getItem(p.getInventory().getHeldItemSlot()));

        if (itemWrapper.isEmpty() && args.length == 1 && !args[0].equalsIgnoreCase("all")) return msg(sender, serverLang().getErrorMsg() + "Nothing in your hand!");
        if (args.length == 0) itemMap.put(p.getInventory().getHeldItemSlot(), ItemWrapper.fromItemStack(p.getItemInHand()));
        if (args.length == 1) switch (args[0].toLowerCase()) {
            case "all" -> {
                for (int i = 0; i < p.getInventory().getArmorContents().length; i++) if (p.getInventory().getArmorContents()[i] != null) itemMap.put(i, ItemWrapper.fromItemStack(p.getInventory().getArmorContents()[i]));
                for (int i = 0; i < p.getInventory().getContents().length; i++) if (p.getInventory().getContents()[i] != null) itemMap.put(i, ItemWrapper.fromItemStack(p.getInventory().getContents()[i]));
            }
            case "cap", "helmet" -> itemMap.put(35, ItemWrapper.fromItemStack(p.getInventory().getHelmet()));
            case "tunic", "chestplate" -> itemMap.put(34, ItemWrapper.fromItemStack(p.getInventory().getChestplate()));
            case "pants", "leggings" -> itemMap.put(33, ItemWrapper.fromItemStack(p.getInventory().getLeggings()));
            case "boots" -> itemMap.put(32, ItemWrapper.fromItemStack(p.getInventory().getBoots()));
            default -> {
                return msg(sender, getSyntax(label));
            }
        }

        for (int i : itemMap.keySet()) {
            itemMap.get(i).setDurability((short) 0);
            p.getInventory().setItem(i, itemMap.get(i).toItemStack());
        }
        return msg(sender, serverLang().getPlugMsg() + "repaired" + ((args.length == 1) ? (args[0].equalsIgnoreCase("all") ? " everything." : " your " + args[0].toLowerCase() + ".") : " the item in your hand."));
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? tabOption(args[0],"all","boots","chestplate","helmet","leggings") : null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + " " + helpBr("item", true);
    }

    public List<String> commandNames() {
        return List.of("repair");
    }
}
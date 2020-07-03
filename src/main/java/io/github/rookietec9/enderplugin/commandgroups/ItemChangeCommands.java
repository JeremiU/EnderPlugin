package io.github.rookietec9.enderplugin.commandgroups;

import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.Item;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.MODE;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 21.4.5
 */
public class ItemChangeCommands implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        label = label != null ? label : command.getName();

        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
        if (args.length > 1 && Java.argWorks(label, "setcount", "unbreakable") || (args.length == 0 && !label.equalsIgnoreCase("telly"))) return msg(sender, this.getSyntax(label));

        Player player = (Player) sender;
        Item<?> item = Item.fromItemStack(player.getItemInHand());
        if (item.isEmpty() && !Java.argWorks(args[0], "telly", "games")) return msg(sender, serverLang().getErrorMsg() + "Please hold something.");

        switch (label.toLowerCase()) {
            case "rename", "name" -> item.setName(Minecraft.tacc(StringUtils.join(args, ' ', 0, args.length)));
            case "unbreakable" -> {
                if (!Java.argWorks(args[0], "on", "off", "true", "false")) return msg(sender, getSyntax(label));
                item.setUnbreakable(Java.argWorks(args[0], "on", "true"));
            }
            case "setcount" -> {
                int amount;
                try {
                    amount = Integer.parseInt(args[0]);
                } catch (NumberFormatException ex) {
                    return msg(sender, serverLang().getNumFormatMsg());
                }
                if (!Java.isInRange(amount, 1, 127)) return msg(sender, serverLang().getErrorMsg() + "The amount has to be between 1-127!");
                item.setCount(amount).toItemStack();
            }
            case "hideflags", "flag" -> {
                ItemFlag flag = null;
                if (args.length == 2) {
                    if (!Java.argWorks(args[0], "show", "hide")) return msg(sender, this.getSyntax(label));

                    for (ItemFlag itemFlag : ItemFlag.values()) if (args[1].equalsIgnoreCase(itemFlag.toString())) flag = ItemFlag.valueOf(args[1].toUpperCase());
                    if (flag == null) return msg(sender, serverLang().getErrorMsg() + "Wrong flags!");
                    if (!args[0].equalsIgnoreCase("show")) item.addFlag(flag);
                    else item.removeFlags(flag);
                }
            }
            case "paint" -> {
                if (args.length != 3) return msg(sender, this.getSyntax(label));
                int r, g, b;
                try {
                    r = Integer.parseInt(args[0]);
                    g = Integer.parseInt(args[1]);
                    b = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    return msg(sender, serverLang().getErrorMsg() + "That's not a number!");
                }
                if (r > 255 || g > 255 || b > 255 || r < 0 || g < 0 || b < 0) return msg(sender, serverLang().getErrorMsg() + "An RGB value has to be in the range of 0-255!");
                if (!item.isPaintAble()) return msg(sender, serverLang().getErrorMsg() + "Please a dyable item.");
                item.setColor(r, g, b);
            }
            case "telly", "games" -> {
                player.getInventory().addItem(Inventories.TELLY_ITEM);
                return msg(sender, serverLang().getPlugMsg() + "Gave you the telly.");
            }
        }

        player.getInventory().setItemInHand(item.toItemStack());
        return msg(sender, serverLang().getPlugMsg() + "Changed the " + switch (label.toLowerCase()) {
            case "rename" -> "name";
            case "unbreakable" -> "breakability";
            case "setcount" -> "amount";
            case "hideflags", "flag" -> "ItemFlag";
            default -> "color";
        } + " of the item in your hand.");
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        alias = alias != null ? alias : command.getName();
        switch (alias.toLowerCase()) {
            case "unbreakable" -> {
                if (args.length == 1) return tabOption(args[0], "on", "off");
            }
            case "setcount" -> {
                if (args.length == 1) return tabOption(args[0], "8", "16", "32", "64");
            }
            case "hideflags", "flag" -> {
                if (args.length == 1) return tabOption(args[0], "show", "hide");
                if (args.length == 2) return tabOption(args[1], ItemFlag.class);
            }
        }
        ;
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + switch (label.toLowerCase()) {
            case "rename", "name" -> helpBr("name", true);
            case "unbreakable" -> helpBr(MODE, true);
            case "setcount" -> helpBr("count", true);
            case "hideflags" -> helpBr(MODE, true) + " " + helpBr("flag", true);
            default -> helpBr("r", true) + " " + helpBr("g", true) + " " + helpBr("b", true);
        };
    }

    public List<String> commandNames() {
        return List.of("rename", "unbreakable", "setcount", "hideflags", "setcount", "telly", "paint");
    }
}
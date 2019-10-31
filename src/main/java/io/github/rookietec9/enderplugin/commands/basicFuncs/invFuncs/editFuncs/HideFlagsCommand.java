package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.editFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremi
 * @version 15.6.4
 * @since 8.8.8
 */
public class HideFlagsCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        ItemFlag flag = null;

        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }

        Player player = (Player) sender;
        boolean hide;
        ItemStack inHand = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
        if (inHand == null) {
            player.sendMessage(l.getErrorMsg() + "Please Hold something!");
            return true;
        }
        ItemMeta meta = inHand.getItemMeta();

        if (args.length == 2) {
            if (!args[0].equalsIgnoreCase("hide") && !args[0].equalsIgnoreCase("show")) {
                sender.sendMessage(this.getSyntax(command, l));
                return true;
            }
            hide = !args[0].equalsIgnoreCase("show");

            for (ItemFlag itemFlag : ItemFlag.values())
                if (args[1].equalsIgnoreCase(itemFlag.toString())) flag = ItemFlag.valueOf(args[1].toUpperCase());

            if (flag == null) {
                sender.sendMessage(l.getErrorMsg() + "Wrong flags!");
                return true;
            }

            if (hide) meta.addItemFlags(flag);
            else meta.removeItemFlags(flag);
            inHand.setItemMeta(meta);
            player.updateInventory();
            sender.sendMessage(l.getPlugMsg() + "Changed the Flags: " + flag.name());
            return true;
        } else sender.sendMessage(this.getSyntax(command, l));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> l = new ArrayList<>();

        if (args.length == 2) {
            for (ItemFlag flag : ItemFlag.values()) l.add(flag.toString().toUpperCase());
            return l;
        }

        if (args.length == 1) {
            l.add("show");
            l.add("hide");
            return l;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() +
                        "hide/show" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() +
                        "flag" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

   public String commandName() {
        return "hideFlags";
    }
}
package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.editFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Renames the item a player is holding.
 *
 * @author Jeremi
 * @version 11.6.0
 * @since 0.5.8
 */
public class RenameCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }
        String name = StringUtils.join(args, ' ', 0, args.length);
        Player player = (Player) sender;
        ItemStack inHand = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
        if (inHand == null || inHand.getType() == Material.AIR) {
            sender.sendMessage(l.getErrorMsg() + "Please hold something.");
            return true;
        }

        ItemMeta currentMeta = inHand.getItemMeta();
        currentMeta.setDisplayName(ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', name));
        inHand.setItemMeta(currentMeta);
        sender.sendMessage(l.getPlugMsg() + "Renamed item to: " + ChatColor.translateAlternateColorCodes('&', name));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "name" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "rename";
    }
}
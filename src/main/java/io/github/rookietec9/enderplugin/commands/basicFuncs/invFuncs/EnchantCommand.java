package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;

/**
 * Opens a player's crafting menu.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 0.5.2
 */
public class EnchantCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Lang l = new Lang(Langs.fromSender(sender));

        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        Player player = (Player) sender;
        player.sendMessage(l.getPlugMsg() + "Opening Enchanting Menu...");
        player.openInventory(Bukkit.createInventory(player, InventoryType.ENCHANTING));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "enchant";
    }
}
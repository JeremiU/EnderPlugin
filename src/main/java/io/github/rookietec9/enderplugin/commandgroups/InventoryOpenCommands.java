package io.github.rookietec9.enderplugin.commandgroups;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.MODE;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 21.3.7
 */
public class InventoryOpenCommands implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        label = label != null ? label : command.getName();

        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
        Player player = (Player) sender;

        if (null != label) switch (label.toLowerCase()) {
            case "anvil" -> player.openInventory(Bukkit.createInventory(player, InventoryType.ANVIL));
            case "enderchest", "ec" -> player.openInventory(player.getEnderChest());
            case "craft" -> player.openWorkbench(null, true);
            case "invsee" -> {
                if (args.length != 1) return msg(sender, getSyntax(label));
                if (Bukkit.getPlayer(args[0]) == null) return msg(sender, serverLang().getOfflineMsg());
                player.openInventory(Bukkit.getPlayer(args[0]).getInventory());
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
       return helpLabel(label) + helpBr(MODE, true);
    }

    public List<String> commandNames() {
        return List.of("anvil","enderchest","craft","invsee");
    }
}
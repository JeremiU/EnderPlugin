package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * Sets the item in a player's hand to the helmet slot.
 *
 * @author Jeremi
 * @version 24.3.6
 * @since 4.6.2
 */
public class HatCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());

        Player p = (Player) sender;

        ItemWrapper<?> helmet = ItemWrapper.fromItemStack(p.getInventory().getHelmet());
        ItemWrapper<?> inHand = ItemWrapper.fromItemStack(p.getInventory().getItem(p.getInventory().getHeldItemSlot()));
        if (inHand.isEmpty()) return msg(sender, serverLang().getErrorMsg() + "Please hold something.");

        p.getInventory().setHelmet(inHand.toItemStack());
        p.getInventory().setItemInHand(helmet.toItemStack());
        p.updateInventory();
        return msg(sender, serverLang().getPlugMsg() + "Enjoy the hat.");
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return null;
    }

    public List<String> commandNames() {
        return List.of("hat");
    }
}
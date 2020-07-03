package io.github.rookietec9.enderplugin.commands.games.ESG;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.configs.esg.ESGKit;
import io.github.rookietec9.enderplugin.configs.esg.ESGPlayer;
import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.utils.datamanagers.Item;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 7.1.9
 */
public class ESGCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
        Player player = (Player) sender;

        Inventory personalizedInventory = Inventories.ESG_KIT;

        for (int i = 0; i < personalizedInventory.getSize(); i++) {
            if (personalizedInventory.getItem(i) == null) continue;
            ESGPlayer esg = new ESGPlayer(player);
            if (esg.getKitUnlocked(ESGKit.Kits.values()[i])) personalizedInventory.getItem(i).setAmount(esg.getKitLevel(ESGKit.Kits.values()[i]));

            if (i < ESGKit.Kits.values().length && esg.getKitLevel(ESGKit.Kits.values()[i]) == 0) {
                personalizedInventory.getItem(i).setType(Material.BARRIER);
                personalizedInventory.getItem(i).setAmount(1);
                Item item = Item.fromItemStack(personalizedInventory.getItem(i));
                item.setLore("","§4Not unlocked yet!");
                personalizedInventory.setItem(i, item.toItemStack());
            }
        }
        player.openInventory(personalizedInventory);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return null;
    }

    public List<String> commandNames() {
        return List.of("esg");
    }
}
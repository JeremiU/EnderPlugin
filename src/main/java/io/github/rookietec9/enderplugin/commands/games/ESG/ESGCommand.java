package io.github.rookietec9.enderplugin.commands.games.ESG;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.esg.ESGKit;
import io.github.rookietec9.enderplugin.API.esg.ESGPlayer;
import io.github.rookietec9.enderplugin.inventories.ESGInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * @author Jeremi
 * @version 13.4.4
 * @since 7.1.9
 */
public class ESGCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }

        Player Player = (Player) sender;
        Inventory inv = Bukkit.createInventory(null, 45, "Choose Your Kit");

        int counter = 0;
        for (ESGKit esgKit : ESGKit.values()) {
            if (esgKit == ESGKit.SHELL) continue;
            inv.setItem(counter, ESGInventory.item(esgKit, new ESGPlayer((Player) sender)));
            counter++;
        }
        Player.openInventory(inv);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "esg";
    }
}
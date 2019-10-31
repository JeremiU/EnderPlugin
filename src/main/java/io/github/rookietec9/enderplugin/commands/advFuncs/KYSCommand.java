package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * An exclusive command that enables a hidden feature in previous versions of MineCraft.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 1.7.4
 */
public class KYSCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        Player p = (Player) sender;
        ItemStack item = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 500, 100), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 500, 100), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, 500, 100), true);
        meta.setDisplayName(ChatColor.DARK_BLUE + "§lMAGIC BLEACH");
        item.setItemMeta(meta);
        p.getInventory().addItem(item);
        p.setHealth(2.0D);
        Bukkit.broadcastMessage(ChatColor.RED + p.getName() + "§2 NEEDS SOME MILK!");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "suicide";
    }
}
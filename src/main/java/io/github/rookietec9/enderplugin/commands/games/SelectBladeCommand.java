package io.github.rookietec9.enderplugin.commands.games;

import io.github.rookietec9.enderplugin.API.ActionBar;
import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.WizardsBlades;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.xboards.WizardsBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jeremi
 * @version 16.2.8
 * @since 11.6.6
 */
public class SelectBladeCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));

        if ((!(sender instanceof Player)) && (args.length == 1)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }

        if (args.length == 1 || args.length == 2) {
            Player player;
            if (args.length == 1) player = (Player) sender;
            else player = Bukkit.getPlayer(args[1]);

            if (player == null) {
                sender.sendMessage(l.getOfflineMsg());
                return true;
            }
            for (WizardsBlades blades : WizardsBlades.values()) {
                if (args[0].equalsIgnoreCase(blades.getBladeName())) {
                    giveBlade(player, blades);
                    return true;
                }
            }
            player.sendMessage(l.getErrorMsg() + "That's not a valid blade!");
        } else {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }
        return true;
    }

    public void giveBlade(Player p, WizardsBlades blade) {
        ItemStack helmet = null, chestplate = null, leggings = null, boots = null, sword = null;

        if (blade.getMode() == 1) {
            helmet = blade.getArmor()[0];
            chestplate = blade.getArmor()[1];
            leggings = blade.getArmor()[2];
            boots = blade.getArmor()[3];

            sword = new ItemStack(blade.getWeaponMat());
        }

        if (blade.getMode() == 2 || blade.getMode() == 3) {
            helmet = WizardsBlades.armorPiece(Material.LEATHER_HELMET, null, 0, blade.getColor());
            chestplate = WizardsBlades.armorPiece(Material.CHAINMAIL_CHESTPLATE, Enchantment.PROTECTION_ENVIRONMENTAL, 5, null);
            leggings = WizardsBlades.armorPiece(Material.LEATHER_LEGGINGS, null, 0, blade.getColor());
            boots = WizardsBlades.armorPiece(Material.LEATHER_BOOTS, null, 0, blade.getColor());
            sword = new ItemStack(Material.WOOD_SWORD);
        }

        if (blade.getMode() == 3) {
            if (blade.getEnch() == Enchantment.PROTECTION_FALL) {
                boots.addUnsafeEnchantment(blade.getEnch(), blade.getEnchLvl());
            }
            if (blade.getEnch() == Enchantment.KNOCKBACK) {
                sword.addUnsafeEnchantment(blade.getEnch(), blade.getEnchLvl());
            }
        }

        for (ItemStack itemStack : new ItemStack[]{helmet, chestplate, leggings, boots}) {
            ItemMeta meta = itemStack.getItemMeta();
            String name = Utils.upSlash(itemStack.getType().toString());

            if (name.contains("Leather"))
                name = name.replace("Helmet", "Cap").replace("Chestplate", "Tunic").replace("Leggings", "Pants");

            meta.setDisplayName(blade.getChatColor() + name.replace("Chainmail", "Chain"));
            itemStack.setItemMeta(meta);
        }

        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(blade.getChatColor() + blade.getBladeName() + " Blade");

        List<String> lore = new ArrayList<>();
        Collections.addAll(lore, blade.getLore());
        lore.add("");
        lore.add(blade.getChatColor() + "+" + Utils.BukkitTools.getWeaponDamage(sword) + " §7Attack Damage");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        sword.setItemMeta(meta);

        //FINAL
        new WizardsBoard(p).updateBlade(blade.getBladeName());
        new User(p).clear();

        for (ItemStack itemStack : new ItemStack[]{sword, helmet, chestplate, leggings, boots}) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.spigot().setUnbreakable(true);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemStack.setItemMeta(itemMeta);
        }

        p.setFlying(false);
        p.setAllowFlight(false);
        p.setHealth(20);
        p.setGameMode(GameMode.ADVENTURE);
        for (PotionEffect potionEffect : p.getActivePotionEffects()) p.removePotionEffect(potionEffect.getType());
        p.getInventory().setHelmet(helmet);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setBoots(boots);
        p.getInventory().setHeldItemSlot(0);
        p.getInventory().setItemInHand(sword);

        ChatColor txtColor = (blade.getChatColor() == ChatColor.WHITE) ? ChatColor.GRAY : ChatColor.WHITE;

        ActionBar.send(txtColor + "" + ChatColor.BOLD + "GAVE YOU THE " + blade.getChatColor() + "" + ChatColor.BOLD + blade.getBladeName().toUpperCase() + txtColor + "" + ChatColor.BOLD + " BLADE", p);
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            for (WizardsBlades wizardsBlades : WizardsBlades.values())
                l.add(wizardsBlades.getBladeName());
            return l;
        }
        return l;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "blade type" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " + Utils.Reference.OPEN_OPTIONAL_CHAR +
                        l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR
        };
    }

    public String commandName() {
        return "selectblade";
    }
}
package io.github.rookietec9.enderplugin.inventories;

import io.github.rookietec9.enderplugin.API.Item;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.esg.ESGKit;
import io.github.rookietec9.enderplugin.API.esg.ESGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * A refreshed class that creates the shop menu. Exists long before I actually cared to work on it.
 *
 * @author Jeremi
 * @version 11.6.0
 * @see io.github.rookietec9.enderplugin.events.games.esg.ESGShopEvent
 * @since 1.2.0
 */
public class ESGInventory {
    public static final Inventory shopMenu = Bukkit.createInventory(null, 45, "§fE§6S§fG | Shop Menu");
    public static final Inventory upgradeMenu = Bukkit.createInventory(null, 45, "§fE§6S§fG | Edit Default Kits");
    public static final Inventory premiumMenu = Bukkit.createInventory(null, 45, "§fE§6S§fG | Edit Premium Kits");

    static {
        Item buyMenu = new Item(Material.COAL, new String[]{"§7Upgrade default kits."}, "§fDefault Kits", 1);
        Item defaultMenu = new Item(Material.DIAMOND, new String[]{"§7Upgrade/Buy premium kits."}, "§fPremium Kits", 1);

        shopMenu.setItem(10, buyMenu.getItem());
        shopMenu.setItem(16, defaultMenu.getItem());
    }

    static {
        int counter = 0;
        for (ESGKit esgKit : ESGKit.values()) {
            if (esgKit == ESGKit.SHELL) continue;
            if (esgKit.getFree()) {
                upgradeMenu.setItem(counter, ESGInventory.item(esgKit, null));
                counter++;
            }
        }
        counter = 0;
        for (ESGKit esgKit : ESGKit.values()) {
            if (esgKit == ESGKit.SHELL) continue;
            if (!esgKit.getFree()) {
                premiumMenu.setItem(counter, ESGInventory.item(esgKit, null));
                counter++;
            }
        }

        premiumMenu.setItem(premiumMenu.getSize() - 1, new Item("§7GO back", Material.ACACIA_DOOR, (byte) 0).getItem());
        upgradeMenu.setItem(upgradeMenu.getSize() - 1, new Item("§7GO back", Material.ACACIA_DOOR, (byte) 0).getItem());
    }

    public static ItemStack item(ESGKit esgKit, ESGPlayer esgPlayer) {
        boolean useLevel = true;
        Item itemStack = new Item(esgKit.getType(), new String[]{ChatColor.DARK_GRAY + esgKit.getLoreLine()}, ChatColor.WHITE +
                Utils.upSlash(esgKit.toString()), esgKit.getDataByte(), 1);

        if (esgPlayer == null) useLevel = false;

        if (useLevel)
            itemStack.setName(ChatColor.WHITE + Utils.upSlash(esgKit.toString()) + " §r[" + esgKit.getColor() + esgPlayer.getKitLevel(esgKit) + "§r]");

        ItemStack item = itemStack.getItem();

        if (item.getType() == Material.LEATHER_BOOTS || item.getType() == Material.LEATHER_LEGGINGS ||
                item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_CHESTPLATE) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) item.getItemMeta();
            leatherArmorMeta.setColor(esgKit.getItemColor());
            item.setItemMeta(leatherArmorMeta);
            if (esgKit.getGlow()) item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            if (esgKit.getGlow() || esgKit.hideFlags()) leatherArmorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            if (esgKit.getGlow() || esgKit.hideFlags()) leatherArmorMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            if (esgKit.getGlow() || esgKit.hideFlags()) leatherArmorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(leatherArmorMeta);
        }
        return item;
    }
}
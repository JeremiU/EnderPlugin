package io.github.rookietec9.enderplugin.events.games.esg;

import io.github.rookietec9.enderplugin.API.Item;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.esg.ESGKit;
import io.github.rookietec9.enderplugin.API.esg.ESGPlayer;
import io.github.rookietec9.enderplugin.inventories.ESGInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * While technically existing before v7.1.5, I decided to re-brand the class due to the utter sexiness of the for loop.
 *
 * @author Jeremi
 * @version 11.6.0
 * @since 7.1.5
 */
public class ESGShopEvent implements Listener {

    @EventHandler
    public void open(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        if (!player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.ESG_HUB)) return;

        ItemStack inHand = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
        if (inHand == null) return;
        if (inHand.getType() == Material.DIAMOND && inHand.getItemMeta().getDisplayName().equalsIgnoreCase("shop")) {
            player.openInventory(ESGInventory.shopMenu);
        }
    }

    @EventHandler
    public void runClick(InventoryClickEvent e) {
        if (!e.getWhoClicked().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.ESG_HUB)) return;
        if (e.getClickedInventory().getName() == null) return;
        if (e.getClickedInventory().getName().equalsIgnoreCase("Choose Your Kit")) return;

        ESGPlayer player = new ESGPlayer((Player) e.getWhoClicked());
        Item buyMenu = new Item(Material.COAL, new String[]{"§7Upgrade the default kits."}, "§fDefault Kits", 1);
        Item defaultMenu = new Item(Material.DIAMOND, new String[]{"§7Upgrade/Buy the premium kits."},
                "§fPremium Kits", 1);

        if (e.getCurrentItem() == null) {
            e.setCancelled(true);
            return;
        }

        Material m = e.getCurrentItem().getType();

        if (e.getCurrentItem().getType().equals(Material.DIAMOND))
            e.getWhoClicked().openInventory(ESGInventory.shopMenu);

        if (e.getClickedInventory().getName().equalsIgnoreCase("§fE§6S§fG | Shop Menu")) {
            if (e.getCurrentItem().getType() == Material.COAL) {
                e.getWhoClicked().openInventory(ESGInventory.upgradeMenu);
                e.setCancelled(true);
            }

            if (e.getCurrentItem().getType() == Material.DIAMOND) {
                e.getWhoClicked().openInventory(ESGInventory.premiumMenu);
                e.setCancelled(true);
            }
        }

        if (e.getClickedInventory().getName().equalsIgnoreCase("§fE§6S§fG | Upgrade Default Kits")) {
            if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
            for (ESGKit esgKit : ESGKit.values()) {
                if (esgKit == ESGKit.SHELL) continue;
                if (!esgKit.getFree()) {
                    if (e.getCurrentItem().getType() == esgKit.getType()) {
                        e.setCancelled(true);
                        e.getWhoClicked().openInventory(classInv(esgKit, player));
                    }
                }
            }
            if (e.getCurrentItem().getData().getData() == 0 && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7GO back")) {
                e.setCancelled(true);
                e.getWhoClicked().openInventory(ESGInventory.shopMenu);
            }
        }
        if (e.getClickedInventory().getName().equalsIgnoreCase("§fE§6S§fG | Upgrade/Buy Premium Kits")) {
            for (ESGKit esgKit : ESGKit.values()) {
                if (esgKit == ESGKit.SHELL) continue;
                if (!esgKit.getFree()) {
                    if (e.getCurrentItem().getType() == esgKit.getType()) {
                        e.setCancelled(true);
                        e.getWhoClicked().openInventory(classInv(esgKit, player));
                    }
                }
            }
            e.setCancelled(true);
            if (e.getCurrentItem().getData().getData() == 0 && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7GO back")) {
                e.setCancelled(true);
                e.getWhoClicked().openInventory(ESGInventory.shopMenu);
            }
        }

        if (e.getClickedInventory().getName().contains("§fE§6S§fG | Kit: ")) {
            if (e.getCurrentItem().getType() == Material.WOOL) {
                if (e.getCurrentItem().getData().getData() == 5) {
                    ESGKit esgKit = ESGKit.from(e.getClickedInventory().getName().replace("§fE§6S§fG | Kit: ", ""));
                    if (esgKit.toUpgrade(player.getKitLevel(esgKit) + 1) <= player.getCoins()) {
                        player.setCoins(player.getCoins() - esgKit.toUpgrade(player.getKitLevel(esgKit) + 1));
                        player.setESGKitLeve(player.getKitLevel(esgKit) + 1, esgKit);
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage("§aUpgraded this kit");
                        //ESGMainBoard.run(new User(player.getBase()));
                    } else {
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage("§eYou couldn't afford that.");
                        e.setCancelled(true);
                    }
                }
                if (e.getCurrentItem().getData().getData() == 3) {
                    ESGKit esgKit = ESGKit.from(e.getClickedInventory().getName().replace("§fE§6S§fG | Kit: ", ""));
                    assert esgKit != null;
                    if (esgKit.getPrice() > player.getCoins()) {
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage("§eYou couldn't afford that.");
                    } else {
                        player.setCoins(player.getCoins() - esgKit.getPrice());
                        player.setESGKitLeve(player.getKitLevel(esgKit) + 1, esgKit);
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage("§eBought kit " + esgKit.getColor() + Utils.upSlash(esgKit.toString()));
                        //ESGMainBoard.run(new User(player.getBase()));
                        e.setCancelled(true);
                    }
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getData().getData() == 0 && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7GO back")) {
                    e.setCancelled(true);
                    ESGKit esgKit = ESGKit.from(e.getClickedInventory().getName().replace("§fE§6S§fG | Kit: ", "").replace(" ", "_"));
                    assert esgKit != null;
                    if (esgKit.getFree()) e.getWhoClicked().openInventory(ESGInventory.upgradeMenu);
                    if (!esgKit.getFree()) e.getWhoClicked().openInventory(ESGInventory.premiumMenu);
                }
            }
        }
    }


    @EventHandler /* Switch items. */
    public void dontDrop2(InventoryDragEvent e) {
        if (e.getInventory() == null)
            return;

        if (e.getInventory().getName().equalsIgnoreCase("§fE§6S§fG | Shop Menu")) {
            e.setCancelled(true);
        }
        if (e.getInventory().getName().equalsIgnoreCase("§fE§6S§fG | Upgrade Default Kits")) {
            e.setCancelled(true);
        }
        if (e.getInventory().getName().equalsIgnoreCase("§fE§6S§fG | Upgrade/Buy Premium Kits")) {
            e.setCancelled(true);
        }
        if (e.getInventory().getName().contains("§fE§6S§fG | Kit: "))
            e.setCancelled(true);
    }

    private Inventory classInv(ESGKit esgKit, ESGPlayer player) {
        int level = player.getKitLevel(esgKit);
        Inventory inv = Bukkit.createInventory(null, 45, "§fE§6S§fG | Kit: " + esgKit.getColor() + Utils.upSlash(esgKit.toString()));
        ItemStack item = ESGInventory.item(esgKit, player);
        inv.setItem(0, item);
        Item up = new Item("§aUpgrade this kit", Material.ACACIA_DOOR, (byte) 5);
        Item buy = new Item("§bBuy this kit", Material.ACACIA_DOOR, (byte) 3);
        Item chest1 = new Item("§7View level 1", Material.ACACIA_DOOR);
        Item chest2 = new Item("§7View level 2", Material.ACACIA_DOOR);
        Item chest3 = new Item("§7View level 3", Material.ACACIA_DOOR);
        Item chest4 = new Item("§7View level 4", Material.ACACIA_DOOR);
        Item chest5 = new Item("§7View level 5", Material.ACACIA_DOOR);
        Item chest6 = new Item("§7View level 6", Material.ACACIA_DOOR);
        Item chest7 = new Item("§7View level 7", Material.ACACIA_DOOR);
        Item chest8 = new Item("§7View level 8", Material.ACACIA_DOOR);
        Item chest9 = new Item("§7View level 9", Material.ACACIA_DOOR);
        Item chest0 = new Item("§7View level 10", Material.ACACIA_DOOR);
        Item back = new Item("§7GO back", Material.ACACIA_DOOR, (byte) 0);

        inv.setItem(0 + (0 * 9), item);

        if (level != 0) {
            inv.setItem(0 + (2 * 9), up.getItem());
        } else {
            inv.setItem(0 + (2 * 9), buy.getItem());
        }
        inv.setItem(0 + (4 * 9), back.getItem());

        inv.setItem(7 + (0 * 9), chest9.getItem());
        inv.setItem(8 + (0 * 9), chest0.getItem());

        inv.setItem(7 + (1 * 9), chest7.getItem());
        inv.setItem(8 + (1 * 9), chest8.getItem());

        inv.setItem(7 + (2 * 9), chest5.getItem());
        inv.setItem(8 + (2 * 9), chest6.getItem());

        inv.setItem(7 + (3 * 9), chest3.getItem());
        inv.setItem(8 + (3 * 9), chest4.getItem());

        inv.setItem(7 + (4 * 9), chest1.getItem());
        inv.setItem(8 + (4 * 9), chest2.getItem());

        return inv;
    }
}
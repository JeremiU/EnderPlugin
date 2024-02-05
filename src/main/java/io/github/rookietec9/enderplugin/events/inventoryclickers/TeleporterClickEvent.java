package io.github.rookietec9.enderplugin.events.inventoryclickers;

import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.2.0
 */
public class TeleporterClickEvent implements Listener {

    @EventHandler
    public void run(InventoryInteractEvent event) {
        if (event.getInventory().getName().equalsIgnoreCase(Inventories.TELEPORTER_MAIN.getName())) event.setCancelled(true);
    }

    @EventHandler
    public void run(PlayerInteractEvent event) {
        if (event.getMaterial() == Material.NETHER_STAR && event.getItem().getItemMeta().hasDisplayName() && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Game Selector")) event.getPlayer().openInventory(Inventories.TELEPORTER_MAIN);
    }

    @EventHandler
    public void run(InventoryClickEvent event) {

        if (event.getClickedInventory() == null || event.getClickedInventory().getName() == null || !Java.argWorks(event.getClickedInventory().getName(), Inventories.TELEPORTER_MAIN.getName(), Inventories.TELEPORTER_ESG.getName(), Inventories.TELEPORTER_ARCHIVED.getName())) return;
        event.setCancelled(true);

        Spawn spawn = null;
        if (event.getClickedInventory().getName().equalsIgnoreCase(Inventories.TELEPORTER_MAIN.getName())) {
            spawn = switch (event.getSlot()) {
                case 8 -> new Spawn(HUB);
                case 11 -> new Spawn(WIZARDS);
                case 12 -> new Spawn(OBSTACLE);
                case 13 -> new Spawn(SUMO);
                case 14 -> new Spawn(TNT_RUN);
                case 15 -> new Spawn(CTF);
                case 19 -> new Spawn(MURDER);
                case 20 -> new Spawn(KIT_PVP);
                case 21 -> new Spawn(PARKOUR);
                case 22 -> new Spawn(SPLEEF);
                case 23 -> new Spawn(HUNGER);
                case 24 -> new Spawn(BOOTY);
                default -> null;
            };

            if (spawn != null) event.getWhoClicked().teleport(spawn.location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            switch (event.getSlot()) {
                case 26 -> {
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().sendMessage(serverLang().getPlugMsg() + "We are currently not working on the arcade games, please come back later!");
                }
                case 35 -> event.getWhoClicked().openInventory(Inventories.TELEPORTER_ARCHIVED);
                case 10 -> event.getWhoClicked().openInventory(Inventories.TELEPORTER_ESG);
            }
        }

        if (event.getClickedInventory().getName().equalsIgnoreCase(Inventories.TELEPORTER_ARCHIVED.getName())) {
            switch (event.getSlot()) {
                case 2 -> spawn = new Spawn(OLD_ARROWS);
                case 3 -> spawn = new Spawn(OLD_RAIL_PVP);
                case 4 -> spawn = new Spawn(OLD_OBSTACLE);
                case 5 -> spawn = new Spawn(OLD_WIZARDS);
            }
            if (spawn != null) event.getWhoClicked().teleport(spawn.location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }

        if (event.getClickedInventory().getName().equalsIgnoreCase(Inventories.TELEPORTER_ESG.getName())) {
            switch (event.getSlot()) {
                case 2 -> event.getWhoClicked().teleport(new Location(Bukkit.getWorld(ESG_FIGHT), -62.5, 52, -132.5, 1.0f, 1.5f), PlayerTeleportEvent.TeleportCause.PLUGIN);
                case 4 -> event.getWhoClicked().teleport(new Spawn(ESG_FIGHT).location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                case 6 -> {
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().sendMessage(PREFIX_ESG + "We are currently not working on a new map, please come back later!");
                }
            }
        }
    }
}
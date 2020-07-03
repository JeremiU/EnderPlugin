package io.github.rookietec9.enderplugin.events.inventoryclickers;

import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
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

/**
 * @author Jeremi
 * @version 20.3.8
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
            switch (event.getSlot()) {
                case 8 -> spawn = new Spawn(Worlds.HUB);
                case 10 -> spawn = new Spawn(Worlds.HIDENSEEK);
                case 11 -> spawn = new Spawn(Worlds.WIZARDS);
                case 12 -> spawn = new Spawn(Worlds.OBSTACLE);
                case 13 -> spawn = new Spawn(Worlds.SUMO);
                case 14 -> spawn = new Spawn(Worlds.TNT_RUN);
                case 15 -> spawn = new Spawn(Worlds.CTF);
                case 19 -> spawn = new Spawn(Worlds.MURDER);
                case 20 -> spawn = new Spawn(Worlds.KIT_PVP);
                case 21 -> spawn = new Spawn(Worlds.PARKOUR);
                case 22 -> spawn = new Spawn(Worlds.SPLEEF);
                case 23 -> spawn = new Spawn(Worlds.HUNGER);
                case 24 -> spawn = new Spawn(Worlds.BOOTY);
            }

            if (spawn != null) event.getWhoClicked().teleport(spawn.location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            switch (event.getSlot()) {
                case 26 -> {
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().sendMessage(serverLang().getPlugMsg() + "We are currently not working on the arcade games, please come back later!");
                }
                case 35 -> event.getWhoClicked().openInventory(Inventories.TELEPORTER_ARCHIVED);
                case 17 -> event.getWhoClicked().openInventory(Inventories.TELEPORTER_ESG);
            }
        }

        if (event.getClickedInventory().getName().equalsIgnoreCase(Inventories.TELEPORTER_ARCHIVED.getName())) {
            switch (event.getSlot()) {
                case 2 -> spawn = new Spawn(Worlds.OLD_ARROWS);
                case 3 -> spawn = new Spawn(Worlds.OLD_RAIL_PVP);
                case 4 -> spawn = new Spawn(Worlds.OLD_OBSTACLE);
                case 5 -> spawn = new Spawn(Worlds.OLD_WIZARDS);
                case 6 -> spawn = new Spawn(Worlds.OLD_SKYWARS);
            }
            if (spawn != null) event.getWhoClicked().teleport(spawn.location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }

        if (event.getClickedInventory().getName().equalsIgnoreCase(Inventories.TELEPORTER_ESG.getName())) {
            switch (event.getSlot()) {
                case 2 -> event.getWhoClicked().teleport(new Location(Bukkit.getWorld(Worlds.ESG_FIGHT), -62.5, 52, -132.5, 1.0f, 1.5f), PlayerTeleportEvent.TeleportCause.PLUGIN);
                case 4 -> event.getWhoClicked().teleport(new Spawn(Worlds.ESG_FIGHT).location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                case 6 -> {
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().sendMessage(Prefixes.ESG + "We are currently not working on a new map, please come back later!");
                }
            }
        }
    }

}
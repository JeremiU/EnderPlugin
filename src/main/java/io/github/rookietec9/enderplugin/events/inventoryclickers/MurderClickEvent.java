package io.github.rookietec9.enderplugin.events.inventoryclickers;

import io.github.rookietec9.enderplugin.Inventories;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 23.1.4
 */
public class MurderClickEvent implements Listener {

    @EventHandler
    public void run(InventoryInteractEvent event) {
        if (event.getInventory().getName().equalsIgnoreCase(Inventories.MURDER_MAP.getName())) event.setCancelled(true);
    }

    @EventHandler
    public void run(InventoryClickEvent event) {
        if (!event.getInventory().getName().equalsIgnoreCase(Inventories.MURDER_MAP.getName())) return;
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 2 -> event.getWhoClicked().openInventory(Inventories.START_MURDER);
            case 6 -> event.getWhoClicked().openInventory(Inventories.START_HNS);
        }
    }
}
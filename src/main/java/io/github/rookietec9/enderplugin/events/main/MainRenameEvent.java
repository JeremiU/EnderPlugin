package io.github.rookietec9.enderplugin.events.main;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Renamss using & as a color code.
 *
 * @author Jeremi
 * @version 7.9.4
 * @since 7.9.3
 */
public class MainRenameEvent implements Listener {

    @EventHandler
    public void run(InventoryClickEvent event) {
        if (event.getInventory().getType() == null || event.getInventory().getType() != InventoryType.ANVIL)
            return;

        if (event.getSlotType() == InventoryType.SlotType.RESULT) {
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getItemMeta() != null) {
                    ItemMeta meta = event.getCurrentItem().getItemMeta();
                    if (meta.getDisplayName() != null || meta.getDisplayName().isEmpty()) {
                        String name = ChatColor.translateAlternateColorCodes('&', meta.getDisplayName());
                        meta.setDisplayName(name);
                        event.getCurrentItem().setItemMeta(meta);
                    }
                }
            }
        }
    }
}

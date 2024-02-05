package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.configs.associates.WorldInventory;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static io.github.rookietec9.enderplugin.Reference.HUB;

/**
 * @author Jeremi
 * @version 25.2.6
 * @since 21.2.1
 */
public class WorldInventoryEvent implements Listener {

    @EventHandler
    public void run(PlayerChangedWorldEvent event) {
        WorldInventory invConfig = new WorldInventory(event.getPlayer());
        invConfig.setInventory(event.getPlayer().getInventory(), event.getFrom());

        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(HUB)) {
            DataPlayer.get(event.getPlayer()).hubNoTp();
            return;
        } else event.getPlayer().setAllowFlight(false);

        DataPlayer.get(event.getPlayer()).clear();

        for (int i = 0; i < invConfig.inventoryContents(event.getPlayer().getWorld()).size(); i++) {
            if (invConfig.inventoryContents(event.getPlayer().getWorld()).get(i) == null) continue;
            event.getPlayer().getInventory().setItem(i, invConfig.inventoryContents(event.getPlayer().getWorld()).get(i));
        }

        if (invConfig.armorContents(event.getPlayer().getWorld()).size() > 0 && invConfig.armorContents(event.getPlayer().getWorld()).get(0) != null) event.getPlayer().getInventory().setBoots(invConfig.armorContents(event.getPlayer().getWorld()).get(0));
        if (invConfig.armorContents(event.getPlayer().getWorld()).size() > 1 && invConfig.armorContents(event.getPlayer().getWorld()).get(1) != null) event.getPlayer().getInventory().setLeggings(invConfig.armorContents(event.getPlayer().getWorld()).get(1));
        if (invConfig.armorContents(event.getPlayer().getWorld()).size() > 2 && invConfig.armorContents(event.getPlayer().getWorld()).get(2) != null) event.getPlayer().getInventory().setChestplate(invConfig.armorContents(event.getPlayer().getWorld()).get(2));
        if (invConfig.armorContents(event.getPlayer().getWorld()).size() > 3 && invConfig.armorContents(event.getPlayer().getWorld()).get(3) != null) event.getPlayer().getInventory().setHelmet(invConfig.armorContents(event.getPlayer().getWorld()).get(3));
    }
}
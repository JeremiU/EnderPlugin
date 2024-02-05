package io.github.rookietec9.enderplugin.events.main;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * @author Jeremi
 * @version 25.1.9
 * @since 25.1.9
 */
public class WorldEditEvent implements Listener {

    @EventHandler
    public void run(BlockBreakEvent event) {
        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == Material.WOOD_AXE) event.setCancelled(true);
    }
}
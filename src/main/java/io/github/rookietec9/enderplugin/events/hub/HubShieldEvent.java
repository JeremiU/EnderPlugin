package io.github.rookietec9.enderplugin.events.hub;

import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import static io.github.rookietec9.enderplugin.Reference.HUB;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 7.8.1
 */
public class HubShieldEvent implements Listener {

    @EventHandler
    public void destroy(BlockBreakEvent e) {
        if (e.getBlock().getWorld().getName().equalsIgnoreCase(HUB) && e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled(!EnderPlugin.getInstance().getConfig().getBoolean("hubEdit"));
    }

    @EventHandler
    public void create(BlockPlaceEvent e) {
        if (e.getBlock().getWorld().getName().equalsIgnoreCase(HUB) && e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled(!EnderPlugin.getInstance().getConfig().getBoolean("hubEdit"));
    }
}
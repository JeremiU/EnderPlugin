package io.github.rookietec9.enderplugin.events.hub;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * @author Jeremi
 * @version 18.5.8
 * @since 7.8.1
 */
public class HubShieldEvent implements Listener {

    @EventHandler
    public void destroy(BlockBreakEvent e) {
        if (e.getBlock().getWorld().getName().equalsIgnoreCase(Worlds.HUB) && e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled(!EnderPlugin.getInstance().getConfig().getBoolean("hubEdit"));
    }

    @EventHandler
    public void create(BlockPlaceEvent e) {
        if (e.getBlock().getWorld().getName().equalsIgnoreCase(Worlds.HUB) && e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled(!EnderPlugin.getInstance().getConfig().getBoolean("hubEdit"));
    }
}
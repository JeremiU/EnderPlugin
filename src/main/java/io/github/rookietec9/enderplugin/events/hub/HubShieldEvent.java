package io.github.rookietec9.enderplugin.events.hub;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 7.8.1
 */
public class HubShieldEvent implements Listener {


    @EventHandler
    public void destroy(BlockBreakEvent e) {
        if (e.getBlock().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUB))
            e.setCancelled(!EnderPlugin.getInstance().getConfig().getBoolean("hubEdit"));
    }

    @EventHandler
    public void create(BlockPlaceEvent e) {
        if (e.getBlock().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUB))
            e.setCancelled(!EnderPlugin.getInstance().getConfig().getBoolean("hubEdit"));
    }
}
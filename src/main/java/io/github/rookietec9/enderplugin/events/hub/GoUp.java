package io.github.rookietec9.enderplugin.events.hub;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 9.1.9
 */
public class GoUp implements Listener {

    @EventHandler
    public void runEvent(PlayerMoveEvent event) {
        if (event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()) return;
        if (event.getTo().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUB)) {
            Location blockOnBot = event.getTo().clone();
            blockOnBot.setY(blockOnBot.getY() - 1);
            if (blockOnBot.getBlock().getType() == Material.WOOD) {
                if (blockOnBot.getBlock().getData() == (byte) 1) {
                    event.getPlayer().teleport(blockOnBot.getWorld().getHighestBlockAt(blockOnBot).getLocation());
                }
            }
        }
    }
}

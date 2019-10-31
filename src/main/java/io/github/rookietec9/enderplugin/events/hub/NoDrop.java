package io.github.rookietec9.enderplugin.events.hub;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 9.6.4
 */
public class NoDrop implements Listener {

    @EventHandler
    public void runEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUB)) player.setCanPickupItems(true);
        if (player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUB)) player.setCanPickupItems(false);
    }
}

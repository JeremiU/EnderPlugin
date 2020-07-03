package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author Jeremi
 * @version 21.2.2
 * @since 21.0.7
 */
public class TeleportationEvent implements Listener {

    @EventHandler
    public void run(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.SPECTATE) {
            DataPlayer.get(event.getPlayer()).addBackLoc(event.getFrom());
            DataPlayer.get(event.getPlayer()).teleportIndex = DataPlayer.get(event.getPlayer()).teleportationList.size() - 1;
        }
    }
}
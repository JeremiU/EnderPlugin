package io.github.rookietec9.enderplugin.events.games.booty;

import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author Jeremi
 * @version 22.4.3
 * @since 2.9.5
 */
public class BootyClick implements Listener {

    @EventHandler
    public void run(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if ((p.getGameMode() == null) || (e.getItem() == null) || (e.getClickedBlock() == null) || (p.getWorld() == null)) return;

        if ((p.getWorld().getName().equalsIgnoreCase(Worlds.BOOTY)) && (e.getClickedBlock().getType() == Material.COAL_BLOCK) && p.getGameMode() == GameMode.ADVENTURE) {
            if (e.getItem().getType() == Material.IRON_SWORD) e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), p.getLocation().getX(), 18, e.getPlayer().getLocation().getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);
            if (e.getItem().getType() == Material.BOW) {
                if (p.getHealthScale() != 20) p.setHealthScale(20);
                if (p.getHealth() != p.getHealthScale()) p.setHealth((p.getHealth() + 1 <= 20) ? (p.getHealth() + 1) : 20);
            }
        }
    }
}
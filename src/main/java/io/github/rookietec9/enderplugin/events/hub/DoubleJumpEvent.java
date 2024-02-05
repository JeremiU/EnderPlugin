package io.github.rookietec9.enderplugin.events.hub;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import static io.github.rookietec9.enderplugin.Reference.HUB;

/**
 * @author Jeremi
 * @version 25.2.6
 * @since 10.5.0
 */
public class DoubleJumpEvent implements Listener {

    @EventHandler
    public void onPlayerWalk(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.CREATIVE || !p.getWorld().getName().equalsIgnoreCase(HUB)) return;
        if ((p.getGameMode() != GameMode.CREATIVE) && (p.isOnGround())) p.setAllowFlight(true);
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent e) {
        if (e.getEntity().getWorld().getName().equalsIgnoreCase(HUB) && (e.getEntity() instanceof Player) && e.getCause() == EntityDamageEvent.DamageCause.FALL) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.CREATIVE || !p.getWorld().getName().equalsIgnoreCase(HUB)) return;
        e.setCancelled(true);
        p.setAllowFlight(false);
        p.setFlying(false);
        p.setVelocity(new Vector(p.getLocation().getX(), 1.0D, p.getLocation().getZ()));
        p.setVelocity(p.getLocation().getDirection());
    }
}
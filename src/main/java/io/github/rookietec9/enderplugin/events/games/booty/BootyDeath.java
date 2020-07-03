package io.github.rookietec9.enderplugin.events.games.booty;

import io.github.rookietec9.enderplugin.events.main.MainDeathEvent;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Jeremi
 * @version 22.5.4
 * @since 14.5.1
 */
public class BootyDeath implements Listener {

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(Worlds.BOOTY) || event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (event.getFrom().getBlock().getType() != Material.WATER && event.getFrom().getBlock().getType() != Material.STATIONARY_WATER) return;
        if (!Java.isInRange(event.getTo().getBlockX(), 34, 81) || !Java.isInRange(event.getTo().getBlockZ(), -37, -84)) return;
        if (event.getPlayer().getLastDamageCause() != null && event.getPlayer().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event.getPlayer().getLastDamageCause();
            if (entityEvent.getDamager() instanceof Player) MainDeathEvent.fullCheck(event.getPlayer(), (Player) entityEvent.getDamager());
        } else MainDeathEvent.fullCheck(event.getPlayer(), null);
    }

    @EventHandler
    public void run(EntityDamageEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER || !event.getEntity().getWorld().getName().equalsIgnoreCase(Worlds.BOOTY)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.WITHER && event.getEntity().getLastDamageCause() != null) event.getEntity().setLastDamageCause(new EntityDamageByEntityEvent(event.getEntity().getLastDamageCause().getEntity(), event.getEntity(), event.getCause(), 10));
    }
}
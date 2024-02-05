package io.github.rookietec9.enderplugin.events.games.booty;

import io.github.rookietec9.enderplugin.events.main.MainDeathEvent;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Teams;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static io.github.rookietec9.enderplugin.Reference.BOOTY;

/**
 * @author Jeremi
 * @version 25.6.5
 * @since 14.5.1
 */
public class BootyDeathEvent implements Listener {

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(BOOTY) || event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (event.getFrom().getBlock().getType() != Material.WATER && event.getFrom().getBlock().getType() != Material.STATIONARY_WATER) return;
        if (!Java.isInRange(event.getTo().getBlockX(), 33, 82) || !Java.isInRange(event.getTo().getBlockZ(), -36, -85)) return;
        if (event.getPlayer().getLastDamageCause() != null && event.getPlayer().getLastDamageCause() instanceof EntityDamageByEntityEvent) {

            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event.getPlayer().getLastDamageCause();
            if (entityEvent.getDamager() instanceof Player) {
                MainDeathEvent.fullCheck(event.getPlayer(), (Player) entityEvent.getDamager());
                return;
            }
            if (entityEvent.getDamager() instanceof Arrow && ((Arrow) entityEvent.getDamager()).getShooter() instanceof Player) {
                MainDeathEvent.fullCheck(event.getPlayer(), (Player) ((Arrow) entityEvent.getDamager()).getShooter());
                return;
            }
            System.out.println("BOOTYDEATH > dmg event found but no killer found!");
        } else MainDeathEvent.fullCheck(event.getPlayer(), null);
    }

    @EventHandler
    public void run(EntityDamageEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER || !event.getEntity().getWorld().getName().equalsIgnoreCase(BOOTY)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.WITHER && event.getEntity().getLastDamageCause() != null) event.getEntity().setLastDamageCause(new EntityDamageByEntityEvent(event.getEntity().getLastDamageCause().getEntity(), event.getEntity(), event.getCause(), 10));
    }

    @EventHandler
    public void run(EntityDamageByEntityEvent event) {
        if (!event.getEntity().getWorld().getName().equalsIgnoreCase(BOOTY) || event.getEntity().getType() != EntityType.PLAYER || event.getDamager().getType() != EntityType.PLAYER) return;
        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        if (Teams.getTeam(damager).equals(Teams.getTeam(damaged))) event.setCancelled(true);
    }
}
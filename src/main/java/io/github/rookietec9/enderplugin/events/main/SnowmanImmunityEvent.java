package io.github.rookietec9.enderplugin.events.main;

import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author Jeremi
 * @version 17.8.8
 * @since 8.8.4
 */
public class SnowmanImmunityEvent implements Listener {

    @EventHandler
    public void onClick(EntityDamageEvent e) {
        if (e.getEntity() instanceof Snowman && e.getCause() == EntityDamageEvent.DamageCause.MELTING) e.setCancelled(true);
    }
}
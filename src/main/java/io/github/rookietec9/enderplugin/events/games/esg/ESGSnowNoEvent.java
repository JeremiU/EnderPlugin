package io.github.rookietec9.enderplugin.events.games.esg;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 8.8.4
 */
public class ESGSnowNoEvent implements Listener {

    @EventHandler
    public void onClick(EntityDamageEvent e) {
        if (e.getEntity().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.ESG_FIGHT) || e.getEntity().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUNGER)) {
            if (e.getEntity() instanceof Snowman) {
                if (e.getCause() == EntityDamageEvent.DamageCause.MELTING) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
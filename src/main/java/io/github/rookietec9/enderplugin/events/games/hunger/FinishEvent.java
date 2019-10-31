package io.github.rookietec9.enderplugin.events.games.hunger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author Jeremi
 */
public class FinishEvent {

    public void run(EntityDamageEvent deathEvent) {
        if (deathEvent instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) deathEvent;
            //event.get
        } else
        if (deathEvent instanceof EntityDamageByBlockEvent) {

        }
    }
}

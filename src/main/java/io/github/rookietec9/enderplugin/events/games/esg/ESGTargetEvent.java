package io.github.rookietec9.enderplugin.events.games.esg;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 8.8.4
 */
public class ESGTargetEvent implements Listener {

    @EventHandler
    public void changeTarget(EntityTargetLivingEntityEvent event) {
        LivingEntity targ = event.getTarget();
        LivingEntity ent;

        if (event.getEntity() instanceof LivingEntity)
            ent = (LivingEntity) event.getEntity();
        else return;

        if (ent.getCustomName() == null) return;

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (ent.getCustomName().equalsIgnoreCase(p.getUniqueId().toString())) {
                if (p == targ) {
                    if (targ.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent dmgEvent = (EntityDamageByEntityEvent) targ.getLastDamageCause();
                        if (dmgEvent.getDamager() instanceof LivingEntity) {
                            if (dmgEvent.getDamager() != null)
                                event.setTarget(dmgEvent.getDamager());
                            else event.setTarget(null);
                        }
                    }
                    targ.getLastDamageCause();
                }
            }
        }
    }
}

package io.github.rookietec9.enderplugin.events.games.esg;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 8.8.4
 */
public class ESGTargetHitEvent implements Listener {

    @EventHandler
    public void changeTarget(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player targ = (Player) event.getEntity();

        if (!(event.getDamager() instanceof LivingEntity)) return;

        if (event.getDamager() != null) {
            for (LivingEntity ent : Bukkit.getWorld(Utils.Reference.Worlds.ESG_FIGHT).getLivingEntities()) {
                if (ent.getCustomName() == null) return;
                if (ent.getCustomName().equalsIgnoreCase(targ.getUniqueId().toString())) {
                    Bukkit.getServer().getPluginManager().callEvent(new EntityTargetLivingEntityEvent(ent, (LivingEntity) event.getDamager(), EntityTargetEvent.TargetReason.CUSTOM));
                }
            }
        }
    }
}
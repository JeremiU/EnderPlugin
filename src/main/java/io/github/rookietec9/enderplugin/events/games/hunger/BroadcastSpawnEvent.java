package io.github.rookietec9.enderplugin.events.games.hunger;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 8.5.3
 */
public class BroadcastSpawnEvent implements Listener {

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (!event.getEntity().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUNGER)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        Bukkit.broadcastMessage("§4Hunger §c> §7a§f[§7n§f] §c" + Utils.upSlash(event.getEntityType().toString()).toUpperCase() + " §7Spawned!");
    }
}
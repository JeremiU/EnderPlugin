package io.github.rookietec9.enderplugin.events.games.booty;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Jeremi
 * @version 14.8.1
 * @since 14.5.1
 */
public class BootyDeath implements Listener {

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.BOOTY)) return;
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;

        if (event.getFrom().getBlock().getType() == Material.WATER || event.getFrom().getBlock().getType() == Material.STATIONARY_WATER)
            return;

        if (event.getTo().getBlock().getType() == Material.WATER || event.getTo().getBlock().getType() == Material.STATIONARY_WATER) {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, (30) * 20, 3));
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (30) * 20, 3));
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (30) * 20, 3));
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (30) * 20, 3));
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, (30) * 20, 14));


            if (!(81 > event.getTo().getBlockX() && event.getTo().getBlockX() > 34)) return;
            if (!(-37 > event.getTo().getBlockZ() && event.getTo().getBlockZ() > -84)) return;
            LivingEntity guardian = null;

            for (LivingEntity entity : event.getPlayer().getWorld().getLivingEntities()) {
                if (!(81 > entity.getLocation().getBlockX() && entity.getLocation().getBlockX() > 34)) continue;
                if (!(-37 > entity.getLocation().getBlockZ() && entity.getLocation().getBlockZ() > -84)) continue;

                if (entity.getType() == EntityType.GUARDIAN) {
                    guardian = entity;
                    for (Entity entity1 : guardian.getNearbyEntities(10, 5, 10)) {
                        if (entity1.getType() == EntityType.PLAYER && ((Player) entity1).getGameMode() == GameMode.ADVENTURE && (entity1.getLocation().getBlock().getType() == Material.WATER || entity1.getLocation().getBlock().getType() == Material.STATIONARY_WATER)) {
                            LivingEntity entity2 = (LivingEntity) event.getPlayer().getWorld().spawnEntity(entity1.getLocation(), EntityType.GUARDIAN);
                            entity2.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (500 * 20), 10));
                            entity2.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (500 * 20), 10));
                        }
                    }
                }
            }

            if (guardian == null) {
                guardian = (LivingEntity) event.getPlayer().getWorld().spawnEntity(event.getTo(), EntityType.GUARDIAN);
            } else guardian.teleport(event.getTo());
            guardian.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (500 * 20), 10));
            guardian.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (500 * 20), 10));
        }
    }
}
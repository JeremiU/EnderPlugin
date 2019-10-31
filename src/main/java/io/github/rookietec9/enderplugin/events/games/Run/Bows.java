package io.github.rookietec9.enderplugin.events.games.Run;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Jeremi
 * @since 11.3.4
 * @version 11.6.4
 */
public class Bows implements Listener {

    @EventHandler
    public void run(EntityShootBowEvent event) {
        ItemStack itemStack = event.getBow();
        LivingEntity entity = event.getEntity();
        Location location = entity.getLocation().clone();
        location.setY(location.getY() + 2);

        if (!itemStack.getItemMeta().hasDisplayName()) return;
        if (ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).equalsIgnoreCase("Intimidator Bow")) {
            if (!(event.getProjectile().getNearbyEntities(5,2,5).isEmpty())) {
                for (Entity entity1 : event.getProjectile().getNearbyEntities(5,2,5)) {
                    if (entity1 instanceof Player) {
                        if (event.getEntity() == entity1) return;
                        ((Player) entity1).setHealth(((Player) entity1).getHealth() - 3);
                    }
                }
            }
        }
    }
}
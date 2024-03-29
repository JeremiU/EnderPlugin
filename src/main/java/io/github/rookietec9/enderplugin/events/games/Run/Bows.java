package io.github.rookietec9.enderplugin.events.games.run;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import static io.github.rookietec9.enderplugin.Reference.SWAG_RUN;

/**
 * @author Jeremi
 * @version 25.6.5
 * @since 11.3.4
 */
public class Bows implements Listener { //prob don't work

    @EventHandler
    public void run(EntityShootBowEvent event) {
        if (!event.getEntity().getWorld().getName().equalsIgnoreCase(SWAG_RUN)) return;

        ItemStack itemStack = event.getBow();
        LivingEntity entity = event.getEntity();
        Location location = entity.getLocation().clone();
        location.setY(location.getY() + 2);

        if (itemStack == null) return;

        if (!itemStack.getItemMeta().hasDisplayName()) return;
        if (ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).equalsIgnoreCase("Intimidator Bow")) {
            if (!(event.getProjectile().getNearbyEntities(5, 2, 5).isEmpty())) {
                for (Entity entity1 : event.getProjectile().getNearbyEntities(5, 2, 5)) {
                    if (entity1 instanceof Player && event.getEntity() != entity1) {
                        ((Player) entity1).setHealth(((Player) entity1).getHealth() - 3);
                    }
                }
            }
        }
    }
}
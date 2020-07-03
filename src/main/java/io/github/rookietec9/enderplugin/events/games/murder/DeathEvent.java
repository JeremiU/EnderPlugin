package io.github.rookietec9.enderplugin.events.games.murder;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.Scheduler;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Jeremi
 * @version 20.4.8
 * @since ?.?.?
 */
public class DeathEvent implements Listener {

    @EventHandler
    public void run(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !event.getEntity().getWorld().getName().equalsIgnoreCase(Worlds.MURDER) || !(event.getDamager() instanceof Player)) return;
        event.setCancelled(true);

        Player player = (Player) event.getDamager();
        ItemStack itemStack = player.getItemInHand();

        if (itemStack != null && itemStack.getType() == Material.STONE_SWORD && (EnderPlugin.scheduler().isRunning("MURDER_TICK"))) EnderPlugin.murderBase.catchPlayer(event);
    }
}
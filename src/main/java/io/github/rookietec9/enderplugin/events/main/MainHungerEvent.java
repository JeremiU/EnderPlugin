package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Checks if the player does not have the hunger effect saturate their hunger.
 *
 * @author Jeremi
 * @version 18.5.8
 * @since 3.8.1
 */
public class MainHungerEvent implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void run(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase(Worlds.HUNGER) || player.getWorld().getName().equalsIgnoreCase(Worlds.ESG_FIGHT)) return;
        if (player.getFoodLevel() < 20) {
            for (PotionEffect p : player.getActivePotionEffects()) if (p.getType() == PotionEffectType.HUNGER) return;
            event.getPlayer().setFoodLevel(20);
            event.getPlayer().setSaturation(20);
        }
    }

}
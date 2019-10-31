package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.API.Utils;
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
 * @version 11.6.0
 * @since 3.8.1
 */
public class MainHungerEvent implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUNGER)
                || player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.ESG_FIGHT)
                || player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.CTF))
            return;
        if (player.getFoodLevel() < 19) {
            for (PotionEffect p : player.getActivePotionEffects()) {
                if (p.getType() == PotionEffectType.HUNGER) {
                    return;
                }
            }
            event.getPlayer().setFoodLevel(20);
            event.getPlayer().setSaturation(20.0F);
        }
    }
}
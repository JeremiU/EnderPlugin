package io.github.rookietec9.enderplugin.events.games.tnt;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since
 */
public class TNTEffectEvent implements Listener {

    @EventHandler
    public void run(PlayerMoveEvent e) {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.TNT_RUN)) {
            if (e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 2));
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 0));
            }
        } else {
        }
    }
}

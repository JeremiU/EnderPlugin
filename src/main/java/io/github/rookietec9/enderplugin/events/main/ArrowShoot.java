package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 9.0.7
 */
public class ArrowShoot implements Listener {

    @EventHandler
    public void runEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow shotArrow = (Arrow) event.getDamager();
            if (shotArrow.getShooter() instanceof Player && event.getEntity() instanceof Player) {
                if (new User((Player) event.getEntity()).getGod()) return;
                Player shooter = (Player) shotArrow.getShooter();
                Lang l = new Lang(Langs.fromSender(shooter));
                shooter.sendMessage(l.getTxtColor() + "You damaged " + l.getDarkColor() + event.getEntity().getName() + l.getTxtColor() + " for " + l.getDarkColor() + String.format("%.2f",
                        (event.getFinalDamage())));
                shooter.sendMessage(l.getDarkColor() + event.getEntity().getName() + l.getTxtColor() + " is on " + l.getDarkColor() + String.format("%.2f", ((Player) event.getEntity()).getHealth()
                        - event.getFinalDamage()) + " health");
            }
        }
    }
}

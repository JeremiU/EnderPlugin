package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 23.8.3
 * @since 9.0.7
 */
public class ArrowShootEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void runEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow && event.getEntity() instanceof Player)) return;
        Arrow shotArrow = (Arrow) event.getDamager();
        Player attacked = (Player) event.getEntity();
        if (!(shotArrow.getShooter() instanceof Player) || DataPlayer.getUser(attacked).getGod()) return;
        Player shooter = (Player) shotArrow.getShooter();

        if (attacked.getHealth() - event.getFinalDamage() < 1) {
            shooter.sendMessage(serverLang().getTxtColor() + "You killed " + serverLang().getDarkColor() + DataPlayer.getUser((Player) event.getEntity()).getNickName() + serverLang().getTxtColor() + ".");
            event.setCancelled(true);
            MainDeathEvent.fullCheck(attacked, shooter);
            return;
        }

        shooter.sendMessage(serverLang().getTxtColor() + "You damaged " + serverLang().getDarkColor() + DataPlayer.getUser((Player) event.getEntity()).getNickName() + serverLang().getTxtColor() + " for " + serverLang().getDarkColor() + String.format("%.2f", (event.getFinalDamage())));
        shooter.sendMessage(serverLang().getDarkColor() + DataPlayer.getUser((Player) event.getEntity()).getNickName() + serverLang().getTxtColor() + " is on " + serverLang().getDarkColor() + String.format("%.2f", ((Player) event.getEntity()).getHealth() - event.getFinalDamage()) + " health");
    }
}
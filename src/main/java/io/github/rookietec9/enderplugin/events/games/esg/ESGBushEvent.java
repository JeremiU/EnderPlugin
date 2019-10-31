package io.github.rookietec9.enderplugin.events.games.esg;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * EventHandler was added on 3.5.5. I'm extremely dumb.
 *
 * @author Jeremi
 * @version 11.6.0
 * @since 3.2.7
 */
public class ESGBushEvent implements Listener {

    @EventHandler
    public void run(EntityDamageByEntityEvent e) {

        if (e.getDamager().getType() != EntityType.PLAYER) {
            return;
        }
        if (e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        Player p = (Player) e.getEntity();
        Player d = (Player) e.getDamager();
        if (!d.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.ESG_FIGHT)) return;


        if (p.getGameMode() != GameMode.ADVENTURE || d.getGameMode() != GameMode.ADVENTURE)
            return;
        if (d.getInventory().getItem(d.getInventory().getHeldItemSlot()) != null && d.getInventory().getItem(d.getInventory().getHeldItemSlot()).getType() != Material.DEAD_BUSH) {
            return;
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (2 * 20), 1, true, true));
    }
}
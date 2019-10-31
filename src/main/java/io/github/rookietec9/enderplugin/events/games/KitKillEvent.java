package io.github.rookietec9.enderplugin.events.games;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.rmi.CORBA.Util;

/**
 * Gives regen for booty and KitPVP players (the ones who kill).
 *
 * @author Jeremi
 * @version 11.6.0
 * @since 8.0.1
 */
public class KitKillEvent implements Listener {

    @EventHandler
    public void run(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player dead = (Player) e.getEntity();

        if (dead == null) return;
        if (dead.getKiller() == null) return;
        if (dead.getHealth() <= 0) {
            Bukkit.broadcastMessage(new User(dead).getTabName() + " was killed by " + new User(dead.getKiller()).getTabName());

            if (dead.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.KIT_PVP) || e.getEntity().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.BOOTY)
                    || e.getEntity().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.WIZARDS)) {
                dead.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (5 * 20), 1, false, true));
            }

            e.setCancelled(true);
            dead.setHealthScale(20);
            dead.setHealth(20);
            for (int i = 0; i < dead.getInventory().getContents().length; i++) {
                dead.getLocation().getWorld().dropItem(dead.getLocation(), dead.getInventory().getContents()[i]);
                dead.getInventory().getContents()[i].setType(Material.AIR);
            }
            dead.updateInventory();
            dead.teleport(dead.getLocation().getWorld().getSpawnLocation());
            dead.sendTitle("§c WOAAAH","§4Looks like you died buddy!");
        }
    }
}
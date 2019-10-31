package io.github.rookietec9.enderplugin.events.games.booty;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Jeremi
 * @version 14.7.4
 * @since 2.9.5
 */
public class BootyClick implements Listener {

    @EventHandler
    public void run(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if ((p.getGameMode() == null) || (e.getItem() == null) || (e.getClickedBlock() == null) || (p.getWorld() == null)) {
            return;
        }
        if ((p.getWorld().equals(Bukkit.getWorld(Utils.Reference.Worlds.BOOTY)))
                && (e.getClickedBlock().getType().equals(Material.COAL_BLOCK))
                && (p.getGameMode().equals(GameMode.ADVENTURE))) {
            if (e.getItem().getType().equals(Material.IRON_SWORD))
                e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), p.getLocation().getX(), 18, e.getPlayer().getLocation().getZ()));
            if (e.getItem().getType().equals(Material.BOW)) {
                if (p.getHealthScale() != 20.0D) {
                    p.setHealthScale(20.0D);
                    p.setHealth(1.0D);
                }
                p.setHealthScale(20.0D);
                if (p.getHealth() != p.getHealthScale() && p.getHealth() < 20) {
                    p.setHealth(p.getHealth() + 1.0D);
                }
            }
        }
    }
}
package io.github.rookietec9.enderplugin.events.games.tnt;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since
 */
public class TNTBreakEvent implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void run(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        BukkitScheduler sch = Bukkit.getScheduler();
        if (p.getGameMode().equals(GameMode.ADVENTURE) && p.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.TNT_RUN)) {
            Location l = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1,
                    p.getLocation().getZ());
            if (l.getBlock().getType() == Material.STAINED_CLAY) {
                // WHITE
                if (l.getBlock().getData() == (byte) 0) {
                    sch.scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> l.getBlock().setType(Material.AIR), 20L);
                }
                // BLUE
                if (l.getBlock().getData() == (byte) 3) {
                    sch.scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        l.getBlock().setType(Material.STAINED_CLAY);
                        l.getBlock().setData((byte) 0);
                    }, 20L);
                }
                // ORANGE
                if (l.getBlock().getData() == (byte) 1) {
                    sch.scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        l.getBlock().setType(Material.STAINED_CLAY);
                        l.getBlock().setData((byte) 0);
                    }, 20L);
                }
            }
        }
    }
}
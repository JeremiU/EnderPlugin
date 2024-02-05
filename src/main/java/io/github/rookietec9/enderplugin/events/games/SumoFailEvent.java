package io.github.rookietec9.enderplugin.events.games;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static io.github.rookietec9.enderplugin.Reference.PREFIX_SUMO;
import static io.github.rookietec9.enderplugin.Reference.SUMO;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since
 */
public class SumoFailEvent implements Listener {

    @EventHandler
    public void run(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getWorld() != Bukkit.getWorld(SUMO) || e.getTo().getY() == e.getFrom().getY() || e.getTo().getY() > e.getFrom().getY()) return;

        if (Bukkit.getWorld(SUMO).getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()).getType() == Material.ENDER_STONE) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!(p.getWorld().getName().equalsIgnoreCase(SUMO)) || !p.getGameMode().equals(GameMode.ADVENTURE)) continue;
                p.sendMessage(PREFIX_SUMO + DataPlayer.getUser(player).getTabName() + " lost the sumo game.");
                player.teleport(new Location(player.getWorld(), 325, 5, -360.5), TeleportCause.PLUGIN);
            }
        }
    }
}
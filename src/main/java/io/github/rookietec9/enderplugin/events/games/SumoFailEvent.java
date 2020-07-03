package io.github.rookietec9.enderplugin.events.games;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author Jeremi
 * @version 22.4.9
 * @since
 */
public class SumoFailEvent implements Listener {

    @EventHandler
    public void run(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getWorld() != Bukkit.getWorld(Worlds.SUMO) || e.getTo().getY() == e.getFrom().getY() || e.getTo().getY() > e.getFrom().getY()) return;

        if (Bukkit.getWorld(Worlds.SUMO).getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()).getType() == Material.ENDER_STONE) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!(p.getWorld().getName().equalsIgnoreCase(Worlds.SUMO)) || !p.getGameMode().equals(GameMode.ADVENTURE)) continue;
                p.sendMessage(Minecraft.tacc("&f[&bSumo&f] " + DataPlayer.getUser(player).getTabName() + " lost the sumo game."));
                player.teleport(new Location(player.getWorld(), 325, 5, -360.5), PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
    }
}
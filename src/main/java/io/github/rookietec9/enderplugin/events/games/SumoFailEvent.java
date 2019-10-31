package io.github.rookietec9.enderplugin.events.games;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since
 */
public class SumoFailEvent implements Listener {

    @EventHandler
    public void run(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (loc(player).getWorld() != Bukkit.getWorld(Utils.Reference.Worlds.SUMO)) {
            return;
        }

        if (e.getTo().getY() == e.getFrom().getY()) return;
        if (e.getTo().getY() > e.getFrom().getY()) return;

        if (Bukkit.getWorld(Utils.Reference.Worlds.SUMO).getBlockAt(loc(player).getBlockX(), loc(player).getBlockY() - 1, loc(player).getBlockZ()).getType().equals(Material.ENDER_STONE)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!(p.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.SUMO)) || !p.getGameMode().equals(GameMode.ADVENTURE))
                    continue;
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&bSumo&f] " + new User(player).getTabName() + " lost the sumo game."));
                player.teleport(new Location(loc(player).getWorld(), 325, 5, -360.5));
            }
        }
    }

    private Location loc(Player p) {
        return p.getLocation();
    }
}
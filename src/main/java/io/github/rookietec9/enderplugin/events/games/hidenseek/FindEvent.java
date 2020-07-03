package io.github.rookietec9.enderplugin.events.games.hidenseek;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 16.4.2
 */
public class FindEvent implements Listener {

    @EventHandler
    public void run(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.ADVENTURE || !player.getWorld().getName().equalsIgnoreCase(Worlds.HIDENSEEK) || !Teams.contains(Teams.badTeam, player)) return;
        for (Entity entity : event.getPlayer().getNearbyEntities(20, 20, 20)) if (entity instanceof Player) DataPlayer.get((Player) entity).sendActionMsg(Prefixes.HIDENNSEEK + "The seeker is near you.");
    }

    @EventHandler
    public void run(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !event.getEntity().getWorld().getName().equalsIgnoreCase(Worlds.HIDENSEEK) || !(event.getDamager() instanceof Player)) return;
        EnderPlugin.hoodBase.catchPlayer(event);
    }
}
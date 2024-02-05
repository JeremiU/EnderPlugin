package io.github.rookietec9.enderplugin.events.games.obst;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static io.github.rookietec9.enderplugin.Reference.OBSTACLE;
import static io.github.rookietec9.enderplugin.Reference.PREFIX_OBS;

/**
 * @author Jeremi
 * @version 25.6.2
 * @since 25.6.1
 */
public class ObstFallEvent implements Listener {

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getTo().getWorld().getName().equalsIgnoreCase(OBSTACLE)) return;
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        Location whiteLevel = new Location(Bukkit.getWorld(OBSTACLE), 1164,6,-50,90,1.35f);
        Location blackLevel = new Location(Bukkit.getWorld(OBSTACLE),1100,4,-50,90,-1.960f);
        Location redLevel = new Location(Bukkit.getWorld(OBSTACLE),1076,6,-50,90,1.3f);
        Location magentaLevel = new Location(Bukkit.getWorld(OBSTACLE), 1037,3,-50,93,-6.3f);

        Block below = DataPlayer.get(event.getPlayer()).blockBelow().getBlock();
        if (below.getType() == Material.WOOL) event.getPlayer().teleport(switch (below.getData()) {
            default -> whiteLevel;
            case 15 -> blackLevel;
            case 14 -> redLevel;
            case 2 -> magentaLevel;
        }); else return;


        DataPlayer.get(event.getPlayer()).sendActionMsg(PREFIX_OBS + "Teleported you back to the start of the level.");
    }
}
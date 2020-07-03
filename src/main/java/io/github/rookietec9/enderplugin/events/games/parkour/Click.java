package io.github.rookietec9.enderplugin.events.games.parkour;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Jeremi
 * @version 18.5.8
 * @since 7.0.8
 */
public class Click implements Listener {

    @EventHandler
    public void run(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.BARRIER) && e.getPlayer().getWorld().getName().equalsIgnoreCase(Worlds.PARKOUR) && e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            e.getClickedBlock().setType(Material.WOOL);
            e.getClickedBlock().setData((byte) 1);
            e.getPlayer().sendMessage("§6Uncovered the §ebarrier §6block");
            EnderPlugin.scheduler().runSingleTask(() -> e.getClickedBlock().setType(Material.BARRIER), "PARKOUR_CLICK", 2);
        }
    }
}
package io.github.rookietec9.enderplugin.events.games.parkour;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 7.0.8
 */
public class Click implements Listener {

    private final BukkitScheduler sch = Bukkit.getScheduler();

    @EventHandler
    public void run(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.BARRIER)
                && e.getPlayer().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.PARKOUR)
                && e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            e.getClickedBlock().setType(Material.WOOL);
            e.getClickedBlock().setData((byte) 1);
            e.getPlayer().sendMessage("§6Uncovered the §ebarrier §6block");
            sch.scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> e.getClickedBlock().setType(Material.BARRIER), 40L);
        }
    }
}
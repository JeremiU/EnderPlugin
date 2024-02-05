package io.github.rookietec9.enderplugin.events.games.obst;

import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static io.github.rookietec9.enderplugin.Reference.OBSTACLE;
import static io.github.rookietec9.enderplugin.Reference.PREFIX_OBS;

/**
 * @author Jeremi
 * @version 25.6.4
 * @since 25.6.1
 */
public class BoxingEvent implements Listener {

    private boolean run = false;

    @EventHandler
    public void run(PlayerChangedWorldEvent event) {
        if (event.getFrom().getName().equalsIgnoreCase(OBSTACLE) && Bukkit.getWorld(OBSTACLE).getPlayers().isEmpty()) run = false;
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(OBSTACLE)) run = true;
        if (!EnderPlugin.scheduler().isRunning("OBST_BOX_SWITCH_1") && run) EnderPlugin.scheduler().runRepeatingTask(this :: runFirst, "OBST_BOX_SWITCH_1", 0, 0.5, PREFIX_OBS);
        if (!EnderPlugin.scheduler().isRunning("OBST_BOX_SWITCH_2") && run) EnderPlugin.scheduler().runRepeatingTask(this :: runSecond, "OBST_BOX_SWITCH_2", 0.3, 0.5, PREFIX_OBS);
        if (!EnderPlugin.scheduler().isRunning("OBST_BOX_SWITCH_3") && run) EnderPlugin.scheduler().runRepeatingTask(this :: runThird, "OBST_BOX_SWITCH_3", 0.65, 0.5, PREFIX_OBS);
        if (!EnderPlugin.scheduler().isRunning("OBST_BOX_SWITCH_4") && run) EnderPlugin.scheduler().runRepeatingTask(this :: runFourth, "OBST_BOX_SWITCH_4", 0.75, 0.5, PREFIX_OBS);
    }

    public void runFirst() {
        boolean on = Bukkit.getWorld(OBSTACLE).getBlockAt(1074, 7, -49).getType() == Material.WOOL;

        Bukkit.getWorld(OBSTACLE).getBlockAt(1074, 5, -49).setType(on ? Material.QUARTZ_BLOCK : Material.WOOL);
        Bukkit.getWorld(OBSTACLE).getBlockAt(1074, 5, -49).setData((byte) 14);

        for (int i = 6; i < 8; i++) {
            Bukkit.getWorld(OBSTACLE).getBlockAt(1074, i, -49).setType(on ? Material.AIR : Material.WOOL);
            Bukkit.getWorld(OBSTACLE).getBlockAt(1074, i, -49).setData((byte) 14);
        }
    }

    public void runSecond() {
        boolean on = Bukkit.getWorld(OBSTACLE).getBlockAt(1064, 7, -50).getType() == Material.WOOL;
        for (int i = 6; i < 8; i++) {
            Bukkit.getWorld(OBSTACLE).getBlockAt(1064, i, -50).setType(on ? Material.AIR : Material.WOOL);
            Bukkit.getWorld(OBSTACLE).getBlockAt(1064, i, -50).setData((byte) 14);
        }
    }

    public void runThird() {
        boolean on = Bukkit.getWorld(OBSTACLE).getBlockAt(1051, 6, -51).getType() == Material.WOOL;
        for (int i = -51; i < -49; i++) {
            Bukkit.getWorld(OBSTACLE).getBlockAt(1051, 6, i).setType(on ? Material.AIR : Material.WOOL);
            Bukkit.getWorld(OBSTACLE).getBlockAt(1051, 6, i).setData((byte) 14);
        }
    }

    public void runFourth() {
        boolean on = Bukkit.getWorld(OBSTACLE).getBlockAt(1044, 7, -51).getType() == Material.WOOL;
        for (int i = 6; i < 8; i++) {
            for (int j = -51; j < -49; j++) {
                Bukkit.getWorld(OBSTACLE).getBlockAt(1044,i,j).setType(on ? Material.AIR : Material.WOOL);
                Bukkit.getWorld(OBSTACLE).getBlockAt(1044,i,j).setData((byte) 14);
            }
        }
    }
}
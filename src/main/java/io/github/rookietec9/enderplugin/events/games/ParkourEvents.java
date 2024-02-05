package io.github.rookietec9.enderplugin.events.games;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.scoreboards.ParkourBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.7.3
 * @since 7.0.8
 */
public class ParkourEvents implements Listener {

    public static final Location
            RESPAWN_WHITE = new Location(Bukkit.getWorld(PARKOUR), -2, 6, 4, 270, -3.15f),
            RESPAWN_GREEN = new Location(Bukkit.getWorld(PARKOUR), 33, 11, 12, 256, 11.5f),
            RESPAWN_ORANGE = new Location(Bukkit.getWorld(PARKOUR), 66, 9, 0, 270, 14.5f),
            RESPAWN_BLUE = new Location(Bukkit.getWorld(PARKOUR), 87, 12, -7, 170, -2.7f),
            RESPAWN_MAGENTA = new Location(Bukkit.getWorld(PARKOUR), 63, 12, -8, 90, 6.75f);

    public static final Location[] RESPAWNS = {RESPAWN_WHITE, RESPAWN_GREEN, RESPAWN_ORANGE, RESPAWN_BLUE, RESPAWN_MAGENTA};

    @EventHandler
    public void run(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.BARRIER) && e.getPlayer().getWorld().getName().equalsIgnoreCase(PARKOUR) && e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            e.getClickedBlock().setType(Material.WOOL);
            e.getClickedBlock().setData((byte) 1);

            if (!Java.isInRange(e.getPlayer().getLocation().getBlockX(), 67, 91)) return;
            if (!Java.isInRange(e.getPlayer().getLocation().getBlockX(), -4, 12)) return;

            DataPlayer.get(e.getPlayer()).sendActionMsg(PREFIX_PARKOUR(3) + "Uncovered a barrier block.");
            EnderPlugin.scheduler().runSingleTask(() -> e.getClickedBlock().setType(Material.BARRIER), "PARKOUR_CLICK", 2, PREFIX_PARKOUR(1));
        }
    }

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(PARKOUR) || event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        Location location = DataPlayer.get(event.getPlayer()).blockBelow();

        if (location.getBlock().getType() != Material.STAINED_CLAY && location.getBlock().getType() != Material.STAINED_GLASS) return;

        teleport(location, event.getPlayer(), switch (location.getBlock().getData()) {
            case 0 -> 1;
            case 5 -> 2;
            case 1 -> 3;
            case 11 -> 4;
            case 2 -> 5;
            default -> 0;
        });
    }

    private void teleport(Location location, Player player, int level) {
        if (location.getBlock().getType() == Material.STAINED_GLASS) {
            player.teleport(RESPAWNS[level]);
            Minecraft.worldBroadcast(PARKOUR, "§lPARKOUR" + SUFFIX + DataPlayer.getUser(player).getNickName() + " finished the " + PARKOUR_COLOR(level) + Java.number(level) + "§7 level.");
            DataPlayer.get(player).tempParkourLevel = ++level;
            DataPlayer.get(player).getBoard(ParkourBoard.class).updateLevel();
        }
        if (location.getBlock().getType() == Material.STAINED_CLAY) {
            player.teleport(RESPAWNS[level - 1]);
            DataPlayer.get(player).sendActionMsg(PREFIX_PARKOUR(level) + "Teleported you back to the start of the level.");
        }
    }
}
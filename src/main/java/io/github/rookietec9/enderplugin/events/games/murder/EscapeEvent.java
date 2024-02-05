package io.github.rookietec9.enderplugin.events.games.murder;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.scoreboards.murder.MurderBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Teams;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import static io.github.rookietec9.enderplugin.Reference.MURDER;
import static io.github.rookietec9.enderplugin.Reference.PREFIX_MURDER;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 16.6.2
 */
public class EscapeEvent implements Listener {

    private final Spawn spawn = new Spawn(MURDER);

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getTo().getWorld().getName().equalsIgnoreCase(MURDER)) return;

        Player p = event.getPlayer();
        Location plateLoc = event.getTo();

        Location bottomLoc = event.getTo().clone();
        bottomLoc.setY(bottomLoc.getY() - 1);

        if (plateLoc.getBlock().getType().equals(Material.STONE_PLATE)) {
            if (!p.getGameMode().equals(GameMode.ADVENTURE) || !bottomLoc.getBlock().getType().equals(Material.BEDROCK)) return;

            if (Teams.contains(Teams.TEAM_POSITIVE, p)) {
                Teams.remove(Teams.TEAM_POSITIVE, p);
                p.teleport(spawn.location(), PlayerTeleportEvent.TeleportCause.PLUGIN);

                DataPlayer.prisonEscapeList.add(p);

                for (Player player : event.getTo().getWorld().getPlayers()) {
                    if (Teams.getTeam(Teams.TEAM_POSITIVE).getSize() > 0) player.sendMessage(PREFIX_MURDER + DataPlayer.getUser(p).getNickName() + " has escaped!");
                    DataPlayer.get(player).getBoard(MurderBoard.class).updatePeople(Teams.getTeam(Teams.TEAM_POSITIVE).getSize());
                }

                if (Teams.getTeam(Teams.TEAM_POSITIVE).getSize() == 0) EnderPlugin.murderBase.reset();
            }

            if (Teams.contains(Teams.TEAM_NEGATIVE, p)) DataPlayer.get(p).sendActionMsg(PREFIX_MURDER + "Go and stop the cellies from escaping!");
        }
    }
}
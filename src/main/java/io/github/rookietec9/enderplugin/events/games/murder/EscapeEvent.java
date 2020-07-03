package io.github.rookietec9.enderplugin.events.games.murder;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.scoreboards.MurderBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 16.6.2
 */
public class EscapeEvent implements Listener {

    private final Spawn spawn = new Spawn(Worlds.MURDER);

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getTo().getWorld().getName().equalsIgnoreCase(Worlds.MURDER)) return;

        Player p = event.getPlayer();
        Location plateLoc = event.getTo();

        Location bottomLoc = event.getTo().clone();
        bottomLoc.setY(bottomLoc.getY() - 1);

        if (plateLoc.getBlock().getType().equals(Material.STONE_PLATE)) {
            if (!p.getGameMode().equals(GameMode.ADVENTURE) || !bottomLoc.getBlock().getType().equals(Material.BEDROCK)) return;

            if (Teams.contains(Teams.goodTeam, p)) {
                Teams.remove(Teams.goodTeam, p);
                p.teleport(spawn.location(), PlayerTeleportEvent.TeleportCause.PLUGIN);

                DataPlayer.prisonEscapeList.add(p);

                for (Player player : event.getTo().getWorld().getPlayers()) {
                    if (Teams.getTeam(Teams.goodTeam).getSize() > 0) player.sendMessage(Prefixes.MURDER + DataPlayer.getUser(p).getNickName() + " has escaped!");
                    DataPlayer.get(player).getBoard(MurderBoard.class).updatePeople(Teams.getTeam(Teams.goodTeam).getSize());
                }

                if (Teams.getTeam(Teams.goodTeam).getSize() == 0) EnderPlugin.murderBase.reset();
            }

            if (Teams.contains(Teams.badTeam, p)) DataPlayer.get(p).sendActionMsg(Prefixes.MURDER + "Go and stop the cellies from escaping!");
        }
    }
}
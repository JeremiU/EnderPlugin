package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.scoreboards.HubBoard;
import io.github.rookietec9.enderplugin.scoreboards.ParkourBoard;
import io.github.rookietec9.enderplugin.scoreboards.WizardsBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.2.6
 * @since 13.4.4
 */
public class ScoreboardSetupEvent implements Listener {

    @EventHandler
    public void run(PlayerChangedWorldEvent event) {

        if (event.getPlayer().getWorld() == null) return;
        if (event.getFrom().getName().equalsIgnoreCase(PARKOUR)) {
            DataPlayer.get(event.getPlayer()).tempParkourLevel = 1;
            DataPlayer.get(event.getPlayer()).getBoard(ParkourBoard.class).updateLevel();
        }
        if (event.getFrom().getName().equalsIgnoreCase(WIZARDS)) DataPlayer.get(event.getPlayer()).getBoard(WizardsBoard.class).updateBlade("NONE");

        DataPlayer.get(event.getPlayer()).finishGame(event.getPlayer().getGameMode());

        for (Board board : DataPlayer.get(event.getPlayer()).boards())
            if (board != null && board.getWorld() != null && event.getPlayer().getWorld().equals(board.getWorld())) {
                board.init();
                event.getPlayer().setGameMode(board.getDefaultGameMode());
           }
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {

        DataPlayer.registerPlayers();
        DataPlayer.get(event.getPlayer()).getBoard(HubBoard.class).init();
        DataPlayer.get(event.getPlayer()).hub();

        for (Player player : Bukkit.getWorld(HUB).getPlayers()) DataPlayer.get(player).getBoard(HubBoard.class).updatePlayers(Bukkit.getOnlinePlayers().size());
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        for (Player player : Bukkit.getWorld(HUB).getPlayers()) if (null != DataPlayer.get(player)) DataPlayer.get(player).getBoard(HubBoard.class).updatePlayers(Bukkit.getOnlinePlayers().size()-1);
        DataPlayer.unregisterPlayer(event.getPlayer());
    }

    @EventHandler
    public void kick(PlayerKickEvent event) {
        for (Player player : Bukkit.getWorld(HUB).getPlayers()) DataPlayer.get(player).getBoard(HubBoard.class).updatePlayers(Bukkit.getOnlinePlayers().size()-1);
        DataPlayer.unregisterPlayer(event.getPlayer());
    }
}
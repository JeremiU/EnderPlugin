package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.scoreboards.HubBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Jeremi
 * @version 21.5.8
 * @since 13.4.4
 */
public class ScoreboardSetupEvent implements Listener {

    @EventHandler
    public void run(PlayerChangedWorldEvent event) {

        if (event.getPlayer().getWorld() == null) return;

        for (Board board : DataPlayer.get(event.getPlayer()).boards())
            if (board != null && event.getPlayer().getWorld() == board.getWorld()) {
                board.init();
                event.getPlayer().setGameMode(board.getDefaultGameMode());

                if (board.getWorld().getName().equalsIgnoreCase(Worlds.MURDER)) board.updateData("Phase", DataPlayer.murderMode);
            }
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {

        DataPlayer.registerPlayers();
        DataPlayer.get(event.getPlayer()).getBoard(HubBoard.class).init();
        DataPlayer.getUser(event.getPlayer()).hub();

        for (Player player : Bukkit.getWorld(Worlds.HUB).getPlayers()) DataPlayer.get(player).getBoard(HubBoard.class).updatePlayers(Bukkit.getOnlinePlayers().size());
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        for (Player player : Bukkit.getWorld(Worlds.HUB).getPlayers()) if (null != DataPlayer.get(player)) DataPlayer.get(player).getBoard(HubBoard.class).updatePlayers(Bukkit.getOnlinePlayers().size()-1);
        DataPlayer.unregisterPlayer(event.getPlayer());
    }

    @EventHandler
    public void kick(PlayerKickEvent event) {
        for (Player player : Bukkit.getWorld(Worlds.HUB).getPlayers()) DataPlayer.get(player).getBoard(HubBoard.class).updatePlayers(Bukkit.getOnlinePlayers().size()-1);
        DataPlayer.unregisterPlayer(event.getPlayer());
    }
}
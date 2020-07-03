package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 22.8.0
 */
public class MainLeaveEvent implements Listener {
    @EventHandler
    public void run(PlayerQuitEvent e) {
        //        if (e.getPlayer().getName().equalsIgnoreCase("Alanu")) {
        //            e.setQuitMessage("Celebrating good times, sponsored by " + new User(e.getPlayer()).getTabName());
        //            return;
        //        }
        e.setQuitMessage(serverLang().getDarkColor() + DataPlayer.getUser(e.getPlayer()).getNickName() + serverLang().getLightColor() + " left the server.");
    }
}
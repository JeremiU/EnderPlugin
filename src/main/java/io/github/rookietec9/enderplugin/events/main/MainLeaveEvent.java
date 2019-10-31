package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 */
public class MainLeaveEvent implements Listener {
    @EventHandler
    public void run(PlayerQuitEvent e) {
        if (e.getPlayer().getName().equalsIgnoreCase("Alanu")) {
            e.setQuitMessage("Celebrating good times, sponsored by " + new User(e.getPlayer()).getTabName());
            return;
        }
        e.setQuitMessage(new User(e.getPlayer()).getTabName() + new Lang(Langs.fromSender(e.getPlayer())).getLightColor() + " seemed to smash that rage button.");
    }
}

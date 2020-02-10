package io.github.rookietec9.enderplugin.events.main;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.Worlds;

/**
 * @author Jeremi
 * @version 16.8.7
 * @since 16.8.7
 */
public class MainSwitchWorldEvent implements Listener {

    @EventHandler
    public void run(PlayerChangedWorldEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(Worlds.SPLEEF))
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
    }
}
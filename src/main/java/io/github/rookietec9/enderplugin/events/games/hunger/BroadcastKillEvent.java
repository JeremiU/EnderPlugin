package io.github.rookietec9.enderplugin.events.games.hunger;

import com.sun.webkit.network.Util;
import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 8.5.3
 */
public class BroadcastKillEvent implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (!event.getEntity().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUNGER)) return;
        if (event.getEntity().getKiller() == null) return;
        Bukkit.broadcastMessage("§4Hunger §c> §7A§f[§7N§f] §c" + event.getEntityType() + " §7Was killed by §c" + event.getEntity().getKiller().getName() + "§7!");
    }
}
package io.github.rookietec9.enderplugin.events.games.hunger;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 8.5.3
 */
public class BroadcastLavaEvent implements Listener {

    @EventHandler
    public void onBucket(PlayerBucketFillEvent event) {
        if (!(event.getItemStack().getType() == Material.LAVA_BUCKET)) return;
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUNGER)) return;
        Bukkit.broadcastMessage("§4Hunger §c> §7" + event.getPlayer().getName() + " filled a " + "§cLava Bucket§7!");
    }
}
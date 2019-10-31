package io.github.rookietec9.enderplugin.events.games.hunger;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 8.5.3
 */
public class BroadcastMineEvent implements Listener {

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.DIAMOND_ORE) return;
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUNGER)) return;
        Bukkit.broadcastMessage("§4Hunger §c> §7" + event.getPlayer().getName() + " broke a " + "§cDiamond Ore§f!");
    }

}
package io.github.rookietec9.enderplugin.events.hub;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static io.github.rookietec9.enderplugin.Reference.HUB;
import static io.github.rookietec9.enderplugin.Reference.PREFIX_HUB;

/**
 * @author Jeremi
 * @version 25.3.3
 * @since 20.3.8
 */
public class TogglerEvent implements Listener {

    @EventHandler
    public void run(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        switch (event.getItem().getType()) {
            case BONE -> {
                if (EnderPlugin.scheduler().isRunning("TOGGLER_PVP_" + event.getPlayer().getName())) return;
                EnderPlugin.scheduler().runMarker("TOGGLER_PVP_" + event.getPlayer().getName().toUpperCase(), 3, PREFIX_HUB);

                if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(HUB)) {
                    event.getPlayer().sendMessage(PREFIX_HUB + "This feature only works in the hub.");
                    return;
                }

                boolean set = !DataPlayer.get(event.getPlayer()).pvpEnabled;
                event.getPlayer().sendMessage(PREFIX_HUB + (set ? "Enabled" : "Disabled") + " combat.");
                DataPlayer.get(event.getPlayer()).pvpEnabled = set;
                event.getPlayer().getInventory().setItemInHand(new ItemWrapper<>(Material.BONE, "§7Combat: " + (set ? "§aEnabled" : "§cDisabled")).toItemStack());
            }
            case INK_SACK -> {
                if (event.getItem().getData().getData() != 10 && event.getItem().getData().getData() != 8) return;
                if (EnderPlugin.scheduler().isRunning("TOGGLER_CHAT_" + event.getPlayer().getName())) return;
                boolean set = !DataPlayer.get(event.getPlayer()).chatEnabled;
                event.getPlayer().sendMessage(PREFIX_HUB + (set ? "Enabled" : "Disabled") + " chat.");
                DataPlayer.get(event.getPlayer()).chatEnabled = set;
                event.getPlayer().getInventory().setItemInHand(new ItemWrapper<>(Material.INK_SACK, "§7Chat: " + (set ? "§aEnabled" : "§cDisabled"), set ? (byte) 10 : (byte) 8, 1).toItemStack());
                EnderPlugin.scheduler().runMarker("TOGGLER_CHAT_" + event.getPlayer().getName().toUpperCase(), 3, PREFIX_HUB);
            }
            case GREEN_RECORD, RECORD_4 -> {
                if (EnderPlugin.scheduler().isRunning("TOGGLER_PLAYERS_" + event.getPlayer().getName())) return;
                EnderPlugin.scheduler().runMarker("TOGGLER_PLAYERS_" + event.getPlayer().getName(), 3, PREFIX_HUB);

                if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(HUB)) {
                    event.getPlayer().sendMessage(PREFIX_HUB + "This feature only works in the hub.");
                    return;
                }

                boolean set = !DataPlayer.get(event.getPlayer()).playersEnabled;
                event.getPlayer().sendMessage(PREFIX_HUB + (set ? "Enabled" : "Disabled") + " players.");
                DataPlayer.get(event.getPlayer()).playersEnabled = set;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (DataPlayer.get(event.getPlayer()).playersEnabled) event.getPlayer().showPlayer(player);
                    else event.getPlayer().hidePlayer(player);
                }
                event.getPlayer().getInventory().setItemInHand(new ItemWrapper<>(set ? Material.GREEN_RECORD : Material.RECORD_4, "§7Players: " + (set ? "§aEnabled" : "§cDisabled")).toItemStack());
            }
        }
    }
}
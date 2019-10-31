package io.github.rookietec9.enderplugin.events.games.hunger;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 8.5.3
 */
public class BroadcastEnchEvent implements Listener {

    @EventHandler
    public void onKill(EnchantItemEvent event) {
        String name = Utils.upSlash(event.getItem().getType().toString()).toUpperCase();
        int level = event.getEnchantsToAdd().entrySet().iterator().next().getValue();

        if (!event.getEnchanter().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUNGER)) return;
        for (int i = 0; i < event.getEnchantsToAdd().size(); i++) {
            Bukkit.broadcastMessage("§4Hunger §c> §7" + event.getEnchanter().getName() + " enchanted " + "a §c" + Utils.upSlash(event.getItem().getType().toString()).toUpperCase() + "§7 with §c" +
                    Utils.upSlash(((Enchantment) event.getEnchantsToAdd().keySet().toArray()[i]).getName()).toUpperCase() +
                    " §7Level §c" + (event.getEnchantsToAdd().values().toArray()[i]) + "§7!");
        }
    }
}
package io.github.rookietec9.enderplugin.events.games.esg;

import io.github.rookietec9.enderplugin.configs.esg.ESGKit;
import io.github.rookietec9.enderplugin.configs.esg.ESGPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 *
 * @author Jeremi
 * @version 18.5.8
 * @since 7.1.5
 */
public class ESGStartChooseEvent implements Listener {

    @EventHandler
    public void run(InventoryClickEvent event) {

        if (!event.getInventory().getTitle().equalsIgnoreCase("§e§lCHOOSE A KIT") || !(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ESGPlayer esgPlayer = new ESGPlayer(player);

        for (ESGKit.Kits esgKit : ESGKit.Kits.values()) {
            if (event.getCurrentItem().getType() == new ESGKit(esgKit).getMaterial()) {
                player.sendMessage("§eSelected " + new ESGKit(esgKit).getColor() + Java.upSlash(esgKit.toString()) + " §r[" + new ESGKit(esgKit).getColor() + esgPlayer.getKitLevel(esgKit) + "§r]" + "§e. You will get it 60 seconds after the game starts.");
            } else if (!esgPlayer.getKitUnlocked(esgKit)) {
                player.sendMessage("§eYou haven't unlocked " + new ESGKit(esgKit).getColor() + Java.capFirst(esgKit.toString()) + "§e yet!");
            }
        }
        event.setCancelled(true);
        player.closeInventory();
    }
}
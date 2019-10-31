package io.github.rookietec9.enderplugin.events.games.esg;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.esg.ESGKit;
import io.github.rookietec9.enderplugin.API.esg.ESGPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * While technically existing before v7.1.5, I decided to re-brand the class due to the utter of the for loop.
 *
 * @author Jeremi
 * @version 11.6.0
 * @since 7.1.5
 */
public class ESGStartChooseEvent implements Listener {

    @EventHandler
    public void run(InventoryClickEvent event) {

        if (!event.getInventory().getTitle().contains("Choose Your Kit")) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        ESGPlayer esgPlayer = new ESGPlayer(player);

        for (ESGKit esgKit : ESGKit.values()) {
            if (event.getCurrentItem().getType() == esgKit.getType()) {
                if (!esgPlayer.getKitUnlocked(esgKit)) {
                    player.sendMessage("§eYou haven't unlocked " + esgKit.getColor() + Utils.capFirst(esgKit.toString()) + "§e yet!");
                    event.setCancelled(true);
                } else {
                    player.sendMessage("§eSelected " + esgKit.getColor() + Utils.upSlash(esgKit.toString()) + " §r["
                            + esgKit.getColor() + esgPlayer.getKitLevel(esgKit) + "§r]" + "§e. You will get it 60 seconds after the game starts.");
                    event.setCancelled(true);
                }
            }
        }
        player.closeInventory();
    }
}
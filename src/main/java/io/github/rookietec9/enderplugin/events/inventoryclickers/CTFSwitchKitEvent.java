package io.github.rookietec9.enderplugin.events.inventoryclickers;

import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static io.github.rookietec9.enderplugin.Reference.CTF;
import static io.github.rookietec9.enderplugin.Reference.PREFIX_CTF;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 22.1.2
 */
public class CTFSwitchKitEvent implements Listener {

    @EventHandler
    public void run(PlayerInteractEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(CTF) || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.WALL_SIGN) return;
        if (!(event.getClickedBlock().getState() instanceof Sign)) return;
        event.getPlayer().openInventory(Inventories.CTF_KIT);
    }

    @EventHandler
    public void run(InventoryClickEvent event) {

        if (event.getClickedInventory() == null || !Inventories.CTF_KIT.getName().equalsIgnoreCase(event.getClickedInventory().getName())) return;
        if (event.getSlot() != 2 && event.getSlot() != 4 && event.getSlot() != 6) return;
        event.setCancelled(true);

        DataPlayer.get((Player) event.getWhoClicked()).tempCTFKit = (byte) ((event.getSlot() / 2)-1);
        event.getWhoClicked().sendMessage(PREFIX_CTF + "You will receive the " + switch (DataPlayer.get((Player) event.getWhoClicked()).tempCTFKit) {
            case 0 -> "Swordsman";
            case 1 -> "Archer";
            default -> "Tank";
        } + " class during the game.");
        event.getWhoClicked().closeInventory();
    }
}
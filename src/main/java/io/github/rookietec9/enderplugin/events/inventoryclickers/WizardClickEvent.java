package io.github.rookietec9.enderplugin.events.inventoryclickers;

import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.scoreboards.WizardsBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.Blades;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

/**
 * @author Jeremi
 * @version 25.2.6
 * @since 21.8.4
 */
public class WizardClickEvent implements Listener {

    public static boolean giveBlade(String input, Player player) {
        if (input == null) return false;
        player.closeInventory();
        for (Blades blade : Blades.blades) {
            if (input.equalsIgnoreCase(blade.getName())) {
                DataPlayer.get(player).clearEffects();
                DataPlayer.get(player).clear();
                player.getInventory().setHeldItemSlot(0);
                player.getInventory().setItem(0, blade.getWeapon().toItemStack());
                player.getInventory().setHelmet(blade.getHelmet().toItemStack());
                player.getInventory().setChestplate(blade.getChestplate().toItemStack());
                player.getInventory().setLeggings(blade.getLeggings().toItemStack());
                player.getInventory().setBoots(blade.getBoots().toItemStack());
                player.setGameMode(GameMode.ADVENTURE);
                player.setHealthScale(20);
                player.setHealth(20);
                ChatColor txtColor = (blade.getChatColor() == ChatColor.WHITE) ? ChatColor.GRAY : ChatColor.WHITE;
                DataPlayer.get(player).sendActionMsg(txtColor + "" + ChatColor.BOLD + "GAVE YOU THE " + blade.getChatColor() + "" + ChatColor.BOLD + ChatColor.stripColor(blade.getName().toUpperCase()) + txtColor + "" + ChatColor.BOLD + " BLADE");
                DataPlayer.get(player).getBoard(WizardsBoard.class).updateBlade(blade.getName());
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void run(InventoryInteractEvent event) {
        if (event.getInventory().getName().equalsIgnoreCase(Inventories.WIZARD_BLADE.getName())) event.setCancelled(true);
    }

    @EventHandler
    public void run(InventoryClickEvent event) {
        if (!event.getInventory().getName().equalsIgnoreCase(Inventories.WIZARD_BLADE.getName())) return;

        event.setCancelled(true);

        giveBlade(switch (event.getSlot()) {
            case 0 -> Blades.ghost.getName();
            case 1 -> Blades.speedy.getName();
            case 2 -> Blades.puncher.getName();
            case 3 -> Blades.spider.getName();
            case 14 -> Blades.power.getName();
            case 15 -> Blades.miner.getName();
            case 16 -> Blades.health.getName();
            case 17 -> Blades.poison.getName();
            case 18 -> Blades.gapple.getName();
            case 19 -> Blades.tank.getName();
            case 20 -> Blades.slow.getName();
            case 21 -> Blades.weak.getName();
            case 32 -> Blades.fire.getName();
            case 33 -> Blades.fisher.getName();
            case 34 -> Blades.knocker.getName();
            case 35 -> Blades.jump.getName();
            case 36 -> Blades.anvil.getName();
            case 37 -> Blades.nausea.getName();
            case 38 -> Blades.uncertain.getName();
            case 39 -> Blades.aqua.getName();
            default -> null;
        }, (Player) event.getWhoClicked());
    }
}
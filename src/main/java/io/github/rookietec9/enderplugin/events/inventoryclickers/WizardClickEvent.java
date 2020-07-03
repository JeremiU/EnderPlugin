package io.github.rookietec9.enderplugin.events.inventoryclickers;

import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.configs.associates.Blades;
import io.github.rookietec9.enderplugin.scoreboards.WizardsBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

/**
 * @author Jeremi
 * @version 22.2.9
 * @since 21.8.4
 */
public class WizardClickEvent implements Listener {

    @EventHandler
    public void run(InventoryInteractEvent event) {
        if (event.getInventory().getName().equalsIgnoreCase(Inventories.WIZARD_BLADE.getName())) event.setCancelled(true);
    }

    @EventHandler
    public void run(InventoryClickEvent event) {
        if (!event.getInventory().getName().equalsIgnoreCase(Inventories.WIZARD_BLADE.getName())) return;

        event.setCancelled(true);

        switch (event.getSlot()) {
            case 0 -> giveBlade(Blades.ghost.getName(), (Player) event.getWhoClicked());
            case 1 -> giveBlade(Blades.speedy.getName(), (Player) event.getWhoClicked());
            case 2 -> giveBlade(Blades.puncher.getName(), (Player) event.getWhoClicked());
            case 3 -> giveBlade(Blades.spider.getName(), (Player) event.getWhoClicked());

            case 14 -> giveBlade(Blades.power.getName(), (Player) event.getWhoClicked());
            case 15 -> giveBlade(Blades.miner.getName(), (Player) event.getWhoClicked());
            case 16 -> giveBlade(Blades.health.getName(), (Player) event.getWhoClicked());
            case 17 -> giveBlade(Blades.poison.getName(), (Player) event.getWhoClicked());

            case 18 -> giveBlade(Blades.gapple.getName(), (Player) event.getWhoClicked());
            case 19 -> giveBlade(Blades.tank.getName(), (Player) event.getWhoClicked());
            case 20 -> giveBlade(Blades.slow.getName(), (Player) event.getWhoClicked());
            case 21 -> giveBlade(Blades.weak.getName(), (Player) event.getWhoClicked());

            case 32 -> giveBlade(Blades.fire.getName(), (Player) event.getWhoClicked());
            case 33 -> giveBlade(Blades.warrior.getName(), (Player) event.getWhoClicked());
            case 34 -> giveBlade(Blades.knocker.getName(), (Player) event.getWhoClicked());
            case 35 -> giveBlade(Blades.jump.getName(), (Player) event.getWhoClicked());

            case 36 -> giveBlade(Blades.anvil.getName(), (Player) event.getWhoClicked());
            case 37 -> giveBlade(Blades.nausea.getName(), (Player) event.getWhoClicked());
            case 38 -> giveBlade(Blades.uncertain.getName(), (Player) event.getWhoClicked());
            case 39 -> giveBlade(Blades.aqua.getName(), (Player) event.getWhoClicked());
        }
    }

    public static boolean giveBlade(String input, Player player) {
        player.closeInventory();
        for (Blades blade : Blades.blades) {
            if (input.equalsIgnoreCase(blade.getName())) {
                DataPlayer.getUser(player).clearEffects().clear();
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
                DataPlayer.get(player).getBoard(WizardsBoard.class).updateBlade(Java.capFirst(blade.getName()));
                return true;
            }
        }
        return false;
    }
}
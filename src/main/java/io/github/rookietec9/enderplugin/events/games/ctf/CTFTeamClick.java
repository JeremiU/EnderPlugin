package io.github.rookietec9.enderplugin.events.games.ctf;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author Jeremi
 * @version 13.8.7
 * @since 13.8.4
 */
public class CTFTeamClick implements Listener {

    @EventHandler
    public void run(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.CTF)) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clicked = event.getClickedBlock();
            if (clicked.getType() == Material.WOOD_BUTTON) {

                Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
                ItemStack chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
                ItemStack sword = new ItemStack(Material.WOOD_SWORD);
                LeatherArmorMeta lm = (LeatherArmorMeta) chestPlate.getItemMeta();
                ItemMeta itemMeta = sword.getItemMeta();
                sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

                player.getInventory().clear();

                if (clicked.getZ() == -10) { //BLUE
                    board.getTeam(Utils.Reference.Teams.blueTeam).addEntry(player.getName());
                    lm.setColor(Color.NAVY);
                    lm.setDisplayName(ChatColor.BLUE + "Leather Tunic");
                    itemMeta.setDisplayName(ChatColor.BLUE + "Wooden Sword");
                    player.teleport(new Location(player.getWorld(), -76.5,8,13.5,-90.03F,0.719F));
                    for (Player player1 : player.getWorld().getPlayers()) player1.sendMessage("§f§lC§e§lT§f§lF §7> " + ChatColor.BLUE + player.getName() + ChatColor.WHITE + " joined the " + ChatColor.BLUE + "blue team" + ChatColor.WHITE + ".");
                }
                if (clicked.getZ() == -8) { //RED
                    board.getTeam(Utils.Reference.Teams.redTeam).addEntry(player.getName());
                    lm.setColor(Color.MAROON);
                    lm.setDisplayName(ChatColor.RED + "Leather Tunic");
                    itemMeta.setDisplayName(ChatColor.RED + "Wooden Sword");
                    for (Player player1 : player.getWorld().getPlayers()) player1.sendMessage("§f§lC§e§lT§f§lF §7> " + ChatColor.RED + player.getName() + ChatColor.WHITE + " joined the " + ChatColor.RED + "red team" + ChatColor.WHITE + ".");
                    player.teleport(new Location(player.getWorld(), -19.5,8,13.5, -270F, 0.719F));
                }
                sword.setItemMeta(itemMeta);
                chestPlate.setItemMeta(lm);
                player.getInventory().setChestplate(chestPlate);
                player.getInventory().setHeldItemSlot(0);
                player.getInventory().setItemInHand(sword);
            }
        }
    }
}
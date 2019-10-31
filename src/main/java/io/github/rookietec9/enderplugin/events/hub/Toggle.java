package io.github.rookietec9.enderplugin.events.hub;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 11.2.7
 */
public class Toggle implements Listener {
    public static HashMap<UUID, Integer> toggleMap = new HashMap<>();

    @EventHandler
    public void PVP(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInHand();
        if (!player.getWorld().getName().equalsIgnoreCase("hub2")) return;
        if (itemStack.getType() == Material.BONE) {
            if (itemStack.getItemMeta().getDisplayName().toLowerCase().contains("§7toggle pvp: ")) {
                if (itemStack.getItemMeta().getDisplayName().toLowerCase().contains("§6invincible")) {
                    if (toggleMap.get(player.getUniqueId()) != 0) {
                        player.sendMessage("§7[§dHub§7] Hol' up!");
                        return;
                    }
                    toggleMap.put(player.getUniqueId(), 3);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            toggleMap.put(player.getUniqueId(), 0);
                        }
                    }, 60);
                    player.sendMessage("§7[§dHub§7] Set your PVP to §cEnabled§7.");
                    (new User(player)).setGod(false);

                    ItemStack togglePvp = new ItemStack(Material.BONE);
                    ItemMeta pvpMeta = togglePvp.getItemMeta();
                    pvpMeta.setDisplayName("§7Toggle PVP: §cEnabled");
                    togglePvp.setItemMeta(pvpMeta);

                    player.getInventory().setItem(2,togglePvp);
                }

                else if (itemStack.getItemMeta().getDisplayName().toLowerCase().contains("§cenabled"))

                {
                    if (toggleMap.get(player.getUniqueId()) != 0) {
                        player.sendMessage("§7[§dHub§7] Hol' up!");
                        return;
                    }
                    toggleMap.put(player.getUniqueId(), 3);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> toggleMap.put(player.getUniqueId(), 0), 60);
                    player.sendMessage("§7[§dHub§7] Set your PVP to §6Invincible§7.");
                    (new User(player)).setGod(true);

                    ItemStack togglePvp = new ItemStack(Material.BONE);
                    ItemMeta pvpMeta = togglePvp.getItemMeta();
                    pvpMeta.setDisplayName("§7Toggle PVP: §6Invincible");
                    togglePvp.setItemMeta(pvpMeta);
                    player.getInventory().setItem(2, togglePvp);
                }
            }
        }
    }

    @EventHandler
    public void Players(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInHand();
        if (!player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUB)) return;
        if (itemStack.getType() == Material.GREEN_RECORD) {
            if (player.getItemInHand().getItemMeta().getDisplayName().toLowerCase().contains("§7toggle players: ")) {
            }
        }
    }

    @EventHandler
    public void Chat(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInHand();
        if (!player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUB)) return;
        if (itemStack.getType() == Material.INK_SACK) {
            if (itemStack.getItemMeta().getDisplayName().toLowerCase().contains("§7toggle chat: ")) {
                if (itemStack.getItemMeta().getDisplayName().toLowerCase().contains("§avisible")) {
                    player.sendMessage("§7[§dHub§7] Set your chat to §cHidden§7.");
                } else if (itemStack.getItemMeta().getDisplayName().toLowerCase().contains("§chidden")) {
                    player.sendMessage("§7[§dHub§7] Set your chat to §aVisible§7.");
                }
            }
        }
    }
}
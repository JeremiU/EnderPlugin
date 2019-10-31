package io.github.rookietec9.enderplugin.events.games.booty;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Jeremi
 * @version 14.7.3
 * @since 3.1.2
 */
public class BootyJump implements Listener {

    private int[] check = {-1, 0, 1, 2};
    private Block[] locBlocks = new Block[check.length];

    @EventHandler
    public void run(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.ADVENTURE && p.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.BOOTY)) {
            checkBlock(Material.DIAMOND_BLOCK, PotionEffectType.SPEED, 5, p);
            checkBlock(Material.IRON_BLOCK, PotionEffectType.JUMP, 5, p);
            checkBlock(Material.DIRT, PotionEffectType.INCREASE_DAMAGE, 5, p);
            checkBlock(Material.WEB, PotionEffectType.BLINDNESS, 10, p);
            checkBlock(Material.STAINED_CLAY, PotionEffectType.SLOW_DIGGING, p, (byte) 6, false, false);
            checkBlock(Material.SPONGE, null, p, (byte) 0, true, false);
            checkBlock(Material.WOOD, null, p, (byte) 0, true, true);
            checkGlass(p);
        }
    }

    private void checkGlass(Player player) {
        if (player == null) return;
        if (player.getWorld() != Bukkit.getWorld(Utils.Reference.Worlds.BOOTY)) return;

        for (int i : new int[]{0, 1, 2, 3}) {
            locBlocks[i] = Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() +
                    check[i], player.getLocation().getBlockZ());
            if (locBlocks[i].getType() != Material.MYCEL) return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1));

            int blade = 31, boots = 32, leggings = 33, chestPlate = 34, helmet = 35;
            if (player.getInventory().getHelmet() != null) {
                player.getInventory().setItem(helmet, player.getInventory().getHelmet());
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
            }

            if (player.getInventory().getItem(0) != null) {
                player.getInventory().setItem(blade, player.getInventory().getItem(0));
                player.getInventory().setItem(0, new ItemStack(Material.AIR));
            }

            if (player.getInventory().getChestplate() != null) {
                player.getInventory().setItem(chestPlate, player.getInventory().getChestplate());
                player.getInventory().setChestplate(new ItemStack(Material.AIR));
            }
            if (player.getInventory().getLeggings() != null) {
                player.getInventory().setItem(leggings, player.getInventory().getLeggings());
                player.getInventory().setLeggings(new ItemStack(Material.AIR));
            }
            if (player.getInventory().getBoots() != null) {
                player.getInventory().setItem(boots, player.getInventory().getBoots());
                player.getInventory().setBoots(new ItemStack(Material.AIR));
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                if (player.getInventory().getItem(blade) != null) {
                    player.getInventory().setItem(0, player.getInventory().getItem(blade));
                    player.getInventory().setHeldItemSlot(0);
                    player.getInventory().setItem(blade, new ItemStack(Material.AIR));
                }

                if (player.getInventory().getItem(helmet) != null) {
                    player.getInventory().setHelmet(player.getInventory().getItem(helmet));
                    player.getInventory().setItem(helmet, new ItemStack(Material.AIR));
                }
                if (player.getInventory().getItem(chestPlate) != null) {
                    player.getInventory().setChestplate(player.getInventory().getItem(chestPlate));
                    player.getInventory().setItem(chestPlate, new ItemStack(Material.AIR));
                }
                if (player.getInventory().getItem(leggings) != null) {
                    player.getInventory().setLeggings(player.getInventory().getItem(leggings));
                    player.getInventory().setItem(leggings, new ItemStack(Material.AIR));
                }
                if (player.getInventory().getItem(boots) != null) {
                    player.getInventory().setBoots(player.getInventory().getItem(boots));
                    player.getInventory().setItem(boots, new ItemStack(Material.AIR));
                }
            }, 5 * 20);

        }
    }

    private void checkBlock(Material blockType, PotionEffectType potionType, Player player, byte b, boolean removeOldEffects, boolean breakBlock) {
        if (player == null) return;
        if (player.getWorld() != Bukkit.getWorld(Utils.Reference.Worlds.BOOTY)) return;

        for (int i : new int[]{0, 1, 2, 3}) {

            locBlocks[i] = Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() +
                    check[i], player.getLocation().getBlockZ());

            if (locBlocks[i].getType() != blockType || locBlocks[i].getData() != b) return;

            for (PotionEffect pot : player.getActivePotionEffects())
                if (removeOldEffects) player.removePotionEffect(pot.getType());
            if (potionType != null) player.addPotionEffect(new PotionEffect(potionType, 100, 1)); //5 sec

            if (breakBlock) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                    if (player.getLocation().getBlockX() == locBlocks[i].getLocation().getBlockX() && (player.getLocation().getBlockY() + check[i]) == locBlocks[i].getLocation().getBlockY() &&
                            player.getLocation().getBlockZ() == locBlocks[i].getLocation().getBlockZ()) {
                        if (player.getWorld().getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() + check[i], player.getLocation().getBlockZ()).getType() == blockType)
                            if (locBlocks[i].getType() == blockType && locBlocks[i].getData() == b)
                                locBlocks[i].setType(Material.AIR);
                    }
                }, 30L);
            }
        }
    }

    private void checkBlock(Material blockType, PotionEffectType potionType, int sec, Player player) {
        Location loc = player.getLocation();
        for (int i = 0; i < check.length; i++) {
            locBlocks[i] = Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() + check[i], player.getLocation().getBlockZ());
            if (Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getBlockAt(loc.getBlockX(), loc.getBlockY() + check[i], loc.getBlockZ()).getType().equals(blockType)) {
                player.addPotionEffect(new PotionEffect(potionType, (sec * 20), 1));
            }

        }
    }
}
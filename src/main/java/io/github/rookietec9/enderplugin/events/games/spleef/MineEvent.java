package io.github.rookietec9.enderplugin.events.games.spleef;

import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import io.github.rookietec9.enderplugin.xboards.SpleefBoard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.Worlds;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.tempSpleefBlocks;

/**
 * @author Jeremi
 * @version 15.6.5
 * @since 14.8.6
 */
public class MineEvent implements Listener {

    public static boolean run = true;
    public static HashMap<Player, Integer> hashMap = new HashMap<>();
    public static Map<Block, Boolean> blockMap = new ConcurrentHashMap<>();

    public static Runnable runnable() {
        return () -> {
            for (Player player : Bukkit.getWorld(Worlds.SPLEEF).getPlayers()) {
                Games.SpleefInfo spleefInfo = new Games().new SpleefInfo();
                spleefInfo.setBlocks(player, spleefInfo.blocksBroken(player) + hashMap.get(player));

                SpleefBoard spleefBoard = new SpleefBoard(player);
                spleefBoard.init();

                int i = hashMap.get(player) * 2;

                if (i != 0) {
                    if (player.getInventory().first(Material.SNOW_BALL) != -1 && player.getInventory().getItem(player.getInventory().first(Material.SNOW_BALL)).getAmount() < 64) {
                        if (i + player.getInventory().getItem(player.getInventory().first(Material.SNOW_BALL)).getAmount() >= 64) {
                            player.getInventory().getItem(player.getInventory().first(Material.SNOW_BALL)).setAmount(64);
                        } else {
                            player.getInventory().getItem(player.getInventory().first(Material.SNOW_BALL)).setAmount(player.getInventory().getItem(player.getInventory().first(Material.SNOW_BALL)).getAmount() + i);
                        }
                    } else if (player.getInventory().all(Material.SNOW_BALL).size() > 1) {
                        for (ItemStack itemStack : player.getInventory().all(Material.SNOW_BALL).values()) {
                            if (itemStack.getAmount() < 64) {
                                if (i + itemStack.getAmount() >= 64) itemStack.setAmount(64);
                                else itemStack.setAmount(itemStack.getAmount() + i);
                                return;
                            }
                        }
                    } else player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, i));
                }

                tempSpleefBlocks.put(player, tempSpleefBlocks.get(player) + hashMap.get(player));
                hashMap.put(player, 0);
                spleefBoard.updateTempBlocks(tempSpleefBlocks.get(player));
                spleefBoard.reloadStats();
            }
        };
    }

    @EventHandler
    public void run(BlockBreakEvent event) {

        Player player = event.getPlayer();

        if (!player.getWorld().getName().equalsIgnoreCase(Worlds.SPLEEF)) return;
        if (player.getGameMode() != GameMode.SURVIVAL) return;

        if (event.getBlock().getType() == Material.SNOW_BLOCK) {
            if (player.getItemInHand().getType() == Material.DIAMOND_SPADE) {
                hashMap.putIfAbsent(player, 0);
                hashMap.put(player, hashMap.get(player) + 1);

                blockMap.putIfAbsent(event.getBlock(), true);
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }

    @EventHandler
    public void run(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!player.getWorld().getName().equalsIgnoreCase(Worlds.SPLEEF)) return;
        if (player.getGameMode() != GameMode.SURVIVAL) return;

        if (event.getBlock().getType() == Material.SNOW_BLOCK) {
            blockMap.putIfAbsent(event.getBlock(), false);
        }
    }
}
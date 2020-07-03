package io.github.rookietec9.enderplugin.events.games.spleef;

import io.github.rookietec9.enderplugin.scoreboards.SpleefBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.reference.DataType;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jeremi
 * @version 21.3.4
 * @since 14.8.6
 */
public class MineEvent implements Listener {

    public static boolean run = true;
    public static final HashMap<Player, Integer> hashMap = new HashMap<>();
    public static final Map<Block, Boolean> blockMap = new ConcurrentHashMap<>();


    public static Runnable runnable() {
        return () -> {
            for (Player player : Bukkit.getWorld(Worlds.SPLEEF).getPlayers()) {
                DataPlayer.get(player).incrementBy(DataType.SPLEEFBLOCKS, hashMap.get(player));

                int i = hashMap.get(player);

                if (i > 0 && i <= 64) {
                    player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, i));
                } else if (i > 64) do {
                    player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, Math.min(i, 64)));
                    i -= 64;
                } while (i > 0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60*20, 4, true, true));

                int newVal = DataPlayer.get(player).tempSpleefBlocks + hashMap.get(player);
                hashMap.put(player, 0);

                DataPlayer.get(player).getBoard(SpleefBoard.class).updateTempBlocks(newVal);
                DataPlayer.get(player).getBoard(SpleefBoard.class).reloadStats();
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
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Worlds.SPLEEF) && event.getPlayer().getGameMode() == GameMode.SURVIVAL && event.getBlock().getType() == Material.SNOW_BLOCK) blockMap.putIfAbsent(event.getBlock(), false);
    }
}
package io.github.rookietec9.enderplugin.events.games.obst;

import io.github.rookietec9.enderplugin.Reference;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Jeremi
 * @version 25.5.4
 * @since 25.5.1
 */
public class StoneRespawnEvent implements Listener {

    private static final int[][] BLOCKS_REDSTONE = {{1097, 3, -47}, {1094, 3, -50}, {1087, 3, -50}};
    private static final int[][] BLOCKS_STONE = {{1097, 3, -50}, {1083, 3, -49}};
    private static final int[][] BLOCKS_GOLD = {{1097, 3, -53}, {1081, 3, -54}, {1079, 3, -48}};
    private static final int[][] BLOCKS_DIAMOND = {{1095, 3, -44}, {1091, 3, -50}, {1091, 3, -56}};
    private static final int[][] BLOCKS_IRON = {{1091, 3, -53}, {1082, 3, -42}};
    private static final int[][] BLOCKS_ANDESITE = {{1094, 3, -54}, {1092, 3, -42}, {1083, 3, -56}, {1081, 3, -51}, {1079, 3, -45}};
    private static final int[][] BLOCKS_COAL = {{1088, 3, -45}, {1085, 3, -53}};
    private static final int[][] BLOCKS_LAPIS = {{1091, 3, -47}, {1089, 3, -40}};
    private static final int[][] BLOCKS_EMERALD = {{1087, 3, -57}, {1086, 3, -42}, {1085, 3, -47}};

    @EventHandler
    public void run(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE || !event.getPlayer().getWorld().getName().equalsIgnoreCase(Reference.OBSTACLE)) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.WALL_SIGN) return;
        if (event.getClickedBlock().getLocation().getBlockX() != 1100 || event.getClickedBlock().getLocation().getBlockY() != 5 || event.getClickedBlock().getLocation().getBlockZ() != -51) return;

        for (int[] i : BLOCKS_REDSTONE) event.getPlayer().getWorld().getBlockAt(i[0], i[1], i[2]).setType(Material.REDSTONE_ORE);
        for (int[] i : BLOCKS_STONE) event.getPlayer().getWorld().getBlockAt(i[0], i[1], i[2]).setType(Material.STONE);
        for (int[] i : BLOCKS_GOLD) event.getPlayer().getWorld().getBlockAt(i[0], i[1], i[2]).setType(Material.GOLD_ORE);
        for (int[] i : BLOCKS_DIAMOND) event.getPlayer().getWorld().getBlockAt(i[0], i[1], i[2]).setType(Material.DIAMOND_ORE);
        for (int[] i : BLOCKS_IRON) event.getPlayer().getWorld().getBlockAt(i[0], i[1], i[2]).setType(Material.IRON_ORE);
        for (int[] i : BLOCKS_COAL) event.getPlayer().getWorld().getBlockAt(i[0], i[1], i[2]).setType(Material.COAL_ORE);
        for (int[] i : BLOCKS_LAPIS) event.getPlayer().getWorld().getBlockAt(i[0], i[1], i[2]).setType(Material.LAPIS_ORE);
        for (int[] i : BLOCKS_EMERALD) event.getPlayer().getWorld().getBlockAt(i[0], i[1], i[2]).setType(Material.EMERALD_ORE);

        for (int[] i : BLOCKS_ANDESITE) {
            event.getPlayer().getWorld().getBlockAt(i[0], i[1], i[2]).setType(Material.STONE);
            event.getPlayer().getWorld().getBlockAt(i[0], i[1], i[2]).setData((byte) 5);
        }
    }

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE || !event.getPlayer().getWorld().getName().equalsIgnoreCase(Reference.OBSTACLE)) return;
        for (int[][] blockLists : new int[][][]{BLOCKS_REDSTONE, BLOCKS_STONE, BLOCKS_GOLD, BLOCKS_DIAMOND, BLOCKS_IRON, BLOCKS_ANDESITE, BLOCKS_COAL, BLOCKS_LAPIS, BLOCKS_EMERALD})
            for (int[] i : blockLists)
                if (event.getTo().getBlockX() == i[0] && event.getTo().getBlockZ() == i[2]) {
                    for (int[] x : blockLists) {
                        if (x == i) continue;
                        event.getPlayer().getWorld().getBlockAt(x[0], x[1], x[2]).setType(Material.AIR);
                    }
                }
    }
}
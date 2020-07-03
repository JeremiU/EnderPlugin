package io.github.rookietec9.enderplugin.events.games.booty;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Jeremi
 * @version 19.9.9
 * @since 3.1.2
 */
public class BootyJump implements Listener {

    private final int[] check = {-1, 0, 1, 2};
    private final Block[] locBlocks = new Block[check.length];

    @EventHandler
    public void run(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.ADVENTURE && p.getWorld().getName().equalsIgnoreCase(Worlds.BOOTY)) {
            checkBlock(Material.DIAMOND_BLOCK, PotionEffectType.SPEED, 5, p);
            checkBlock(Material.IRON_BLOCK, PotionEffectType.JUMP, 5, p);
            checkBlock(Material.DIRT, PotionEffectType.INCREASE_DAMAGE, 5, p);
            checkBlock(Material.WEB, PotionEffectType.BLINDNESS, 10, p);
            checkBlock(Material.STAINED_CLAY, PotionEffectType.SLOW_DIGGING, p, (byte) 6, false, false);
            checkBlock(Material.SPONGE, null, p, (byte) 0, true, false);
            checkBlock(Material.WOOD, null, p, (byte) 0, true, true);
            checkMycelium(p);
        }
    }

    private void checkMycelium(Player player) {
        if (player == null) return;
        if (player.getWorld() != Bukkit.getWorld(Worlds.BOOTY)) return;

        for (int i : new int[]{0, 1, 2, 3}) {
            locBlocks[i] = Bukkit.getWorld(Worlds.BOOTY).getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() + check[i], player.getLocation().getBlockZ());
            if (locBlocks[i].getType() != Material.MYCEL) return;
            DataPlayer.getUser(player).ghost();
        }
    }

    private void checkBlock(Material blockType, PotionEffectType potionType, Player player, byte b, boolean removeOldEffects, boolean breakBlock) {
        if (player == null) return;
        if (player.getWorld() != Bukkit.getWorld(Worlds.BOOTY)) return;

        for (int i : new int[]{0, 1, 2, 3}) {

            locBlocks[i] = Bukkit.getWorld(Worlds.BOOTY).getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() + check[i], player.getLocation().getBlockZ());
            if (locBlocks[i].getType() != blockType || locBlocks[i].getData() != b) return;

            for (PotionEffect pot : player.getActivePotionEffects()) if (removeOldEffects) player.removePotionEffect(pot.getType());
            if (potionType != null) player.addPotionEffect(new PotionEffect(potionType, 100, 1)); //5 sec

            if (breakBlock) {
                EnderPlugin.scheduler().runSingleTask(() -> {
                    Location loc = player.getLocation().clone();
                    loc.setY(loc.getY() + check[i]);
                    if (locBlocks[i].getLocation() == loc && locBlocks[i].getType() == blockType && locBlocks[i].getData() == b || player.getWorld().getBlockAt(loc).getType() == blockType) locBlocks[i].setType(Material.AIR);
                }, "BOOTY_BLOCK_BREAK", 3);
            }
        }
    }

    private void checkBlock(Material blockType, PotionEffectType potionType, int sec, Player player) {
        Location loc = player.getLocation();
        for (int i = 0; i < check.length; i++) {
            loc.setY(loc.getY() + check[i]);
            locBlocks[i] = Bukkit.getWorld(Worlds.BOOTY).getBlockAt(loc);
            if (Bukkit.getWorld(Worlds.BOOTY).getBlockAt(loc).getType().equals(blockType)) player.addPotionEffect(new PotionEffect(potionType, (sec * 20), 1));
        }
    }
}
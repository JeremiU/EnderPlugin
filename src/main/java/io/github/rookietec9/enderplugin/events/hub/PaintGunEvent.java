package io.github.rookietec9.enderplugin.events.hub;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 25.3.7
 * @since 23.5.5
 */
public class PaintGunEvent implements Listener {

    final List<UUID> snowballs = new ArrayList<>();
    final HashSet<Location> blockLocs = new HashSet<>();

    @EventHandler
    public void run(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!event.hasItem() || !event.getItem().hasItemMeta() || (event.getItem().hasItemMeta() && !event.getItem().getItemMeta().hasDisplayName())) return;
        if (!event.getItem().getItemMeta().getDisplayName().toLowerCase().contains("paint gun")) return;
        if (!DataPlayer.canSnowBall.getOrDefault(event.getPlayer(), true)) return;
        DataPlayer.canSnowBall.put(event.getPlayer(), false);
        event.setCancelled(true);
        Snowball snowball = event.getPlayer().launchProjectile(Snowball.class);
        EnderPlugin.snowballScheduler().runSingleTask(() -> DataPlayer.canSnowBall.put(event.getPlayer(), true), "DELAY_" + snowball.getUniqueId().toString(), 0.125, serverLang().getPlugMsg());
    }

    @EventHandler
    public void run(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        Player player = (Player) event.getEntity().getShooter();
        if (player.getInventory().getItemInHand() != null && player.getInventory().getItemInHand().hasItemMeta() && player.getInventory().getItemInHand().getItemMeta().hasDisplayName() &&
                player.getInventory().getItemInHand().getItemMeta().getDisplayName().toLowerCase().contains("paint gun")) {
            snowballs.add(event.getEntity().getUniqueId());
            player.getWorld().playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 10.0F, 10.0F);
        }
    }

    @EventHandler
    public void run(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.SNOWBALL || !snowballs.contains(event.getEntity().getUniqueId())) return;
        BlockIterator blockIterator = new BlockIterator(event.getEntity().getWorld(), event.getEntity().getLocation().toVector(), event.getEntity().getVelocity().normalize(), 0.0D, 4);
        Block block = null;
        while (blockIterator.hasNext()) {
            block = blockIterator.next();
            if (block.getType() != Material.AIR) break;
        }
        if (block == null || blockLocs.contains(block.getLocation())) return;
        if (block.getState() instanceof Banner || block.getState() instanceof Beacon || block.getState() instanceof BrewingStand || block.getState() instanceof Chest ||
            block.getState() instanceof  CommandBlock || block.getState() instanceof  CreatureSpawner || block.getState() instanceof Dispenser || block.getState() instanceof Dropper ||
            block.getState() instanceof Furnace || block.getState() instanceof Hopper || block.getState() instanceof Jukebox || block.getState() instanceof NoteBlock ||
            block.getState() instanceof Sign || block.getState() instanceof Skull || block.getType() == Material.WATER || block.getType() == Material.LAVA || block.getType().name().toLowerCase().contains("torch") ||
            block.getType() == Material.STATIONARY_LAVA || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.PAINTING || block.getType() == Material.ITEM_FRAME)
            return;

        final Material m = block.getType();
        final byte b = block.getData();

        blockLocs.add(block.getLocation());

        block.setType(Material.STAINED_CLAY);
        block.setData((byte) Java.getRandom(0, 15));
        event.getEntity().getWorld().playEffect(event.getEntity().getLocation(), Effect.MOBSPAWNER_FLAMES, 10);

        Block finalBlock = block;
        EnderPlugin.snowballScheduler().runSingleTask(() -> {
            blockLocs.remove(finalBlock.getLocation());
            finalBlock.setType(m);
            finalBlock.setData(b);
        }, "RET_" + finalBlock.getLocation().getBlockX() + "_" + finalBlock.getLocation().getBlockY() + "_" + finalBlock.getLocation().getBlockZ(), 3, serverLang().getPlugMsg());
    }
}
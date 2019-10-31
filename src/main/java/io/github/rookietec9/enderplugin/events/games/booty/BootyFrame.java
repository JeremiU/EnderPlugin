package io.github.rookietec9.enderplugin.events.games.booty;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Jeremi
 * @version 14.7.7
 * @since 12.4.9
 */
public class BootyFrame implements Listener {

    private boolean run = false;

    @EventHandler
    public void run(PlayerChangedWorldEvent event) {
        if (event.getFrom().getName().equalsIgnoreCase(Utils.Reference.Worlds.BOOTY) && Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getPlayers().isEmpty()) {
            run = false;
        }

        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.BOOTY)) {
            LivingEntity entity = null;

            for (LivingEntity entity1 : event.getPlayer().getWorld().getLivingEntities()) {
                if (entity1.getType() == EntityType.GUARDIAN) entity = entity1;
            }
            if (entity == null)
                entity = (LivingEntity) event.getPlayer().getWorld().spawnEntity(new Location(event.getPlayer().getWorld(), 22.5, 5, -29.7, 359.61F, 3.105F), EntityType.GUARDIAN);
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000000 * 20, 1));
            run = true;
        }
        runFlip();
    }

    private void runFlip() {
        Location frameLoc = new Location(Bukkit.getWorld(Utils.Reference.Worlds.BOOTY), 23, 5, -19);
        ItemFrame frame = null;

        for (Entity entity : frameLoc.getWorld().getEntities())
            if (entity instanceof ItemFrame && entity.getLocation().getBlockX() == 23)
                frame = (ItemFrame) entity;

        final ItemFrame itemFrame = frame;

        if (Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getBlockAt(23, 4, -19) == null ||
                !(Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getBlockAt(23, 4, -19).getState() instanceof Sign))
            return;
        if (itemFrame == null) return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
            if (!run) return;

            switch (itemFrame.getItem().getType()) {
                case DIAMOND_BLOCK: {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        itemFrame.setItem(new ItemStack(Material.IRON_BLOCK));
                        updateSign("§f§lIRON", "§7§lJUMP II");
                        runFlip();
                    }, 60L);
                    return;
                }
                case IRON_BLOCK: {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        itemFrame.setItem(new ItemStack(Material.DIRT));
                        updateSign("§e§lDIRT", "§6§lSTRENGTH II");
                        runFlip();
                    }, 60L);
                    return;
                }
                case DIRT: {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        itemFrame.setItem(new ItemStack(Material.MYCEL));
                        updateSign("§6§lMYCELIUM", "§lINVISIBILITY");
                        runFlip();
                    }, 60L);
                    return;
                }
                case MYCEL: {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        itemFrame.setItem(new ItemStack(Material.SPONGE));
                        updateSign("§e§lSPONGE", "§f§lCLEAR EFX");
                        runFlip();
                    }, 60L);
                    return;
                }
                case SPONGE: {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        itemFrame.setItem(new ItemStack(Material.WOOD));
                        updateSign("§6§lWOOD", "§f§lDISAPPEARS");
                        runFlip();
                    }, 60L);
                    return;
                }
                case WOOD: {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        itemFrame.setItem(new ItemStack(Material.SLIME_BLOCK));
                        updateSign("§a§lSLIME", "§f§lBOUNCE");
                        runFlip();
                    }, 60L);
                    return;
                }
                case SLIME_BLOCK: {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        itemFrame.setItem(new ItemStack(Material.COAL_BLOCK));
                        updateSign("§0§lCOAL", "§f§lTP/HEAL");
                        runFlip();
                    }, 60L);
                    return;
                }
                case COAL_BLOCK: {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        itemFrame.setItem(new ItemStack(Material.STAINED_CLAY, 1, (short) 0, (byte) 6));
                        updateSign("§4§lTERRACOTTA", "§c§lFATIGUE II");
                        runFlip();
                    }, 60L);
                    return;
                }
                default:
                case STAINED_CLAY: {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        itemFrame.setItem(new ItemStack(Material.DIAMOND_BLOCK));
                        updateSign("§3§lDIAMOND", "§b§lSPEED II");
                        runFlip();
                    }, 60L);
                }
            }
        });
    }

    private void updateSign(String line1, String line2) {
        Block block = Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getBlockAt(23, 4, -19);
        if (!(block.getState() instanceof Sign)) {
            block.setType(Material.WALL_SIGN);
        }
        Sign sign = (Sign) Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getBlockAt(23, 4, -19).getState();

        if (!sign.getLine(0).equalsIgnoreCase("§f-------------") && !sign.getLine(3).equalsIgnoreCase("§f-------------")) {
            sign.setLine(0, "§f-------------");
            sign.setLine(3, "§f-------------");
        }
        sign.setLine(1, line1);
        sign.setLine(2, line2);
        sign.update(true);

        block = Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getBlockAt(24, 4, -19);
        if (!(block.getState() instanceof Sign)) {
            block.setType(Material.WALL_SIGN);
        }
        sign = (Sign) Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getBlockAt(24, 4, -19).getState();
        sign.setLine(0, "§f-------------");
        sign.setLine(3, "§f-------------");
        sign.setLine(1, "§8§lWATER WILL");
        sign.setLine(2, "§8§lKILL YOU");
        sign.update(true);
    }
}
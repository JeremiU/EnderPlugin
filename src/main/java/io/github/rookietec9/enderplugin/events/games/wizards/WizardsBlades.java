package io.github.rookietec9.enderplugin.events.games.wizards;

import com.google.common.collect.HashBiMap;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.Blades;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Used for wizards to create potion-tipped blades.
 *
 * @author Jeremi
 * @version 22.4.3
 * @since 6.6.1
 */
public class WizardsBlades implements Listener {

    public static HashBiMap<Player, Player> effectedMap = HashBiMap.create();

    @EventHandler
    public void run(EntityDamageByEntityEvent e) {
        if (e.getDamager() == null || e.getDamage() == 0 || e.getEntity() == null || !(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player) || DataPlayer.getUser((Player) e.getEntity()).getGod()) return;
        if (e.getDamager().getWorld().getName().equalsIgnoreCase(Worlds.WIZARDS) && e.getEntity().getWorld().getName().equalsIgnoreCase(Worlds.WIZARDS)) {
            Player hitter = (Player) e.getDamager();
            Player getter = (Player) e.getEntity();
            ItemStack dmgItem = hitter.getEquipment().getItemInHand();

            if (dmgItem == null || dmgItem.getItemMeta() == null || dmgItem.getItemMeta().getDisplayName() == null || dmgItem.getItemMeta().getDisplayName().length() == 0) return;

            for (Blades blade : Blades.blades) {
                if (dmgItem.getItemMeta().getDisplayName().toLowerCase().contains(blade.getName().toLowerCase()) && blade.getQuad() != null) {
                    for (Blades.Quad<PotionEffectType, Integer, Integer, Integer> ench : blade.getQuad()) {
                        if (Java.getRandom(1, 100) <= ench.chance) {
                            Player target = (Minecraft.BAD_EFFECTS().contains(ench.potionEffect)) ? getter : hitter;
                            target.addPotionEffect(new PotionEffect(ench.potionEffect, ench.duration * 20, ench.strength - 1, false, true));
                            if (ench.potionEffect == PotionEffectType.BLINDNESS) target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, ench.duration * 20, ench.strength - 1, false, true));
                            if (Minecraft.BAD_EFFECTS().contains(ench.potionEffect)) effectedMap.put(getter, hitter);
                        }
                    }
                }
            }

            registerBlade("anvil", Java.getRandom(1, 4) > 1, 4, Material.ANVIL, hitter, getter);
            registerBlade("spider", Java.getRandom(1, 4) == 1, 0, Material.WEB, hitter, getter);
            registerBlade("aqua", Java.getRandom(1, 4) == 1, 0, Material.WATER, hitter, getter);

            if (dmgItem.getItemMeta().getDisplayName().contains("Ghost") && Java.getRandom(1, 4) == 1) DataPlayer.getUser(hitter).ghost();
            if (dmgItem.getItemMeta().getDisplayName().contains("Fire") && Java.getRandom(1, 2) == 1) getter.setFireTicks(100);

            if (dmgItem.getItemMeta().getDisplayName().contains("Puncher") && Java.getRandom(1, 4) == 1) {
                Location location = getter.getLocation().clone();
                location.setY(location.getY() + 6);
                getter.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }

            int good = Java.getRandom(1, 2);
            if (Java.getRandom(1, 2) == 1) registerBlade("Uncertain", boilPot(good == 1), hitter, getter, (good == 1));
        }
    }

    private void registerBlade(String name, boolean work, int yModifier, Material type, Player damager, Player damagee) {
        if (!work) return;
        effectedMap.forcePut(damagee, damager);
        if (damager.getEquipment().getItemInHand().getItemMeta().getDisplayName().toLowerCase().contains(name)) {
            Location location = damagee.getLocation().clone();
            location.setY(location.getY() + yModifier);
            Block oldBlock = location.getBlock();

            if (oldBlock.getType() == type) return;

            Material m = oldBlock.getType();
            byte b = oldBlock.getData();
            BlockFace bf = (oldBlock instanceof Directional) ? ((Directional) oldBlock).getFacing() : null;

            oldBlock.setType(type != Material.ANVIL ? type : Material.AIR);

            FallingBlock block = type == Material.ANVIL ? Bukkit.getWorld(Worlds.WIZARDS).spawnFallingBlock(location, type, (byte) 0) : null;
            if (block != null) block.setHurtEntities(true);

            String id = "WIZARD_BLADE_" + name.toUpperCase() + "X" + oldBlock.getLocation().getBlockX() + "Y" + oldBlock.getLocation().getBlockY() + "Z" + oldBlock.getLocation().getZ();

            if (!EnderPlugin.scheduler().isRunning(id))
                EnderPlugin.scheduler().runSingleTask(() -> {
                    if (block != null && block.getLocation() != oldBlock.getLocation()) {
                        block.getLocation().getBlock().setType(Material.AIR);
                    } else {
                        oldBlock.setType(m);
                        oldBlock.setData(b);
                        if (oldBlock instanceof Directional) ((Directional) oldBlock).setFacingDirection(bf);
                    }
                }, id, 3);
        }
    }

    private PotionEffect boilPot(boolean good) {
        PotionEffectType[] goodType = {PotionEffectType.ABSORPTION, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.ABSORPTION,
                PotionEffectType.JUMP, PotionEffectType.SPEED};

        PotionEffectType[] badType = {PotionEffectType.POISON, PotionEffectType.CONFUSION, PotionEffectType.WITHER, PotionEffectType.BLINDNESS, PotionEffectType.SLOW,
                PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS};

        return new PotionEffect(good ? goodType[(Java.getRandom(0, goodType.length - 1))] : badType[(Java.getRandom(0, badType.length - 1))], (Java.getRandom(2, 5) * 20), (Java.getRandom(0, 1)));
    }

    private void registerBlade(String name, PotionEffect potionEffect, Player killerEntity, Player victimEntity, boolean positive) {
        ItemStack dmgItem = killerEntity.getEquipment().getItemInHand();
        if (dmgItem == null || dmgItem.getType() == Material.AIR || !dmgItem.getItemMeta().hasDisplayName()) return;

        if (dmgItem.getItemMeta().getDisplayName().toLowerCase().contains(name.toLowerCase())) {
            if (positive) killerEntity.addPotionEffect(potionEffect);
            else victimEntity.addPotionEffect(potionEffect);
        }
    }
}
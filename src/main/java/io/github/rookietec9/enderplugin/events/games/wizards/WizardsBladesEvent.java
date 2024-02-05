package io.github.rookietec9.enderplugin.events.games.wizards;

import com.google.common.collect.HashBiMap;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.Blades;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.rookietec9.enderplugin.Reference.PREFIX_WZRDS;
import static io.github.rookietec9.enderplugin.Reference.WIZARDS;

/**
 * Used for wizards to create potion-tipped blades.
 *
 * @author Jeremi
 * @version 25.4.9
 * @since 6.6.1
 */
public class WizardsBladesEvent implements Listener {

    public static final HashBiMap<LivingEntity, LivingEntity> effectedMap = HashBiMap.create();

    @EventHandler
    public void run(EntityDamageByEntityEvent e) {
        if (e.getDamager() == null || e.getDamage() == 0 || e.getEntity() == null || !(e.getDamager() instanceof Player) || !(e.getEntity() instanceof LivingEntity) || e.getEntity() instanceof Player && DataPlayer.getUser((Player) e.getEntity()).getGod()) return;
        if (e.getDamager().getWorld().getName().equalsIgnoreCase(WIZARDS) && e.getEntity().getWorld().getName().equalsIgnoreCase(WIZARDS) && e.getEntity().getLocation().getZ() <= -46.3) {
            Player hitter = (Player) e.getDamager();
            LivingEntity getter = (LivingEntity) e.getEntity();
            ItemStack dmgItem = hitter.getEquipment().getItemInHand();

            if (dmgItem == null || dmgItem.getItemMeta() == null || dmgItem.getItemMeta().getDisplayName() == null || dmgItem.getItemMeta().getDisplayName().length() == 0) return;

            for (Blades blade : Blades.blades) {
                if (dmgItem.getItemMeta().getDisplayName().toLowerCase().contains(blade.getName().toLowerCase()) && blade.getQuad() != null) {
                    for (Blades.Quad<PotionEffectType, Integer, Integer, Integer> ench : blade.getQuad()) {
                        if (Java.getRandom(1, 100) <= ench.chance) {
                            LivingEntity target = (Minecraft.BAD_EFFECTS().contains(ench.potionEffect)) ? getter : hitter;
                            target.addPotionEffect(new PotionEffect(ench.potionEffect, ench.duration * 20, ench.strength - 1, false, true));
                            if (ench.potionEffect == PotionEffectType.BLINDNESS) target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, ench.duration * 20, ench.strength - 1, false, true));
                            if (Minecraft.BAD_EFFECTS().contains(ench.potionEffect)) effectedMap.put(getter, hitter);
                        }
                    }
                }
            }

            registerBlade("anvil", Java.getRandom(1, 4) > 1, 2, Material.ANVIL, hitter, getter);
            registerBlade("spider", Java.getRandom(1, 4) == 1, 0, Material.WEB, hitter, getter);
            registerBlade("aqua", Java.getRandom(1, 4) == 1, 0, Material.WATER, hitter, getter);

            if (dmgItem.getItemMeta().getDisplayName().contains("Ghost") && Java.getRandom(1, 4) == 1) DataPlayer.get(hitter).ghost();
            if (dmgItem.getItemMeta().getDisplayName().contains("Fire") && Java.getRandom(1, 2) == 1) getter.setFireTicks(100);

            if (dmgItem.getItemMeta().getDisplayName().contains("Puncher") && Java.getRandom(1, 4) == 1) {
                Location location = getter.getLocation().clone();
                location.setY(location.getY() + 6);
                getter.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }

            if (dmgItem.getItemMeta().getDisplayName().contains("Knocker")) {
                getter.setVelocity(new Vector(hitter.getLocation().getX(), 1.0D, hitter.getLocation().getZ()));
                getter.setVelocity(hitter.getLocation().getDirection().multiply(DataPlayer.get(hitter).tempKnocker / 20.0));
                DataPlayer.get(hitter).tempKnocker = 0;
                final String id = "KNOCKER_CHECK_" + DataPlayer.getUser(hitter).getNickName();
                if (EnderPlugin.scheduler().isRunning(id)) EnderPlugin.scheduler().cancel(id);
            }

            int good = Java.getRandom(1, 2);
            if (Java.getRandom(1, 2) == 1) registerBlade("Uncertain", boilPot(good == 1), hitter, getter, (good == 1));
        }
    }

    @EventHandler
    public void run(PlayerInteractEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(WIZARDS) || event.getPlayer().getGameMode() != GameMode.ADVENTURE || DataPlayer.get(event.getPlayer()).tempKnocker >= 200 || event.getPlayer().getLocation().getZ() > -46.3) return;
        if (event.getItem() == null || event.getItem().getType() != Material.WOOD_SWORD || !DataPlayer.get(event.getPlayer()).tempWizardBlade.toLowerCase().contains("knocker") || event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        final String id = "KNOCKER_CHECK_" + DataPlayer.getUser(event.getPlayer()).getNickName();
        if (!EnderPlugin.scheduler().isRunning(id)) EnderPlugin.scheduler().runRepeatingTask(() -> {
            if (event.getPlayer().isBlocking() && DataPlayer.get(event.getPlayer()).tempKnocker < 200) {
                DataPlayer.get(event.getPlayer()).tempKnocker += 10;
                DataPlayer.get(event.getPlayer()).sendActionMsg("§9" + StringUtils.repeat("■", DataPlayer.get(event.getPlayer()).tempKnocker / 10) + "§c" + StringUtils.repeat("■", 20 - DataPlayer.get(event.getPlayer()).tempKnocker / 10));
            }
        }, id, 0, 0.125, PREFIX_WZRDS);
    }

    @EventHandler
    public void run(PlayerFishEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(WIZARDS) || event.getCaught() == null || !(event.getCaught() instanceof LivingEntity) || event.getPlayer().getLocation().getZ() > -46.3) return;
        LivingEntity caught = (LivingEntity) event.getCaught();
        caught.setVelocity(new Vector(caught.getLocation().getX(), 1.0D, caught.getLocation().getZ()));
        caught.setVelocity(event.getPlayer().getLocation().getDirection().multiply(-0.5));
    }

    private void registerBlade(String name, boolean work, int yModifier, Material type, Player damager, LivingEntity damagee) {
        if (!work) return;
        effectedMap.forcePut(damagee, damager);
        if (damager.getEquipment().getItemInHand().getItemMeta().getDisplayName().toLowerCase().contains(name)) {
            Location location = damagee.getLocation().clone();
            location.setY(location.getY() + yModifier);
            Block oldBlock = location.getBlock();

            if (oldBlock.getType() == type) return;

            AtomicReference<Material> mat = new AtomicReference<>(oldBlock.getType());
            AtomicInteger data = new AtomicInteger(oldBlock.getData());

            BlockFace bf = (oldBlock instanceof Directional) ? ((Directional) oldBlock).getFacing() : null;

            oldBlock.setType(type != Material.ANVIL ? type : Material.AIR);

            FallingBlock block = type == Material.ANVIL ? Bukkit.getWorld(WIZARDS).spawnFallingBlock(location, type, (byte) 0) : null;
            if (block != null) block.setHurtEntities(true);

            String id = "WIZARD_BLADE_" + name.toUpperCase() + "X" + oldBlock.getLocation().getBlockX() + "Y" + oldBlock.getLocation().getBlockY() + "Z" + oldBlock.getLocation().getZ();

            if (!EnderPlugin.scheduler().isRunning(id))
                EnderPlugin.scheduler().runSingleTask(() -> {
                    if (block != null && !oldBlock.getLocation().equals(block.getLocation())) {
                        mat.set(block.getLocation().getBlock().getType());
                        data.set(block.getLocation().getBlock().getData());
                    }

                    if (mat.get() == Material.ANVIL) mat.set(Material.AIR);

                    if (block != null && block.getLocation().getY() % 1.0 == 0) {
                        block.getLocation().getBlock().setType(Material.AIR);
                    } else if (block != null) {
                        block.getLocation().getBlock().setType(mat.get());
                        block.getLocation().getBlock().setData(data.byteValue());
                    } else {
                        oldBlock.setType(mat.get());
                        oldBlock.setData(data.byteValue());
                        if (oldBlock instanceof Directional) ((Directional) oldBlock).setFacingDirection(bf);
                    }
                }, id, 3, PREFIX_WZRDS);
        }
    }

    private PotionEffect boilPot(boolean good) {
        PotionEffectType[] goodType = {PotionEffectType.ABSORPTION, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.ABSORPTION,
                PotionEffectType.JUMP, PotionEffectType.SPEED};

        PotionEffectType[] badType = {PotionEffectType.POISON, PotionEffectType.CONFUSION, PotionEffectType.WITHER, PotionEffectType.BLINDNESS, PotionEffectType.SLOW,
                PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS};

        return new PotionEffect(good ? goodType[(Java.getRandom(0, goodType.length - 1))] : badType[(Java.getRandom(0, badType.length - 1))], (Java.getRandom(2, 5) * 20), (Java.getRandom(0, 1)));
    }

    private void registerBlade(String name, PotionEffect potionEffect, Player killerEntity, LivingEntity victimEntity, boolean positive) {
        ItemStack dmgItem = killerEntity.getEquipment().getItemInHand();
        if (dmgItem == null || dmgItem.getType() == Material.AIR || !dmgItem.getItemMeta().hasDisplayName()) return;

        if (dmgItem.getItemMeta().getDisplayName().toLowerCase().contains(name.toLowerCase())) {
            if (positive) killerEntity.addPotionEffect(potionEffect);
            else victimEntity.addPotionEffect(potionEffect);
        }
    }
}
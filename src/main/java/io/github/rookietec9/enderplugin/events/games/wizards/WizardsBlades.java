package io.github.rookietec9.enderplugin.events.games.wizards;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Used for wizards to create potion-tipped blades.
 *
 * @author Jeremi
 * @version 16.3.7
 * @since 6.6.1
 */
public class WizardsBlades implements Listener {

    @EventHandler
    public void run(EntityDamageByEntityEvent e) {
        if (e.getDamager() == null || e.getDamage() == 0 || e.getEntity() == null)
            return;

        if (e.getDamager().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.WIZARDS) && e.getEntity().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.WIZARDS)) {
            if (!(e.getDamager() instanceof LivingEntity)) return;

            LivingEntity hitter = (LivingEntity) e.getDamager();
            LivingEntity getter = (LivingEntity) e.getEntity();

            if (getter instanceof Player && new User((Player) getter).getGod()) return;

            ItemStack dmgItem = hitter.getEquipment().getItemInHand();

            if (dmgItem == null || dmgItem.getItemMeta() == null || dmgItem.getItemMeta().getDisplayName() == null || dmgItem.getItemMeta().getDisplayName().length() == 0)
                return;
            if (getter instanceof Player && new User((Player) getter).getGod())
                return;

            registerBlade("Nausea", new PotionEffect(PotionEffectType.CONFUSION, 200, 9, false, true), hitter, getter, DyeColor.PURPLE, ChatColor.DARK_PURPLE, false);
            registerBlade("Poison", new PotionEffect(PotionEffectType.POISON, 60, 1, false, true), hitter, getter, DyeColor.GREEN, ChatColor.DARK_GREEN, false);
            registerBlade("Tank", new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1, false, true), hitter, getter, DyeColor.GRAY, ChatColor.DARK_GRAY, true);
            registerBlade("Slow", new PotionEffect(PotionEffectType.SLOW, 100, 0, false, true), hitter, getter, DyeColor.GRAY, ChatColor.GRAY, false);
            registerBlade("Gapple", new PotionEffect(PotionEffectType.ABSORPTION, 120, 0, false, true), hitter, getter, DyeColor.YELLOW, ChatColor.YELLOW, true);
            registerBlade("Speedy", new PotionEffect(PotionEffectType.SPEED, 60, 1, false, true), hitter, getter, DyeColor.LIGHT_BLUE, ChatColor.AQUA, true);
            registerBlade("Jump", new PotionEffect(PotionEffectType.JUMP, 60, 1, false, true), hitter, getter, DyeColor.LIME, ChatColor.GREEN, true);
            registerBlade("Health", new PotionEffect(PotionEffectType.REGENERATION, 60, 1, false, true), hitter, getter, DyeColor.PINK, ChatColor.LIGHT_PURPLE, true);

            int good = Utils.getRandom(1, 2);

            if (Utils.getRandom(1, 4) == 1) registerBlade("Weak", new PotionEffect(PotionEffectType.WEAKNESS, 60, 0, false, true), hitter, getter, DyeColor.GRAY, ChatColor.DARK_GRAY, true);
            if (Utils.getRandom(1, 5) == 1) registerBlade("Power", new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0, false, true), hitter, getter, DyeColor.RED, ChatColor.DARK_RED, true);
            if (Utils.getRandom(1, 2) == 1) registerBlade("Uncertain", boilPot(good == 1), hitter, getter, DyeColor.GRAY, ChatColor.DARK_GRAY, (good == 1));

            if (dmgItem.getItemMeta().getDisplayName().contains("Puncher")) {
                if (Utils.getRandom(1, 4) == 1) {
                    Location location = getter.getLocation().clone();
                    location.setY(location.getY() + 6);
                    getter.teleport(location);
                    if (getter instanceof Sheep) {
                        getter.teleport(location);
                        ((Sheep) getter).setColor(DyeColor.RED);
                        if (getter.setPassenger(hitter)) hitter.sendMessage("§fEnjoy your new §cFriend§f!");
                    }
                }
            }
            if (dmgItem.getItemMeta().getDisplayName().contains("Anvil")) {
                int random = Utils.getRandom(1, 4);
                if (random == 1 || random == 2 || random == 3) {
                    Location location = getter.getLocation().clone();
                    location.setY(location.getY() + Utils.getRandom(4, 8));
                    location.getBlock().setType(Material.ANVIL, true);
                    Block block = getter.getLocation().getBlock();
                    Material material = block.getType();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        if (material != Material.ANVIL) block.setType(material);
                        else block.setType(Material.AIR);
                    }, 3 * 20);
                    if (getter instanceof Sheep) {
                        ((Sheep) getter).setColor(DyeColor.GRAY);
                        if (getter.setPassenger(hitter)) hitter.sendMessage("§fEnjoy your new §8Friend§f!");
                    }
                }
            }

            if (dmgItem.getItemMeta().getDisplayName().toLowerCase().contains("ghost")) {
                if (hitter instanceof Player) {
                    Player hitMan = (Player) hitter;
                    int blade = hitMan.getInventory().getHeldItemSlot(), boots = 32, leggings = 33, chestPlate = 34, helmet = 35;
                    if (Utils.getRandom(1, 4) == 1) {
                        hitMan.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (5 * 20), 0, false, false), true);
                        if (hitMan.getInventory().getHelmet() != null) {
                            hitMan.getInventory().setItem(helmet, hitMan.getInventory().getHelmet());
                            hitMan.getInventory().setHelmet(new ItemStack(Material.AIR));
                        }

                        if (hitMan.getInventory().getItem(0) != null) {
                            hitMan.getInventory().setItem(blade, hitMan.getInventory().getItem(0));
                            hitMan.getInventory().setItem(0, new ItemStack(Material.AIR));
                        }

                        if (hitMan.getInventory().getChestplate() != null) {
                            hitMan.getInventory().setItem(chestPlate, hitMan.getInventory().getChestplate());
                            hitMan.getInventory().setChestplate(new ItemStack(Material.AIR));
                        }
                        if (hitMan.getInventory().getLeggings() != null) {
                            hitMan.getInventory().setItem(leggings, hitMan.getInventory().getLeggings());
                            hitMan.getInventory().setLeggings(new ItemStack(Material.AIR));
                        }
                        if (hitMan.getInventory().getBoots() != null) {
                            hitMan.getInventory().setItem(boots, hitMan.getInventory().getBoots());
                            hitMan.getInventory().setBoots(new ItemStack(Material.AIR));
                        }
                        if (getter instanceof Sheep) {
                            ((Sheep) getter).setColor(DyeColor.GRAY);
                            if (getter.setPassenger(hitter)) hitter.sendMessage("§fEnjoy your new §8Friend§f!");
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                            if (hitMan.getInventory().getItem(blade) != null) {
                                hitMan.getInventory().setItem(0, hitMan.getInventory().getItem(blade));
                                hitMan.getInventory().setHeldItemSlot(0);
                                hitMan.getInventory().setItem(blade, new ItemStack(Material.AIR));
                            }

                            if (hitMan.getInventory().getItem(helmet) != null) {
                                hitMan.getInventory().setHelmet(hitMan.getInventory().getItem(helmet));
                                hitMan.getInventory().setItem(helmet, new ItemStack(Material.AIR));
                            }
                            if (hitMan.getInventory().getItem(chestPlate) != null) {
                                hitMan.getInventory().setChestplate(hitMan.getInventory().getItem(chestPlate));
                                hitMan.getInventory().setItem(chestPlate, new ItemStack(Material.AIR));
                            }
                            if (hitMan.getInventory().getItem(leggings) != null) {
                                hitMan.getInventory().setLeggings(hitMan.getInventory().getItem(leggings));
                                hitMan.getInventory().setItem(leggings, new ItemStack(Material.AIR));
                            }
                            if (hitMan.getInventory().getItem(boots) != null) {
                                hitMan.getInventory().setBoots(hitMan.getInventory().getItem(boots));
                                hitMan.getInventory().setItem(boots, new ItemStack(Material.AIR));
                            }

                        }, 5 * 20);
                    }
                }
            }

            if (dmgItem.getItemMeta().getDisplayName().contains("Fire")) {
                if (Utils.getRandom(1, 4) == 1) {
                    getter.setFireTicks(100);
                    if (getter instanceof Sheep) {
                        ((Sheep) getter).setColor(DyeColor.ORANGE);
                        if (getter.setPassenger(hitter)) hitter.sendMessage("§fEnjoy your new §6Friend§f!");
                    }
                }
            }

            if (dmgItem.getItemMeta().getDisplayName().contains("Web")) {
                if (Utils.getRandom(1, 4) == 1) {
                    Block block = getter.getLocation().getBlock();
                    Material matToRecover = block.getType();
                    byte byteToRecover = block.getData();
                    block.setType(Material.WEB);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        block.setType(matToRecover);
                        block.setData(byteToRecover);
                    }, 5 * 20);

                    if (getter instanceof Sheep) {
                        ((Sheep) getter).setColor(DyeColor.GRAY);
                        if (getter.setPassenger(hitter)) hitter.sendMessage("§fEnjoy your new §7Friend§f!");
                    }
                }
            }

            if (dmgItem.getItemMeta().getDisplayName().contains("Aqua")) {
                if (Utils.getRandom(1, 4) == 1) {
                    Block block = getter.getLocation().getBlock();
                    Material matToRecover = block.getType();
                    byte byteToRecover = block.getData();
                    block.setType(Material.WATER);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        block.setType(matToRecover);
                        block.setData(byteToRecover);
                    }, 3 * 20);
                }
                if (getter instanceof Sheep) {
                    ((Sheep) getter).setColor(DyeColor.CYAN);
                    if (getter.setPassenger(hitter)) hitter.sendMessage("§fEnjoy your new §3Friend§f!");
                }
            }
        }
    }

    private PotionEffect boilPot(boolean good) {
        PotionEffectType[] goodType = {PotionEffectType.ABSORPTION, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.ABSORPTION,
                PotionEffectType.JUMP, PotionEffectType.SPEED};

        PotionEffectType[] badType = {PotionEffectType.POISON, PotionEffectType.CONFUSION, PotionEffectType.WITHER, PotionEffectType.BLINDNESS, PotionEffectType.SLOW,
                PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS};
        if (good)
            return new PotionEffect(goodType[(Utils.getRandom(0, goodType.length - 1))], (Utils.getRandom(2, 5) * 20), (Utils.getRandom(0, 2)));
        else
            return new PotionEffect(badType[(Utils.getRandom(0, badType.length - 1))], (Utils.getRandom(2, 5) * 20), (Utils.getRandom(0, 1)));
    }

    private void registerBlade(String name, PotionEffect potionEffect, LivingEntity killerEntity, LivingEntity victimEntity, DyeColor sheepColor, ChatColor talkColor, boolean positive) {
        ItemStack dmgItem = killerEntity.getEquipment().getItemInHand();

        if (dmgItem.getItemMeta().getDisplayName().toLowerCase().contains(name.toLowerCase())) {
            if (positive) killerEntity.addPotionEffect(potionEffect);
            else victimEntity.addPotionEffect(potionEffect);

            if (victimEntity instanceof Sheep) {
                ((Sheep) victimEntity).setColor(sheepColor);
                if ((victimEntity).setPassenger(killerEntity))
                    killerEntity.sendMessage("§fEnjoy your new " + talkColor + "Friend§f!");
            }
        }
    }
}
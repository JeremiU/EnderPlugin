package io.github.rookietec9.enderplugin.events.games.esg;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 5.0.4
 */
public class ESGEggEvent implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() == null) return;
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.ESG_FIGHT) && !e.getPlayer().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUNGER)) {
            return;
        }
        if (!e.getItem().getItemMeta().hasDisplayName()) return;

        if (e.getItem().getType() == Material.BONE && e.getItem().getItemMeta().getDisplayName().contains("Compressed Wolf")) {
            e.setCancelled(true);
            Wolf wolf = (Wolf) p.getWorld().spawnEntity(new Location(p.getWorld(), e.getClickedBlock().getX(), e.getClickedBlock().getY() + 2, e.getClickedBlock().getZ()), EntityType.WOLF);
            wolf.setCustomName(p.getUniqueId().toString());
            wolf.setCustomNameVisible(false);
            wolf.setOwner(p);
            wolf.setAdult();
            wolf.setRemoveWhenFarAway(false);
            wolf.setAngry(true);
            clearItem(e.getPlayer(), Material.BONE, "Compressed Wolf");

            wolf.setCollarColor(DyeColor.values()[Utils.getRandom(0, (DyeColor.values().length - 1))]);
        }

        if (e.getItem().getType() == Material.SADDLE && e.getItem().getItemMeta().getDisplayName().contains("Compressed Horse")) {
            e.setCancelled(true);
            Horse horse = (Horse) p.getWorld().spawnEntity(new Location(p.getWorld(), e.getClickedBlock().getX(), e.getClickedBlock().getY() + 2, e.getClickedBlock().getZ()), EntityType.HORSE);
            horse.setCustomName(p.getUniqueId().toString());
            horse.setCustomNameVisible(false);

            horse.setMaxHealth(20.0);
            horse.setHealth(horse.getMaxHealth());
            horse.setJumpStrength(horse.getJumpStrength() * 2);
            horse.setOwner(p);
            horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (1000000 * 20), 2));
            horse.setCarryingChest(false);
            horse.setOwner(e.getPlayer());
            horse.setAdult();
            horse.setTamed(true);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            horse.setVariant(Horse.Variant.HORSE);
            clearItem(e.getPlayer(), Material.SADDLE, "Compressed Horse");

            horse.setColor(Horse.Color.values()[Utils.getRandom(0, Horse.Color.values().length - 1)]);
            horse.setStyle(Horse.Style.values()[Utils.getRandom(0, Horse.Style.values().length - 1)]);
        }

        if (e.getItem().getType() == Material.MAGMA_CREAM
                && e.getItem().getItemMeta().getDisplayName().contains("Compressed Cube")) {
            e.setCancelled(true);
            MagmaCube slime = (MagmaCube) p.getWorld().spawnEntity(new Location(p.getWorld(), (double) e.getClickedBlock().getX(),
                    (double) e.getClickedBlock().getY() + 2, (double) e.getClickedBlock().getZ()), EntityType.MAGMA_CUBE);
            slime.setCustomName(e.getPlayer().getUniqueId().toString());
            slime.setCustomNameVisible(false);
            slime.setSize(3);
            slime.setHealth(slime.getMaxHealth() - 2);
            clearItem(e.getPlayer(), Material.MAGMA_CREAM, "Compressed Cube");
        }

        if (e.getItem().getType() == Material.RECORD_12 && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).contains("Random Positive Potion")) {
            e.setCancelled(true);
            clearItem(e.getPlayer(), Material.RECORD_12, "§fRandom §bPositive§f Potion (§bDrink§f)");
            boilPot(p, true);
        } //BLUE POTION
        if (e.getItem().getType() == Material.RECORD_3 && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).contains("Random Negative Potion")) {
            e.setCancelled(true);
            clearItem(e.getPlayer(), Material.RECORD_3, "§fRandom §cNegative§f Potion (§cSplash§f)");
            boilPot(p, false);
        } //RED POTION

        if (e.getItem().getType() == Material.PUMPKIN && e.getItem().getItemMeta().getDisplayName().contains("Compressed Baller")) {
            e.setCancelled(true);
            Snowman baller = (Snowman) p.getWorld().spawnEntity(new Location(p.getWorld(), e.getClickedBlock().getX(), e.getClickedBlock().getY() + 2, e.getClickedBlock().getZ()), EntityType.SNOWMAN);
            baller.setCustomName(p.getUniqueId().toString());
            baller.setMaxHealth(20.0);
            baller.setHealth(20.0);
            baller.setRemoveWhenFarAway(false);
            baller.setCustomNameVisible(false);
            clearItem(e.getPlayer(), Material.PUMPKIN, "Compressed Baller");
        }
    }

    private void boilPot(Player p, boolean good) {
        ItemStack pot = new ItemStack(Material.POTION);
        PotionMeta potMeta = (PotionMeta) pot.getItemMeta();
        PotionEffectType[] goodType = {PotionEffectType.ABSORPTION, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.HEALTH_BOOST, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.ABSORPTION,
                PotionEffectType.JUMP, PotionEffectType.SPEED, PotionEffectType.WATER_BREATHING};

        PotionEffectType[] badType = {PotionEffectType.POISON, PotionEffectType.CONFUSION, PotionEffectType.WITHER, PotionEffectType.BLINDNESS, PotionEffectType.SLOW,
                PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS};

        if (good) pot.setDurability((short) 8258);
        if (!good) pot.setDurability((short) 16460);

        if (good) potMeta.setDisplayName("§b+§f Potion (§bDrink§f)");
        if (!good) potMeta.setDisplayName("§c-§f Potion (§cNegative§f)");

        if (good)
            potMeta.addCustomEffect(new PotionEffect(goodType[(Utils.getRandom(0, goodType.length - 1))], (Utils.getRandom(5, 20) * 20), (Utils.getRandom(0, 3))), true);
        if (!good)
            potMeta.addCustomEffect(new PotionEffect(badType[(Utils.getRandom(0, badType.length - 1))], (Utils.getRandom(5, 20) * 20), (Utils.getRandom(0, 2))), true);

        pot.setItemMeta(potMeta);
        Potion potion = Potion.fromItemStack(pot);

        if (!good) potion.setSplash(true);
        potion.apply(pot);

        p.getInventory().setItem(p.getInventory().getHeldItemSlot(), pot);
    }

    private void clearItem(Player p, Material toRemove, String nameToRemove) {
        for (int i = 0; i < p.getInventory().getContents().length; i++) {
            if (p.getInventory().getContents()[i] == null
                    || p.getInventory().getContents()[i].getType() != toRemove)
                continue;
            if (p.getInventory().getContents()[i].getType() == toRemove) {
                if (p.getInventory().getContents()[i].getAmount() != 0) {
                    if (p.getInventory().getContents()[i].getItemMeta().hasDisplayName() && p.getInventory().getContents()[i].getItemMeta().getDisplayName().contains(nameToRemove)) {
                        ItemStack toDel = p.getInventory().getContents()[i];
                        toDel.setAmount(p.getInventory().getContents()[i].getAmount() - 1);
                        p.getInventory().remove(p.getInventory().getContents()[i]);
                        p.getInventory().addItem(toDel);
                    }
                }
            }
        }
    }
}
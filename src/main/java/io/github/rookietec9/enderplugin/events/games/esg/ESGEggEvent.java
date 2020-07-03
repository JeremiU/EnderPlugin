package io.github.rookietec9.enderplugin.events.games.esg;

import io.github.rookietec9.enderplugin.entities.ESGBlaze;
import io.github.rookietec9.enderplugin.entities.ESGHorse;
import io.github.rookietec9.enderplugin.entities.ESGSnowMan;
import io.github.rookietec9.enderplugin.entities.ESGWolf;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.TargetMapper;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


/**
 * @author Jeremi
 * @version 22.8.0
 * @since 5.0.4
 */
public class ESGEggEvent implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Worlds.ESG_FIGHT) && event.getPlayer().getGameMode() == GameMode.ADVENTURE && event.getItem() != null) {
            if (event.getItem().getType() == Material.BONE && DataPlayer.getUser(p).remove(Material.BONE, 1, "compressed wolf")) {
                Wolf wolf = (Wolf) Minecraft.spawn(new ESGWolf(event.getPlayer().getWorld()), event.getPlayer().getLocation());
                wolf.setOwner(event.getPlayer());

                if (Teams.getTeam(event.getPlayer()) == null) return;
                String toColor = Teams.getTeam(event.getPlayer()).getName();
                toColor = toColor.replace("team-", "").replace("white", "yellow").toUpperCase();

                wolf.setCollarColor(DyeColor.valueOf(toColor));
            }
            if (event.getItem().getType() == Material.SADDLE && DataPlayer.getUser(p).remove(Material.SADDLE, 1, "compressed horse")) {
                Horse horse = (Horse) Minecraft.spawn(new ESGHorse(event.getPlayer().getWorld()), event.getPlayer().getLocation());
                horse.setJumpStrength(Math.random() * 2);
                if (horse.getJumpStrength() < 1) horse.setJumpStrength(2 * horse.getJumpStrength());
                horse.setOwner(p);
                horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (10000 * 20), 2));
                horse.setOwner(event.getPlayer());
                horse.setAdult();
                horse.setTamed(true);
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                TargetMapper.getTMP(horse).setOwners(event.getPlayer());
            }
            if (event.getItem().getType() == Material.MAGMA_CREAM && DataPlayer.getUser(p).remove(Material.MAGMA_CREAM, 1, "compressed cube")) {

            }
            if (event.getItem().getType() == Material.PUMPKIN && DataPlayer.getUser(p).remove(Material.PUMPKIN, 1, "compressed baller")) {
                LivingEntity snowDude = Minecraft.spawn(new ESGSnowMan(event.getPlayer().getWorld()), event.getPlayer().getLocation());

                TargetMapper tmp = TargetMapper.getTMP(snowDude);
                tmp.setOwners(event.getPlayer()).setTarget(tmp.closestTarget());
            }
            if (event.getItem().getType() == Material.BLAZE_ROD && DataPlayer.getUser(p).remove(Material.BLAZE_ROD, 1, "compressed fire")) {
                LivingEntity blazeDude = Minecraft.spawn(new ESGBlaze(event.getPlayer().getWorld()), event.getPlayer().getLocation());

                TargetMapper tmp = TargetMapper.getTMP(blazeDude);
                tmp.setOwners(event.getPlayer()).setTarget(tmp.closestTarget());
            }
/*
            if (Java.argWorks(event.getItem().getType().toString(), Material.RECORD_12.toString(), Material.RECORD_3.toString(), Material.PUMPKIN.toString(), Material.MAGMA_CREAM.toString(), Material.SADDLE.toString(), Material.BONE.toString())) {
                event.setCancelled(true);
                if (event.getItem().getAmount() > 1) event.getItem().setAmount(event.getItem().getAmount() - 1);
                else event.getPlayer().getInventory().remove(event.getItem());
            }   */
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

        if (good) potMeta.addCustomEffect(new PotionEffect(goodType[(Java.getRandom(0, goodType.length - 1))], (Java.getRandom(5, 20) * 20), (Java.getRandom(0, 3))), true);
        if (!good) potMeta.addCustomEffect(new PotionEffect(badType[(Java.getRandom(0, badType.length - 1))], (Java.getRandom(5, 20) * 20), (Java.getRandom(0, 2))), true);

        pot.setItemMeta(potMeta);
        Potion potion = Potion.fromItemStack(pot);

        if (!good) potion.setSplash(true);
        potion.apply(pot);

        p.getInventory().setItem(p.getInventory().getHeldItemSlot(), pot);
    }
}
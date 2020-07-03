package io.github.rookietec9.enderplugin.utils.methods;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.entities.CustomMob;
import net.minecraft.server.v1_8_R3.EntityCreature;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * @author Jeremi
 * @version 22.4.1
 */
public interface Minecraft {

    static List<String> getVersions() {
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList("Pre", "Classic", "Indev", "Infdev", "Seecret", "Survtest", "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Omicron", "Pi", "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega"));
        return list;
    }

    static Set<Material> HOLLOW_MATERIALS() {
        Set<Material> HOLLOW_MATERIALS = new HashSet<>();
        HOLLOW_MATERIALS.add(Material.AIR);
        HOLLOW_MATERIALS.add(Material.SAPLING);
        HOLLOW_MATERIALS.add(Material.POWERED_RAIL);
        HOLLOW_MATERIALS.add(Material.DETECTOR_RAIL);
        HOLLOW_MATERIALS.add(Material.LONG_GRASS);
        HOLLOW_MATERIALS.add(Material.DEAD_BUSH);
        HOLLOW_MATERIALS.add(Material.YELLOW_FLOWER);
        HOLLOW_MATERIALS.add(Material.RED_ROSE);
        HOLLOW_MATERIALS.add(Material.BROWN_MUSHROOM);
        HOLLOW_MATERIALS.add(Material.RED_MUSHROOM);
        HOLLOW_MATERIALS.add(Material.TORCH);
        HOLLOW_MATERIALS.add(Material.REDSTONE_WIRE);
        HOLLOW_MATERIALS.add(Material.SEEDS);
        HOLLOW_MATERIALS.add(Material.SIGN_POST);
        HOLLOW_MATERIALS.add(Material.WOODEN_DOOR);
        HOLLOW_MATERIALS.add(Material.LADDER);
        HOLLOW_MATERIALS.add(Material.RAILS);
        HOLLOW_MATERIALS.add(Material.WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.LEVER);
        HOLLOW_MATERIALS.add(Material.STONE_PLATE);
        HOLLOW_MATERIALS.add(Material.IRON_DOOR_BLOCK);
        HOLLOW_MATERIALS.add(Material.WOOD_PLATE);
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_OFF);
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_ON);
        HOLLOW_MATERIALS.add(Material.STONE_BUTTON);
        HOLLOW_MATERIALS.add(Material.SNOW);
        HOLLOW_MATERIALS.add(Material.SUGAR_CANE_BLOCK);
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_OFF);
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_ON);
        HOLLOW_MATERIALS.add(Material.PUMPKIN_STEM);
        HOLLOW_MATERIALS.add(Material.MELON_STEM);
        HOLLOW_MATERIALS.add(Material.VINE);
        HOLLOW_MATERIALS.add(Material.FENCE_GATE);
        HOLLOW_MATERIALS.add(Material.WATER_LILY);
        HOLLOW_MATERIALS.add(Material.NETHER_WARTS);
        HOLLOW_MATERIALS.add(Material.CARPET);

        return HOLLOW_MATERIALS;
    }

    static Set<PotionEffectType> BAD_EFFECTS() {
        Set<PotionEffectType> BAD_EFFECTS = new HashSet<>();
        BAD_EFFECTS.add(PotionEffectType.BLINDNESS);
        BAD_EFFECTS.add(PotionEffectType.CONFUSION);
        BAD_EFFECTS.add(PotionEffectType.HARM);
        BAD_EFFECTS.add(PotionEffectType.HUNGER);
        BAD_EFFECTS.add(PotionEffectType.POISON);
        BAD_EFFECTS.add(PotionEffectType.SLOW);
        BAD_EFFECTS.add(PotionEffectType.SLOW_DIGGING);
        BAD_EFFECTS.add(PotionEffectType.WEAKNESS);
        BAD_EFFECTS.add(PotionEffectType.WITHER);
        return BAD_EFFECTS;
    }

    static String getMatName(Material material) {
        String name = Java.upSlash(material.toString());

        if (name.contains("Leather")) name = name.replace("Helmet", "Cap").replace("Chestplate", "Tunic").replace("Leggings", "Pants");
        return Java.capFirst(name.replace("Chainmail", "Chain"));
    }

    static int getArmorPoints(ItemStack armorItem) {
        return switch (armorItem.getType()) {
            case LEATHER_BOOTS, LEATHER_HELMET, GOLD_BOOTS, CHAINMAIL_BOOTS -> 1;
            case GOLD_HELMET, LEATHER_LEGGINGS, IRON_BOOTS, CHAINMAIL_HELMET -> 2;
            case DIAMOND_HELMET, DIAMOND_BOOTS, GOLD_LEGGINGS, LEATHER_CHESTPLATE -> 3;
            case CHAINMAIL_LEGGINGS -> 4;
            case CHAINMAIL_CHESTPLATE, GOLD_CHESTPLATE, IRON_LEGGINGS -> 5;
            case IRON_CHESTPLATE, DIAMOND_LEGGINGS -> 6;
            case DIAMOND_CHESTPLATE -> 8;
            default -> 0;
        };
    }

    static double getWeaponDamage(ItemStack itemStack) {
        double xtraDamage = (itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > 0) ? (itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL) * 1.25) : 0;

        return switch (itemStack.getType()) {
            case GOLD_SPADE, WOOD_SPADE -> 1 + xtraDamage;
            case STONE_SPADE, GOLD_PICKAXE, WOOD_PICKAXE -> 2 + xtraDamage;
            case STONE_PICKAXE, WOOD_AXE, GOLD_AXE, IRON_SPADE -> 3 + xtraDamage;
            case DIAMOND_SPADE, STONE_AXE, IRON_PICKAXE, WOOD_SWORD, GOLD_SWORD -> 4 + xtraDamage;
            case DIAMOND_PICKAXE, IRON_AXE, STONE_SWORD -> 5 + xtraDamage;
            case DIAMOND_AXE, IRON_SWORD -> 6 + xtraDamage;
            case DIAMOND_SWORD -> 7 + xtraDamage;
            default -> xtraDamage;
        };
    }

    static void worldBroadcast(World world, String broadcast) {
        if (world == null) return;
        for (Player player : world.getPlayers()) player.sendMessage(broadcast);
    }

    static void worldBroadcast(String worldName, String broadcast) {
        worldBroadcast(Bukkit.getWorld(worldName), broadcast);
    }

    static String versionInfo(VerType verType) {
        return versionInfo(EnderPlugin.getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().toString(), verType);
    }

    static String versionInfo(String urlString, VerType verType) {
        String name = urlString.substring(EnderPlugin.getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().toString().lastIndexOf("/") + 1);
        String verCycle = name.substring(name.lastIndexOf("-") + 1).replace(EnderPlugin.getInstance().getName(), "").replace(".jar", "");
        for (int i = 0; i <= 10; i++) verCycle = verCycle.replace(String.valueOf(i), "");
        verCycle = verCycle.replace(".", "");
        String verNum = name.replace(verCycle, "").substring(name.indexOf("-") + 1).replace(".jar", "");

        return switch (verType) {
            case NUM -> verNum;
            case CYCLE -> verCycle;
            case NUM_NO_DOTS -> verNum.replace(".", "");
            default -> name;
        };
    }

    static LivingEntity spawn(CustomMob mob, Location location) {

        EntityCreature creature = mob.spawn(location.getWorld()).creature();
        creature.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) creature.getBukkitEntity()).setRemoveWhenFarAway(false);
        ((CraftWorld) location.getWorld()).addEntity(creature, CreatureSpawnEvent.SpawnReason.CUSTOM);

        return (LivingEntity) creature.getBukkitEntity();
    }

    static Color translateChatColorToColor(ChatColor chatColor) {
        return switch (chatColor) {
            case AQUA -> Color.AQUA;
            case BLACK -> Color.BLACK;
            case BLUE, DARK_BLUE -> Color.BLUE;
            case DARK_AQUA -> DyeColor.CYAN.getColor();
            case DARK_GRAY, GRAY -> Color.GRAY;
            case DARK_GREEN, GREEN -> Color.GREEN;
            case DARK_PURPLE, LIGHT_PURPLE -> Color.PURPLE;
            case DARK_RED, RED -> Color.RED;
            case GOLD, YELLOW -> Color.YELLOW;
            case WHITE -> Color.WHITE;
            default -> null;
        };
    }

    static String translateEffectName(PotionEffectType type) {
        return switch (type.getName()) {
            case "SLOW" -> "SLOWNESS";
            case "FAST_DIGGING" -> "HASTE";
            case "INCREASE_DAMAGE" -> "STRENGTH";
            case "HARM" -> "INSTANT DAMAGE";
            case "JUMP" -> "JUMP BOOST";
            case "CONFUSION" -> "NAUSEA";
            case "DAMAGE_RESISTANCE" -> "RESISTANCE";

            default -> Java.upSlash(type.getName()).toUpperCase();
        };
    }

    static String tacc(String message) {
        return ChatColor.translateAlternateColorCodes('&', message).replace("&r", "&7").replace("&R", "&7");
    }

    enum VerType {CYCLE, NUM, JARNAME, NUM_NO_DOTS}
}
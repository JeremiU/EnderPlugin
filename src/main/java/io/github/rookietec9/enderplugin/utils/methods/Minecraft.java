package io.github.rookietec9.enderplugin.utils.methods;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.entities.CustomMob;
import io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static io.github.rookietec9.enderplugin.Inventories.*;
import static io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem.PartyTeam;

/**
 * @author Jeremi
 * @version 25.7.3
 */
public interface Minecraft {

    static List<String> getVersions() {
        return Arrays.asList("Pre", "Classic", "Indev", "Infdev", "Seecret", "Survtest", "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Omicron", "Pi", "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega");
    }

    static Set<Material> HOLLOW_MATERIALS() {
        return new HashSet<>(Arrays.asList(Material.AIR, Material.SAPLING, Material.POWERED_RAIL, Material.DETECTOR_RAIL,
                Material.LONG_GRASS, Material.DEAD_BUSH, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.TORCH,
                Material.REDSTONE_WIRE, Material.SEEDS, Material.SIGN_POST, Material.WOODEN_DOOR, Material.LADDER, Material.RAILS, Material.WALL_SIGN, Material.LEVER,
                Material.STONE_PLATE, Material.IRON_DOOR_BLOCK, Material.WOOD_PLATE, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.STONE_BUTTON,
                Material.SNOW, Material.SUGAR_CANE_BLOCK, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.PUMPKIN_STEM, Material.MELON_STEM, Material.VINE,
                Material.FENCE_GATE, Material.WATER_LILY,  Material.NETHER_WARTS, Material.CARPET
        ));
    }

    static Set<PotionEffectType> BAD_EFFECTS() {
        return new HashSet<>(Arrays.asList(PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION,  PotionEffectType.HARM, PotionEffectType.HUNGER,
                PotionEffectType.POISON, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS, PotionEffectType.WITHER));
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
        EntityInsentient creature = mob.spawn(location.getWorld()).mob();
        creature.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) creature.getBukkitEntity()).setRemoveWhenFarAway(false);
        ((CraftWorld)location.getWorld()).addEntity(creature, CreatureSpawnEvent.SpawnReason.CUSTOM);
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
            case RED -> Color.RED;
            case DARK_RED -> Color.fromRGB(120,0,0);
            case YELLOW -> Color.YELLOW;
            case GOLD -> Color.fromRGB(255,223,0);
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

    static ChatColor teamColor(String string, boolean whiteToYellow) {
        if (string.toLowerCase().contains("red")) return ChatColor.RED;
        if (string.toLowerCase().contains("blue")) return ChatColor.BLUE;
        if (string.toLowerCase().contains("green")) return ChatColor.GREEN;
        if (string.toLowerCase().contains("bad")) return ChatColor.DARK_RED;
        if (string.toLowerCase().contains("good")) return ChatColor.GOLD;
        if (string.toLowerCase().contains("yellow") || string.toLowerCase().contains("white")) {
            return whiteToYellow ? ChatColor.YELLOW : ChatColor.WHITE;
        }
        return ChatColor.GRAY;
    }

    static String tacc(String message) {
        return ChatColor.translateAlternateColorCodes('&', message).replace("&r", "&7").replace("&R", "&7");
    }

    static void startGame(InventoryClickEvent event, Inventory inventory, String prefix, Runnable global, Runnable parties, Runnable party, PartyTeam... validTeams) {
        if (!inventory.getName().equalsIgnoreCase(event.getInventory().getName())) return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        player.closeInventory();

        if (!PartySystem.check(player, prefix, event.getSlot(), validTeams)) return;

        switch (event.getSlot()) {
            case START_GLOBAL -> global.run();
            case START_PARTIES_RANDOM, START_PARTIES_TEAMS -> parties.run();
            case START_PARTY_TEAM, START_PARTY_RANDOM -> party.run();
        }
    }

    enum VerType {CYCLE, NUM, JARNAME, NUM_NO_DOTS}

    static void addWorld(String worldName) {
        editWorld(worldName, true);
    }

    static void removeWorld(String worldName) {
        editWorld(worldName, false);
    }

    private static void editWorld(String worldName, boolean add) {
        List<String> list = getWorlds();
        if (add) list.add(worldName); else list.remove(worldName);
        EnderPlugin.getInstance().getConfig().set("worlds", list);
        EnderPlugin.getInstance().saveConfig();
        EnderPlugin.getInstance().reloadConfig();
    }

    static List<String> getWorlds() {
        return EnderPlugin.getInstance().getConfig().getStringList("worlds");
    }
}
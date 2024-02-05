package io.github.rookietec9.enderplugin.utils.datamanagers;

import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremi
 * @version 24.3.8
 * @since 17.7.9
 */
public class Blades {

    public static final Blades[] blades;
    public static final Blades miner, anvil, fire, gapple, ghost, health, jump, nausea, poison, power, speedy, slow, uncertain, weak, spider, fisher, tank, aqua, puncher, knocker;

    static {
        anvil = new Blades("Anvil", ChatColor.DARK_GRAY, Color.fromRGB(32, 32, 32), true, "§7Summon an anvil above the enemy [§875%§7]", null);
        fire = new Blades("Fire", ChatColor.GOLD, Color.fromRGB(255, 128, 0), true, "§7Set the enemy on fire (§600:05§7) [§650%§7]", null);
        gapple = new Blades("Gapple", ChatColor.YELLOW, Color.fromRGB(255, 255, 0), false, null, List.of(new Quad<>(PotionEffectType.ABSORPTION, 1, 3, 50), new Quad<>(PotionEffectType.REGENERATION, 1, 3, 50)));
        ghost = new Blades("Ghost", ChatColor.WHITE, Color.fromRGB(195, 255, 255), true, "§7Go Completely Invisible §7(§f00:05§7) [§f25%§7]", null);
        health = new Blades("Health", ChatColor.LIGHT_PURPLE, Color.fromRGB(255, 0, 255), false, null, List.of(new Quad<>(PotionEffectType.REGENERATION, 2, 5, 75)));
        jump = new Blades("Jump", ChatColor.GREEN, Color.fromRGB(0, 255, 0), false, null, List.of(new Quad<>(PotionEffectType.JUMP, 3, 3, 100)));
        nausea = new Blades("Nausea", ChatColor.DARK_PURPLE, Color.fromRGB(64, 0, 64), false, null, List.of(new Quad<>(PotionEffectType.CONFUSION, 10, 10, 100), new Quad<>(PotionEffectType.BLINDNESS, 10, 5, 50)));
        poison = new Blades("Poison", ChatColor.DARK_GREEN, Color.fromRGB(0, 128, 0), false, null, List.of(new Quad<>(PotionEffectType.POISON, 2, 3, 50)));
        power = new Blades("Power", ChatColor.DARK_RED, Color.fromRGB(128, 0, 0), false, null, List.of(new Quad<>(PotionEffectType.INCREASE_DAMAGE, 1, 3, 50)));
        speedy = new Blades("Speedy", ChatColor.AQUA, Color.fromRGB(0, 255, 255), false, null, List.of(new Quad<>(PotionEffectType.SPEED, 2, 3, 100)));
        slow = new Blades("Slow", ChatColor.DARK_GRAY, Color.fromBGR(128, 128, 128), false, null, List.of(new Quad<>(PotionEffectType.SLOW, 2, 5, 75)));
        uncertain = new Blades("Uncertain", ChatColor.GRAY, Color.fromRGB(192, 192, 192), true, "§7Gives a random effect §7[§f50%§7]", null);
        weak = new Blades("Weak", ChatColor.DARK_GRAY, Color.fromRGB(32, 32, 64), false, null, List.of(new Quad<>(PotionEffectType.WEAKNESS, 2, 3, 25)));
        spider = new Blades("Spider", ChatColor.WHITE, Color.fromRGB(255, 255, 255), true, "§7Trap a player in a web §7(§f00:05§7) §7[§f25%§7]", null);

        fisher = new Blades("Fisher", ChatColor.AQUA, Color.fromRGB(139, 231, 220), false, "§7Pull enemy players towards §7[§b100%§7]", null);
        fisher.setWeaponMat(Material.FISHING_ROD);
        fisher.weaponEnch = Pair.of(Enchantment.DAMAGE_ALL, 3);

        tank = new Blades("Tank", ChatColor.DARK_AQUA, Color.fromRGB(32, 64, 64), false, null, List.of(new Quad<>(PotionEffectType.DAMAGE_RESISTANCE, 1, 3, 100)));
        tank.setChestMat(Material.IRON_CHESTPLATE);

        aqua = new Blades("Aqua", ChatColor.DARK_AQUA, Color.fromRGB(32, 192, 192), true, "§7Trap a player in water §7(§300:03§7) §7[§325%§7]", null);
        aqua.bootEnch = Pair.of(Enchantment.DEPTH_STRIDER, 5);

        puncher = new Blades("Puncher", ChatColor.DARK_RED, Color.fromRGB(255, 64, 0), true, "§7Punch a player high up [§425%§7]", null);
        puncher.bootEnch = Pair.of(Enchantment.PROTECTION_FALL, 5);

        knocker = new Blades("Knocker", ChatColor.BLUE, Color.fromRGB(128, 128, 255), true, "§7Block to charge (§900:03§7)", null);

        miner = new Blades("Miner", ChatColor.LIGHT_PURPLE, Color.fromRGB(192, 128, 128), true, "§7Upgrades to §d+3.25§7 Attack Damage Upon Kill", null);
        miner.weaponEnch = Pair.of(Enchantment.DAMAGE_ALL, 1);
        miner.setWeaponMat(Material.WOOD_PICKAXE);

        blades = new Blades[]{anvil, fire, gapple, ghost, health, jump, nausea, poison, power, speedy, slow, uncertain, weak, spider, fisher, tank, aqua, puncher, knocker, miner};
    }

    private Pair<Enchantment, Integer> chestEnch;
    private Pair<Enchantment, Integer> weaponEnch;
    private Pair<Enchantment, Integer> bootEnch;
    private List<Quad<PotionEffectType, Integer, Integer, Integer>> effects;
    private String[] lore;
    private ChatColor chatColor;
    private Color armorColor;
    private String name;
    private Material weaponMat;
    private Material chestMat;
    private boolean specialFunc;

    private Blades(String name, ChatColor chatColor, Color armorColor, boolean specialFunc, String lore, List<Quad<PotionEffectType, Integer, Integer, Integer>> quads) {
        setName(name);
        setChatColor(chatColor);
        setSpecialFunc(specialFunc);
        setLore(lore);
        setArmorColor(armorColor);
        setQuads(quads);
    }

    private void setLore(String... lore) {
        this.lore = lore;
    }

    private void setArmorColor(Color color) {
        this.armorColor = color;
    }

   private void setWeaponMat(Material material) {
        this.weaponMat = material;
    }

    private void setChestMat(Material material) {
        this.chestMat = material;
    }

    private void setSpecialFunc(boolean specialFunc) {
        this.specialFunc = specialFunc;
    }

    private void setQuads(List<Quad<PotionEffectType, Integer, Integer, Integer>> typeStrengthDurationChance) {
        effects = typeStrengthDurationChance;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    private void setChatColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public String getName() {
        return getChatColor() + Java.capFirst(this.name);
    }

    private void setName(String name) {
        this.name = name;
    }

    public List<Quad<PotionEffectType, Integer, Integer, Integer>> getQuad() {return this.effects;}

    public ItemWrapper<?> getHelmet() {
        return getBasicPiece(Material.LEATHER_HELMET);
    }

    public ItemWrapper<?> getChestplate() {
        if (null == chestMat) chestMat = Material.CHAINMAIL_CHESTPLATE;
        if (null == chestEnch) chestEnch = Pair.of(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        return getBasicEnchantedPiece(chestMat, chestEnch);
    }

    public ItemWrapper<?> getLeggings() {
        return getBasicPiece(Material.LEATHER_LEGGINGS);
    }

    public ItemWrapper<?> getBoots() {
        if (null == bootEnch) return getBasicPiece(Material.LEATHER_BOOTS);
        return getBasicEnchantedPiece(Material.LEATHER_BOOTS, bootEnch);
    }

    public ItemWrapper<?> getWeapon() {
        if (null == weaponMat) weaponMat = Material.WOOD_SWORD;
        ItemWrapper<?> weapon = getBasicPiece(weaponMat);
        if (null != weaponEnch) weapon.addEnch(Pair.of(weaponEnch, 1).getKey());
        weapon.setName(getName() + " Blade");

        if (!specialFunc && null != effects) {
            for (Quad<PotionEffectType, Integer, Integer, Integer> effect : effects)
                weapon.addLore(ChatColor.GRAY + Java.capFirst(Minecraft.translateEffectName(effect.potionEffect)) + " " + chatColor + Java.convertToRoman(effect.strength) + " §7(" + Board.formatTime(effect.duration, chatColor, chatColor) + "§7) [" + chatColor + effect.chance + "%§7]");
        } else {
            List<String> strings = new ArrayList<>();
            for (String s : lore) if (s != null) strings.add(s);
            String[] arr = new String[strings.size()];
            arr = strings.toArray(arr);
            weapon.addLore(strings.toArray(arr));
        }
        weapon.addLore("", chatColor + "+" + Minecraft.getWeaponDamage(weapon.toItemStack()) + " §7Attack Damage");
        weapon.addFlag(ItemFlag.HIDE_UNBREAKABLE);
        if (!this.getName().equalsIgnoreCase("miner")) weapon.addFlag(ItemFlag.HIDE_ENCHANTS);
        return weapon;
    }

    private ItemWrapper<?> getBasicEnchantedPiece(Material material, Pair<Enchantment, Integer> ench) {
        ItemWrapper<?> itemWrapper = getBasicPiece(material);
        itemWrapper.addEnch(ench);
        return itemWrapper;
    }

    private ItemWrapper<?> getBasicPiece(Material material) {
        ItemWrapper<?> piece = new ItemWrapper<>(material, chatColor + Minecraft.getMatName(material));
        piece.setColor(armorColor);
        piece.setUnbreakable(true);
        piece.addFlag(ItemFlag.HIDE_ATTRIBUTES).addFlag(ItemFlag.HIDE_UNBREAKABLE);
        return piece;
    }

    public static class Quad<PotionEffect, Strength, Duration, Chance> {
        public final PotionEffect potionEffect;
        public final Strength strength;
        public final Duration duration;
        public final Chance chance;

        Quad(PotionEffect potionEffect, Strength strength, Duration duration, Chance chance) {
            this.potionEffect = potionEffect;
            this.strength = strength;
            this.duration = duration;
            this.chance = chance;
        }
    }
}
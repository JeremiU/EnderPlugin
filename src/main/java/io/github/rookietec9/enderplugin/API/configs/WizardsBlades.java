package io.github.rookietec9.enderplugin.API.configs;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * An enum of all the blades.
 *
 * @author TheEnderCrafter9
 * @version 16.3.6
 * @since 11.6.6
 */
public enum WizardsBlades {

    //Material.WOOD_SWORD, true, null, 0, null,

    /*1*/   ANVIL(new String[]{"§7Summon an anvil above the enemy [§875%§7]"}, ChatColor.DARK_GRAY, Color.fromRGB(32, 32, 32), "Anvil"),
    /*2*/   FIRE(new String[]{"§7Set the enemy on fire (§60:05§7) [§625%§7]"}, ChatColor.GOLD, Color.fromRGB(255, 128, 0), "Fire"),
    /*3*/   GAPPLE(new String[]{"§7Absorption §eI§7 (§e0:06§7)"}, ChatColor.YELLOW, Color.fromRGB(255, 255, 0), "Gapple"),
    /*4*/   GHOST(new String[]{"§7Go Completely Invisible §7(§f0:05§7) [§f25%§7]"}, ChatColor.WHITE, Color.fromRGB(195, 255, 255), "Ghost"),
    /*5*/   HEALTH(new String[]{"§7Regeneration §dII§7 (§d0:03§7)"}, ChatColor.LIGHT_PURPLE, Color.fromRGB(255, 0, 255), "Health"),
    /*6*/   JUMP(new String[]{"§7Jump Boost §aII§7 (§a0:03§7)"}, ChatColor.GREEN, Color.fromRGB(0, 255, 0), "Jump"),
    /*7*/   NAUSEA(new String[]{"§7Nausea §5X§7 (§50:10§7)"}, ChatColor.DARK_PURPLE, Color.fromRGB(64, 0, 64), "Nausea"),
    /*8*/   POISON(new String[]{"§7Poison §2II§7 (§20:03§7)"}, ChatColor.DARK_GREEN, Color.fromRGB(0, 128, 0), "Poison"),
    /*9*/   POWER(new String[]{"§7Strength §4I§7 (§40:03§7 [§420%§7])"}, ChatColor.DARK_RED, Color.fromRGB(128, 0, 0), "Power"),
    /*10*/  SPEEDY(new String[]{"§7Swiftness §bII§7 (§b0:03§7)"}, ChatColor.AQUA, Color.fromRGB(0, 255, 255), "Speedy"),
    /*11*/  SLOWNESS(new String[]{"§7Slowness §8I§7 (§80:05§7)"}, ChatColor.DARK_GRAY, Color.fromBGR(128, 128, 128), "Slowness"),
    /*12*/  UNCERTAIN(new String[]{"§7Gives a random effect §7[§f50%§7]"}, ChatColor.GRAY, Color.fromRGB(192, 192, 192), "Uncertain"),
    /*13*/  WEAKNESS(new String[]{"§7Weakness §8I§7 (§80:03§7) §7[§825%§7]"}, ChatColor.DARK_GRAY, Color.fromRGB(32, 32, 64), "Weakness"),
    /*14*/  WEB(new String[]{"§7Trap a player in a web §7(§f0:05§7) §7[§f25%§7]"}, ChatColor.WHITE, Color.fromRGB(255, 255, 255), "Web"),

    /*15*/   WARRIOR(new String[]{}, ChatColor.AQUA, Material.WOOD_SPADE, null, 0, new ItemStack[]{new ItemStack(Material.DIAMOND_HELMET),
            new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS)}, "Warrior"),

    /*16*/   TANK(new String[]{"§7Resistance §3II§7 (§30:03§7)"}, ChatColor.DARK_AQUA, Material.WOOD_SWORD, Enchantment.PROTECTION_ENVIRONMENTAL, 5, new ItemStack[]{
            armorPiece(Material.LEATHER_HELMET, null, 0, Color.fromRGB(32, 64, 64)),
            armorPiece(Material.IRON_CHESTPLATE, Enchantment.PROTECTION_ENVIRONMENTAL, 5, null),
            armorPiece(Material.LEATHER_LEGGINGS, null, 0, Color.fromRGB(32, 64, 64)),
            armorPiece(Material.LEATHER_BOOTS, null, 0, Color.fromRGB(32, 64, 64))}, "Tank"),

    /*17*/   AQUA(new String[]{"§7Trap a player in a water §7(§30:03§7) §7[§325%§7]"}, ChatColor.DARK_AQUA, Material.WOOD_SWORD, Enchantment.PROTECTION_ENVIRONMENTAL, 5,
            new ItemStack[]{armorPiece(Material.LEATHER_HELMET, Enchantment.WATER_WORKER, 1, Color.fromRGB(32, 192, 192)),
                    armorPiece(Material.CHAINMAIL_CHESTPLATE, Enchantment.PROTECTION_ENVIRONMENTAL, 5, null),
                    armorPiece(Material.LEATHER_LEGGINGS, null, 0, Color.fromRGB(32, 192, 192)),
                    armorPiece(Material.LEATHER_BOOTS, Enchantment.DEPTH_STRIDER, 3, Color.fromRGB(32, 192, 192)),}, "Aqua"),

    /*18*/   PUNCHER(new String[]{"§7Punch a player high up [§c25%§7]"}, ChatColor.DARK_RED, Color.fromRGB(255, 64, 0), "Puncher", Enchantment.PROTECTION_FALL, 5),
    /*19*/   KNOCKER(new String[]{"§7Punch a player back"}, ChatColor.BLUE, Color.fromRGB(128, 128, 255), "Knocker", Enchantment.KNOCKBACK, 2),

    MINER(new String[]{"§7Upgrades to §d+3.25§7 Attack Damage Upon Kill"}, ChatColor.LIGHT_PURPLE, Material.WOOD_PICKAXE, Enchantment.DAMAGE_ALL, 1,
            new ItemStack[]{
                    armorPiece(Material.LEATHER_HELMET, null, 0, Color.fromRGB(192, 128, 128)),
                    armorPiece(Material.CHAINMAIL_CHESTPLATE, Enchantment.PROTECTION_ENVIRONMENTAL, 5, null),
                    armorPiece(Material.LEATHER_LEGGINGS, null, 0, Color.fromRGB(192, 128, 128)),
                    armorPiece(Material.LEATHER_BOOTS, null, 0, Color.fromRGB(192, 128, 128))}, "Miner");

    String[] lore;
    ChatColor chatColor;
    Color color;
    Material weaponMat;
    ItemStack[] armor;
    String blade;

    Enchantment ench;
    int enchLvl;

    int mode;

    //MODE 1
    WizardsBlades(String[] lore, ChatColor chatColor, Material weaponMat, Enchantment chestEnchantment, int enchLvl, ItemStack[] armor, String blade) {
        this.lore = lore;
        this.chatColor = chatColor;
        this.weaponMat = weaponMat;
        this.ench = chestEnchantment;
        this.enchLvl = enchLvl;
        this.armor = armor;
        this.blade = blade;

        this.mode = 1;
    }

    //MODE 2
    WizardsBlades(String[] lore, ChatColor chatColor, Color color, String blade) {
        this.lore = lore;
        this.chatColor = chatColor;
        this.color = color;
        this.blade = blade;

        this.mode = 2;
    }

    //MODE 3
    WizardsBlades(String[] lore, ChatColor chatColor, Color color, String blade, Enchantment ench, int enchLvl) {
        this.ench = ench;
        this.enchLvl = enchLvl;
        this.lore = lore;
        this.chatColor = chatColor;
        this.color = color;
        this.blade = blade;

        this.mode = 3;
    }

    public static ItemStack armorPiece(Material material, Enchantment ench, int lvl, Color leatherColor) {
        ItemStack itemStack = new ItemStack(material);

        if (ench != null) itemStack.addUnsafeEnchantment(ench, lvl);

        if (leatherColor != null) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            armorMeta.setColor(leatherColor);
            itemStack.setItemMeta(armorMeta);
        }

        ItemMeta meta = itemStack.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        return itemStack;
    }

    public String[] getLore() {
        return lore;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public Color getColor() {
        return color;
    }

    public int getMode() {
        return mode;
    }

    public String getBladeName() {
        return blade;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public Enchantment getEnch() {
        return ench;
    }

    public int getEnchLvl() {
        return enchLvl;
    }

    public Material getWeaponMat() {
        return weaponMat;
    }
}
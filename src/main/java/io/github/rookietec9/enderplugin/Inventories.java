package io.github.rookietec9.enderplugin;

import io.github.rookietec9.enderplugin.configs.esg.ESGKit;
import io.github.rookietec9.enderplugin.utils.datamanagers.Blades;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.SkullMaker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionType;

import static io.github.rookietec9.enderplugin.Reference.PREFIX_CTF;

/**
 * @author Jeremi
 * @version 25.6.5
 * @since 17.6.3
 */
public class Inventories {

    public static final Inventory ESG_KIT;
    public static final Inventory BOOTY_MAP;
    public static final Inventory BOOTY_TEAM;
    public static final Inventory WIZARD_BLADE;
    public static final Inventory TELEPORTER_MAIN;
    public static final Inventory TELEPORTER_ARCHIVED;
    public static final Inventory TELEPORTER_ESG;
    public static final Inventory CTF_KIT;
    public static final Inventory MURDER_MAP;
    public static final Inventory START_MURDER, START_SPLEEF, START_ESG, START_HUNGER, START_SUMO, START_RABBIT_RUN, START_CTF, START_HNS;

    private static final String PROGRESS_5 = "§fProgress: " + "§b" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "" + Java.BLACK_BOX;
    private static final String PROGRESS_4 = "§fProgress: " + "§a" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "§8" + Java.BLACK_BOX;
    private static final String PROGRESS_3 = "§fProgress: " + "§6" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "§8" + Java.BLACK_BOX + "" + Java.BLACK_BOX;
    private static final String PROGRESS_2 = "§fProgress: " + "§c" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "§8" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "" + Java.BLACK_BOX;
    private static final String PROGRESS_1 = "§fProgress: " + "§4" + Java.BLACK_BOX + "§8" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "" + Java.BLACK_BOX + "" + Java.BLACK_BOX;

    public static final ItemStack TELLY_ITEM = new ItemWrapper<>(Material.NETHER_STAR, "§7Game Selector", "§fView our minigames.").toItemStack();

    public static final int START_PARTY_RANDOM = 2, START_PARTY_TEAM = 3, START_GLOBAL = 4, START_PARTIES_RANDOM = 5, START_PARTIES_TEAMS = 6;

    static {
        ESG_KIT = Bukkit.createInventory(null, 45, "§e§lCHOOSE A KIT");
        int i = 0;
        for (ESGKit.Kits kits : ESGKit.Kits.values()) {
            if (kits.getX() == 0) continue;
            ESGKit kit = new ESGKit(kits);
            ItemWrapper<?> itemWrapper = new ItemWrapper<>(kit.getMaterial(), kit.getColor() + Java.upSlash(kits.toString()), kit.getDataByte(), "", kit.getLoreLine());
            itemWrapper.setColor(kit.getItemColor());
            ESG_KIT.setItem(i++, itemWrapper.toItemStack());
        }
    }

    static {
        BOOTY_MAP = Bukkit.createInventory(null, 45, "§b§lCHOOSE A MAP");
        BOOTY_MAP.setItem(9, new ItemWrapper<>(Material.QUARTZ_BLOCK, "§fPLAIN", "§f· §7100% Quartz").toItemStack());
        BOOTY_MAP.setItem(11, new ItemWrapper<>(Material.STAINED_CLAY, "§7CLASSIC", (byte) 8, 1, "§f· §780% Air", "§f· §720% Gold").toItemStack());
        BOOTY_MAP.setItem(13, new ItemWrapper<>(Material.STAINED_CLAY, "§fFANCY", (byte) 0, 1, "§f· §785% Air", "§f· §711% Quartz", "§f· §73% Pink Stained Clay §7(§4Fatigue§7)", "§f· §71% Coal §7(§fHeal§7/§fTP§7)").toItemStack());
        BOOTY_MAP.setItem(15, new ItemWrapper<>(Material.GLASS, "§7GLASSY", "§f· §779.5% Air", "§f· §710% Glass", "§f· §75.5% Iron §7(§aJump Boost§7)", "§f· §73% Sponge (§4Removes All Effects§7)", "§f· §71.6% Diamond §7(§bSpeed§7)", "§f· §70.4% Mycelium §7(§8Invisibility§7)").toItemStack());

        BOOTY_MAP.setItem(17, new ItemWrapper<>(Material.STAINED_CLAY, "§dEASY", (byte) 6, 1, "§f· §750% Air", "§f· §750% Gold").toItemStack());
        BOOTY_MAP.setItem(27, new ItemWrapper<>(Material.STAINED_CLAY, "§6INSANE", (byte) 1, 1, "§f· §779.5% Air", "§f· §715% Wood §7(§4Breaks§7)", "§f· §75% Quartz", "§f· §70.5% Coal §7(§fHeal§7/§fTP§7)").toItemStack());
        BOOTY_MAP.setItem(29, new ItemWrapper<>(Material.DIAMOND_BLOCK, "§bBUFFED", "§f· §779.5% Air", "§f· §77% Iron §7(§aJump Boost§7)", "§f· §75% Diamond §7(§bSpeed§7)", "§f· §74% Quartz", "§f· §74% Dirt §7(§cStrength§7)", "§f· §70.5% Sponge (§4Removes All Effects§7)").toItemStack());
        BOOTY_MAP.setItem(31, new ItemWrapper<>(Material.STAINED_CLAY, "§eOPEN", (byte) 4, 1, "§f· §795% Air", "§f· §73% Quartz", "§f· §72% Iron §7(§aJump Boost§7)").toItemStack());
        BOOTY_MAP.setItem(33, new ItemWrapper<>(Material.WEB, "§4SPOOKY", "§f· §785% Air", "§f· §75% Iron §7(§aJump Boost§7)", "§f· §75% Web §7(§5Blindness§7)", "§f· §75% Dirt §7(§cStrength§7)").toItemStack());
        BOOTY_MAP.setItem(35, new ItemWrapper<>(Material.SLIME_BLOCK, "§aSLIMY", "§f· §779.5% Air", "§f· §710.5% Slime", "§f· §75% Iron §7(§aJump Boost§7)", "§f· §75% Diamond §7(§bSpeed§7)").toItemStack());
    }

    static {
        BOOTY_TEAM = Bukkit.createInventory(null, 27, "§3§lCHOOSE A TEAM");
        BOOTY_TEAM.setItem(10, new SkullMaker().withSkinUrl("https://textures.minecraft.net/texture/c47237437eef639441b92b217efdc8a72514a9567c6b6b81b553f4ef4ad1cae").buildWithName("§c§lRED TEAM"));
        BOOTY_TEAM.setItem(12, new SkullMaker().withSkinUrl("https://textures.minecraft.net/texture/cbdd969412df6e0acffbb7a73bfa34110eecc1f51d80ba0da25da439316bc").buildWithName("§b§lBLUE TEAM"));
        BOOTY_TEAM.setItem(14, new SkullMaker().withSkinUrl("https://textures.minecraft.net/texture/97c2d5eee84bba1d7e94f933a0a556ed7ea4e4fa65e8e9f56325813b").buildWithName("§6§lYELLOW TEAM"));
        BOOTY_TEAM.setItem(16, new SkullMaker().withSkinUrl("https://textures.minecraft.net/texture/78d58a7651fedae4c03efebc226c03fd791eb74a132babb974e8d838ac6882").buildWithName("§2§lGREEN TEAM"));
    }

    static {
        WIZARD_BLADE = Bukkit.createInventory(null, 45, "§2§lCHOOSE A BLADE");
        WIZARD_BLADE.setItem(0, new ItemWrapper<SkullMeta>(Material.SKULL_ITEM, Blades.ghost.getName() + " Blade").toItemStack());
        WIZARD_BLADE.setItem(1, new ItemWrapper<PotionMeta>(Material.POTION, Blades.speedy.getName() + " Blade").toSplashPotion(PotionType.SPEED));
        WIZARD_BLADE.setItem(2, new ItemWrapper<>(Material.GHAST_TEAR, Blades.puncher.getName() + " Blade").toItemStack());
        WIZARD_BLADE.setItem(3, new ItemWrapper<>(Material.WEB, Blades.spider.getName() + " Blade").toItemStack());

        WIZARD_BLADE.setItem(4, new ItemWrapper<>(Material.SIGN, "§fLevel [§b§lV§f]").toItemStack());
        WIZARD_BLADE.setItem(13, new ItemWrapper<>(Material.SIGN, "§fLevel [§a§lIV§f]").toItemStack());
        WIZARD_BLADE.setItem(22, new ItemWrapper<>(Material.SIGN, "§fLevel [§e§lIII§f]").toItemStack());
        WIZARD_BLADE.setItem(31, new ItemWrapper<>(Material.SIGN, "§fLevel [§6§lII§f]").toItemStack());
        WIZARD_BLADE.setItem(40, new ItemWrapper<>(Material.SIGN, "§fLevel [§c§lI§f]").toItemStack());

        WIZARD_BLADE.setItem(14, new ItemWrapper<PotionMeta>(Material.POTION, Blades.power.getName() + " Blade").toSplashPotion(PotionType.STRENGTH));
        WIZARD_BLADE.setItem(15, new ItemWrapper<>(Material.IRON_PICKAXE, Blades.miner.getName() + " Blade").addFlag(ItemFlag.HIDE_ATTRIBUTES).toItemStack());
        WIZARD_BLADE.setItem(16, new ItemWrapper<>(Material.POTION, Blades.health.getName() + " Blade").toSplashPotion(PotionType.REGEN));
        WIZARD_BLADE.setItem(17, new ItemWrapper<>(Material.POTION, Blades.poison.getName() + " Blade").toSplashPotion(PotionType.POISON));

        WIZARD_BLADE.setItem(18, new ItemWrapper<>(Material.GOLDEN_APPLE, Blades.gapple.getName() + " Blade").toItemStack());
        WIZARD_BLADE.setItem(19, new ItemWrapper<>(Material.IRON_CHESTPLATE, Blades.tank.getName() + " Blade").toItemStack());
        WIZARD_BLADE.setItem(20, new ItemWrapper<PotionMeta>(Material.POTION, Blades.slow.getName() + " Blade").toSplashPotion(PotionType.SLOWNESS));
        WIZARD_BLADE.setItem(21, new ItemWrapper<PotionMeta>(Material.POTION, Blades.weak.getName() + " Blade").toSplashPotion(PotionType.WEAKNESS));

        WIZARD_BLADE.setItem(32, new ItemWrapper<>(Material.FLINT_AND_STEEL, Blades.fire.getName() + " Blade").toItemStack());
        WIZARD_BLADE.setItem(33, new ItemWrapper<>(Material.FISHING_ROD, Blades.fisher.getName() + " Blade").toItemStack());
        WIZARD_BLADE.setItem(34, new ItemWrapper<>(Material.STICK, Blades.knocker.getName() + " Blade").toItemStack());
        WIZARD_BLADE.setItem(35, new ItemWrapper<PotionMeta>(Material.POTION, Blades.jump.getName() + " Blade").toSplashPotion(PotionType.JUMP));

        WIZARD_BLADE.setItem(36, new ItemWrapper<>(Material.ANVIL, Blades.anvil.getName() + " Blade").toItemStack());
        WIZARD_BLADE.setItem(37, new ItemWrapper<PotionMeta>(Material.POTION, Blades.nausea.getName() + " Blade").toSplashPotion(PotionType.INSTANT_DAMAGE));
        WIZARD_BLADE.setItem(38, new ItemWrapper<>(Material.RECORD_8, Blades.uncertain.getName() + " Blade").addFlag(ItemFlag.HIDE_POTION_EFFECTS).toItemStack());
        WIZARD_BLADE.setItem(39, new ItemWrapper<>(Material.WATER_BUCKET, Blades.aqua.getName() + " Blade").toItemStack());
    }

    static {
        TELEPORTER_MAIN = Bukkit.createInventory(null, 36, "§3§lGAME SELECTOR");
        TELEPORTER_MAIN.setItem(8, new ItemWrapper<>(Material.COMPASS, "§d§lHUB", "§fTeleport to the hub.", PROGRESS_5).toItemStack());
        TELEPORTER_MAIN.setItem(17, new ItemWrapper<>(Material.STAINED_GLASS_PANE, "§c-----", (byte) 14).toItemStack());
        TELEPORTER_MAIN.setItem(26, new ItemWrapper<>(Material.HOPPER, "§9§lARCADE GAMES", "§fPlay our arcade games.", PROGRESS_1).toItemStack());
        TELEPORTER_MAIN.setItem(35, new ItemWrapper<>(Material.STAINED_GLASS_PANE, "§4§lARCHIVED GAMES", (byte) 14, 1, "§fView the abandoned games.").toItemStack());

        TELEPORTER_MAIN.setItem(10, new ItemWrapper<>(Material.BLAZE_ROD, "§6§lESG", "§fOur own survival games.", PROGRESS_3).toItemStack());
        TELEPORTER_MAIN.setItem(11, new ItemWrapper<PotionMeta>(Material.POTION, "§2§lWIZARDS", (byte) 16452, 1, "§fFight using potion-tipped blades.", PROGRESS_5).toPotion(PotionType.POISON, true));
        TELEPORTER_MAIN.setItem(12, new ItemWrapper<>(Material.SEA_LANTERN, "§3§lOBSTACLE COURSE", "§fPlay our old parkour map.", PROGRESS_3).toItemStack());
        TELEPORTER_MAIN.setItem(13, new ItemWrapper<>(Material.STICK, "§b§lSUMO", "§fFight to knock your opponent outside of the arena.", PROGRESS_3).toItemStack());
        TELEPORTER_MAIN.setItem(14, new ItemWrapper<>(Material.RABBIT_FOOT, "§a§lRABBIT RUN", "§fPlay our version of TNT run.", PROGRESS_1).toItemStack());
        TELEPORTER_MAIN.setItem(15, new ItemWrapper<>(Material.IRON_HELMET, "§e§lCTF", "§fPlay capture the flag", PROGRESS_5).toItemStack());

        TELEPORTER_MAIN.setItem(19, new ItemWrapper<>(Material.SMOOTH_BRICK, "§4§lMURDERER", (byte) 1, 1, "§fCan you escape before the murderer finds you?", PROGRESS_5).toItemStack());
        TELEPORTER_MAIN.setItem(20, new ItemWrapper<>(Material.LEATHER_CHESTPLATE, "§b§lKIT PVP", "§fPlay Kit PVP", PROGRESS_1).setColor(Color.AQUA).toItemStack());
        TELEPORTER_MAIN.setItem(21, new ItemWrapper<>(Material.IRON_BLOCK, "§7§lPARKOUR", "§fPlay parkour.", PROGRESS_3).toItemStack());
        TELEPORTER_MAIN.setItem(22, new ItemWrapper<>(Material.DIAMOND_SPADE, "§9§lSPLEEF", "§fPlay spleef.", PROGRESS_5).toItemStack());
        TELEPORTER_MAIN.setItem(23, new ItemWrapper<>(Material.COOKED_BEEF, "§6§lHUNGER", "§fSee who can survive famine on a farm longer.", PROGRESS_2).toItemStack());
        TELEPORTER_MAIN.setItem(24, new ItemWrapper<>(Material.WOOD_SWORD, "§3§lBOOTY", "§fPlay a mixture of parkour and pvp.", PROGRESS_5).addFlag(ItemFlag.HIDE_ATTRIBUTES).toItemStack());
    }

    static {
        TELEPORTER_ARCHIVED = Bukkit.createInventory(null, 9, "§7§lARCHIVED GAMES");
        TELEPORTER_ARCHIVED.setItem(2, new ItemWrapper<>(Material.BOW, "§a§lSHOOTING RANGE").toItemStack());
        TELEPORTER_ARCHIVED.setItem(3, new ItemWrapper<>(Material.MINECART, "§6§lRAIL PVP").toItemStack());
        TELEPORTER_ARCHIVED.setItem(4, new ItemWrapper<>(Material.IRON_BOOTS, "§8§lJUMP").toItemStack());
        TELEPORTER_ARCHIVED.setItem(5, new ItemWrapper<>(Material.MAGMA_CREAM, "§9§lOP KIT PVP").toItemStack());
        TELEPORTER_ARCHIVED.setItem(6, new ItemWrapper<>(Material.ENDER_PEARL, "§3§lSKYWARS").toItemStack());
    }

    static {
        TELEPORTER_ESG = Bukkit.createInventory(null, 9, "§6§lESG SELECTOR");
        TELEPORTER_ESG.setItem(2, new ItemWrapper<>(Material.CHEST, "§6§lWAREHOUSE").toItemStack());
        TELEPORTER_ESG.setItem(4, new ItemWrapper<>(Material.SANDSTONE, "§e§lDESERT MAP", (byte) 2, 1).toItemStack());
        TELEPORTER_ESG.setItem(6, new ItemWrapper<>(Material.SAPLING, "§a§lJUNGLE MAP", (byte) 3, 1).toItemStack());
    }

    static {
        CTF_KIT = Bukkit.createInventory(null, 9, PREFIX_CTF + "Classes");
        CTF_KIT.setItem(2, new ItemWrapper<>(Material.STONE_SWORD, "§6§lSwordsman", "§f· §7Stone Sword", "§f· §7Leather Chestplate", "§f· §7Iron Boots").addFlag(ItemFlag.HIDE_ATTRIBUTES).toItemStack());
        CTF_KIT.setItem(4, new ItemWrapper<>(Material.BOW, "§4§lArcher", "§f· §7Wooden Sword", "§f· §7Punch 1, Infinity 1 Bow", "§f· §7Arrow", "§f· §7Leather Chestplate", "§f· §7Iron Boots").toItemStack());
        CTF_KIT.setItem(6, new ItemWrapper<>(Material.IRON_CHESTPLATE, "§2§lTank", "§f· §7Wooden Sword", "§f· §7Iron Chestplate", "§f· §7Leather Pants", "§f· §7Leather Boots").toItemStack());
    }

    static {
        MURDER_MAP = Bukkit.createInventory(null, 9, "§7§lCHOOSE A MAP");
        MURDER_MAP.setItem(2, new ItemWrapper<>(Material.SMOOTH_BRICK, "§4§lJAIL MINIMAP").toItemStack());
        MURDER_MAP.setItem(6, new ItemWrapper<>(Material.SMOOTH_BRICK, "§6§lNEIGHBORHOOD MINIMAP").toItemStack());
    }

    static {
        START_CTF = gameStart(ChatColor.YELLOW, "CTF");
        START_ESG = gameStart(ChatColor.GOLD, "ESG");
        START_HUNGER = gameStart(ChatColor.GOLD, "HUNGER");
        START_MURDER = gameStart(ChatColor.DARK_RED, "MURDERER");
        START_RABBIT_RUN = gameStart(ChatColor.GREEN, "RABBIT RUN");
        START_SUMO = gameStart(ChatColor.AQUA, "SUMO KB");
        START_SPLEEF = gameStart(ChatColor.BLUE, "SPLEEF");
        START_HNS = gameStart(ChatColor.GOLD, "HIDE N SEEK");
    }

    public static Inventory gameStart(ChatColor chatColor, String name) {
        Inventory inventory = Bukkit.createInventory(null, 9, chatColor + "" + ChatColor.BOLD + name);
        inventory.setItem(START_PARTY_RANDOM, new ItemWrapper<>(Material.MAP, "§6§lPlay Single Party (random teams)", (byte) 0, 1).toItemStack());
        inventory.setItem(START_PARTY_TEAM, new ItemWrapper<>(Material.MAP, "§6§lPlay Single Party (assigned teams)", (byte) 0, 1).toItemStack());
        inventory.setItem(START_PARTIES_RANDOM, new ItemWrapper<>(Material.MAP, "§6§lPlay With the World's Parties (random teams)", (byte) 0, 2).toItemStack());
        inventory.setItem(START_PARTIES_TEAMS, new ItemWrapper<>(Material.MAP, "§6§lPlay With the World's Parties (assigned teams)", (byte) 0, 2).toItemStack());
        inventory.setItem(START_GLOBAL, new ItemWrapper<>(Material.ENDER_PEARL, "§b§lPlay World Wide", (byte) 0, 3).toItemStack());
        return inventory;
    }
}
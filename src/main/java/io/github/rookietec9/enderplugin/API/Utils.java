package io.github.rookietec9.enderplugin.API;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jeremi
 * @version 14.3.2
 * @since 3.1.2
 */
public class Utils {

    public static Set<Material> HOLLOW_MATERIALS() {
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

    public static int getLeven(String firstString, String secondString) {
        int getLevenDistance = 0;
        char[] firstArray = firstString.toCharArray();
        char[] secondArray = secondString.toCharArray();
        if (firstArray == secondArray) {
            return 0;
        }
        if (firstArray.length == secondArray.length) {
            for (int i = 0; i < firstArray.length; i++) {
                if (firstArray[i] != secondArray[i]) {
                    getLevenDistance++;
                }
            }
        }
        if (firstArray.length != secondArray.length) {
            if (firstArray.length < secondArray.length) {
                getLevenDistance = secondArray.length - firstArray.length;
                secondArray = String.valueOf(secondArray).substring(0, firstArray.length).toCharArray();
                if (firstArray.length == secondArray.length) {
                    for (int i = 0; i < firstArray.length; i++) {
                        if (firstArray[i] != secondArray[i]) {
                            getLevenDistance++;
                        }
                    }
                }
            } else {
                getLevenDistance = firstArray.length - secondArray.length;

                firstArray = String.valueOf(firstArray).substring(0, secondArray.length).toCharArray();
                if (firstArray.length == secondArray.length) {
                    for (int i = 0; i < firstArray.length; i++) {
                        if (firstArray[i] != secondArray[i]) {
                            getLevenDistance++;
                        }
                    }
                }
            }
        }
        return getLevenDistance;
    }

    public static boolean isInRange(int compare, int min, int max) {
        return (compare <= max) && (min <= compare);
    }

    public static String capFirst(String s) {
        if (s.length() < 1) {
            return "-1";
        }
        char[] array = s.toLowerCase().toCharArray();
        StringBuilder str = new StringBuilder(String.valueOf(array[0]).toUpperCase());
        for (int i = 1; i < array.length; i++) {
            str.append(array[i]);
        }
        return str.toString();
    }

    public static String upSlash(String s) {
        String[] ret = s.replace("_", " ").split(" ");
        StringBuilder str = new StringBuilder();
        for (String s1 : ret) {
            str.append(capFirst(s1));
            str.append(" ");
        }
        return str.toString().substring(0, str.toString().length() - " ".length());
    }

    public static String convertToRoman(int num) {
        StringBuilder result = new StringBuilder();
        while (num > 0) {
            if (num >= 1000) {
                result.append("M");
                num -= 1000;
            } else if (num >= 900) {
                result.append("CM");
                num -= 900;
            } else if (num >= 500) {
                result.append("D");
                num -= 500;
            } else if (num >= 400) {
                result.append("CD");
                num -= 400;
            } else if (num >= 100) {
                result.append("C");
                num -= 100;
            } else if (num >= 90) {
                result.append("XC");
                num -= 90;
            } else if (num >= 50) {
                result.append("L");
                num -= 50;
            } else if (num >= 40) {
                result.append("XL");
                num -= 40;
            } else if (num >= 10) {
                result.append("X");
                num -= 10;
            } else if (num >= 9) {
                result.append("IX");
                num -= 9;
            } else if (num >= 5) {
                result.append("V");
                num -= 5;
            } else if (num >= 4) {
                result.append("IV");
                num -= 4;
            } else {
                result.append("I");
                num--;
            }
        }
        return result.toString();
    }

    public static int getRandom(double min, double max) {
        return (int) ((int) (Math.random() * (max + 1.0D - min)) + min);
    }

    public static class BukkitTools {
        public static int getArmorPoints(ItemStack armorItem) {
            switch (armorItem.getType()) {
                case LEATHER_BOOTS:
                case LEATHER_HELMET:
                case GOLD_BOOTS:
                case CHAINMAIL_BOOTS: return 1;

                case GOLD_HELMET:
                case LEATHER_LEGGINGS:
                case IRON_BOOTS:
                case CHAINMAIL_HELMET: return 2;

                case DIAMOND_HELMET:
                case DIAMOND_BOOTS:
                case GOLD_LEGGINGS:
                case LEATHER_CHESTPLATE: return 3;

                case CHAINMAIL_LEGGINGS: return 4;

                case CHAINMAIL_CHESTPLATE:
                case GOLD_CHESTPLATE:
                case IRON_LEGGINGS: return 5;

                case IRON_CHESTPLATE:
                case DIAMOND_LEGGINGS: return 6;

                case DIAMOND_CHESTPLATE: return 8;

                default: return 0;
            }
        }

        public static double getWeaponDamage(ItemStack itemStack) {
            double xtraDamage = (itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > 0) ? (itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL) * 1.25) : 0;

            switch (itemStack.getType()) {
                case GOLD_SPADE:
                case WOOD_SPADE: return 1 + xtraDamage;

                case STONE_SPADE:
                case GOLD_PICKAXE:
                case WOOD_PICKAXE: return 2 + xtraDamage;

                case STONE_PICKAXE:
                case WOOD_AXE:
                case GOLD_AXE:
                case IRON_SPADE: return 3 + xtraDamage;

                case DIAMOND_SPADE:
                case STONE_AXE:
                case IRON_PICKAXE:
                case WOOD_SWORD:
                case GOLD_SWORD: return 4 + xtraDamage;

                case DIAMOND_PICKAXE:
                case IRON_AXE:
                case STONE_SWORD: return 5 + xtraDamage;

                case DIAMOND_AXE:
                case IRON_SWORD: return 6 + xtraDamage;

                case DIAMOND_SWORD: return 7 + xtraDamage;

                default: return xtraDamage;
            }
        }

        public static void worldBroadcast(World world, String broadcast) {
            for (Player player : world.getPlayers()) player.sendMessage(broadcast);
        }
    }

    public static class Reference {
        public static final char OPEN_OPTIONAL_CHAR = '[';
        public static final char CLOSE_OPTIONAL_CHAR = ']';
        public static final char OPEN_MANDATORY_CHAR = '<';
        public static final char CLOSE_MANDATORY_CHAR = '>';
        public static final String USER = "username";
        public static final String MESSAGE = "message";
        public static final String MODE = "mode";
        public static String[] GREEK_VER = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Omicron", "Pi", "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega"};

        public static class Teams {
            public static final String redTeam = "team-red";
            public static final String blueTeam = "team-blue";
            public static final String whiteTeam = "team-white";
            public static final String greenTeam = "team-green";

            public static final String goodTeam = "team-positive";
            public static final String badTeam = "team-negative";
        }

        public static class Worlds {
            public static final String OLD_HUB = "hub";
            public static final String CITY = "city";
            public static final String OLD_ESG_HUB = "esg_hub";
            public static final String SWAG_RUN = "run";
            public static final String ESG_FIGHT = "SurvivalGames";
            public static final String SWAG_DUCK = "duck";
            public static final String OLD_WIZARDS = "wizards";
            public static final String MURDERER = "White_Mansion";
            public static final String OLD_OBSTACLE = "parkour";
            public static final String OLD_RAIL_PVP = "railpvp";
            public static final String BOOTY = "booty";
            public static final String OLD_ARROWS = "Rush";
            public static final String OBSTACLE = "parkour-new";
            public static final String TNT_RUN = "tntrun";
            public static final String TESTING = "testing";
            public static final String HUB = "hub2";
            public static final String PARKOUR = "newPC";
            public static final String OLD_SKYWARS = "skyvars";
            public static final String HIDENSEEK = "NeighborhoodHideNSeek";
            public static final String HUNGER = "hunger";
            public static final String CTF = "towerdefense";
            public static final String SWAG_FLY = "fly";
            public static final String KIT_PVP = "fight";
            public static final String WIZARDS = "world_wizards";
            public static final String SUMO = "SumoKB";
            public static final String ESG_HUB = "esg_hub_2";
            public static final String SPLEEF = "Spleef";
        }
    }
}
package io.github.rookietec9.enderplugin;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;

/**
 * @author Jeremi
 * @version 25.7.3
 * @since 25.2.0
 */
public class Reference {

    public static final String
            TITLE_WZRDS = "WIZARDS",
            TITLE_HUB = "HUB",
            TITLE_MMG = "MMG",
            TITLE_SUMO = "SUMOKB",
            TITLE_ESG = "ESG",
            TITLE_SPLEEF = "SPLEEF",
            TITLE_CTF = "CTF",
            TITLE_HNS = "HNS",
            TITLE_ALT_BOOTY = "PKP",
            TITLE_BOOTY = "BOOTY",
            TITLE_HUNGER = "HUNGER",
            TITLE_MURDERER = "MURDERER",
            TITLE_KIT_PVP = "KIT PVP",
            TITLE_OBSTCLE = "OBSTCLE",
            TITLE_PARKOUR = "PARKOUR",
            TITLE_RABBIT = "RABBIT";

    public static final String
            OLD_HUB = "hub",
            CITY = "city",
            OLD_ESG_HUB = "esg_hub",
            SWAG_RUN = "run",
            ESG_FIGHT = "SurvivalGames",
            SWAG_DUCK = "duck",
            OLD_WIZARDS = "wizards",
            MURDER = "White_Mansion",
            OLD_OBSTACLE = "parkour",
            OLD_RAIL_PVP = "railpvp",
            BOOTY = "booty",
            OLD_ARROWS = "Rush",
            OBSTACLE = "parkour-new",
            TNT_RUN = "world_tntrun",
            TESTING = "testing",
            HUB = "hub2",
            PARKOUR = "newPC",
            OLD_SKYWARS = "skyvars",
            HIDENSEEK = "NeighborhoodHideNSeek",
            HUNGER = "hunger",
            CTF = "towerdefense",
            SWAG_FLY = "fly",
            KIT_PVP = "fight",
            WIZARDS = "world_wizards",
            SUMO = "SumoKB",
            ESG_HUB = "esg_hub_2",
            SPLEEF = "Spleef";

    public static final char OOC = '[', COC = ']', OMC = '<', CMC = '>';

    public static final String USER = "username", MSG = "message", MODE = "mode";

    public static final String SUFFIX = ChatColor.GRAY + " > ";

    public static final String
            PREFIX_MURDER = colorFormat(TITLE_MURDERER, ChatColor.DARK_RED) + SUFFIX,
            PREFIX_HIDENNSEEK = colorFormat(TITLE_HNS, ChatColor.GOLD) + SUFFIX,
            PREFIX_MMG_LOBBY = colorFormat(TITLE_MMG, ChatColor.GRAY) + SUFFIX,
            PREFIX_CTF = colorFormat(TITLE_CTF, ChatColor.YELLOW) + SUFFIX,
            PREFIX_SPLEEF = colorFormat(TITLE_SPLEEF, ChatColor.BLUE) + SUFFIX,
            PREFIX_ESG = colorFormat(TITLE_ESG, ChatColor.GOLD) + SUFFIX,
            PREFIX_PARTY = "§d§LParty" + SUFFIX,
            PREFIX_WZRDS = colorFormat(TITLE_WZRDS, ChatColor.DARK_GREEN) + SUFFIX,
            PREFIX_HUB = colorFormat(TITLE_HUB, ChatColor.LIGHT_PURPLE) + SUFFIX,
            PREFIX_SUMO = colorFormat(TITLE_SUMO, ChatColor.AQUA) + SUFFIX,
//          PREFIX_BOOTY = colorFormat(TITLE_BOOTY, ChatColor.DARK_AQUA) + SUFFIX,
            PREFIX_ALT_BOOTY = colorFormat(TITLE_ALT_BOOTY, ChatColor.DARK_AQUA) + SUFFIX,
            PREFIX_OBS = colorFormat(TITLE_OBSTCLE, ChatColor.DARK_AQUA) + SUFFIX,
            SLOW_HEADER = "&9&lSLOW DOWN ENEMIES",
            SLOW_TEXT = "&7Slow down enemies for 15 seconds",
            BLIND_HEADER = "&8&lBLIND ENEMIES",
            BLIND_TEXT = "&7Blind enemies for 15 seconds",
            SPEED_HEADER = "&b&lSPEED",
            SPEED_TEXT = "&7Gain speed for 30 seconds",
            HEALTH_HEADER = "&e&lHEALTH",
            HEALTH_TEXT = "&7Gain extra health for 30 seconds";

    public static String PREFIX_BOOTY(Player player) {
        return colorFormat(DataPlayer.getUser(player).isOG() ? TITLE_ALT_BOOTY : TITLE_BOOTY, ChatColor.DARK_AQUA) + SUFFIX;
    }

    public static final Location
            POWERUP_SLOW = new Location(Bukkit.getWorld(WIZARDS), 39.5, 5, -78.5, 90, -1.05f),
            POWERUP_BLIND = new Location(Bukkit.getWorld(WIZARDS), 15.5, 5, -78.5, 270, -1.05f),
            POWERUP_SPEED = new Location(Bukkit.getWorld(WIZARDS), 15.5, 5, -48.5, 270, -1.05f),
            POWERUP_HEALTH = new Location(Bukkit.getWorld(WIZARDS), 39.5, 5, -48.5, 90, -1.05f);

    public static final int BUFF_COOLDOWN = 90, DEBUFF_COOLDOWN = 105;
    public static final int YAW_EAST = 5, YAW_WEST = 4, YAW_SOUTH = 3, YAW_NORTH = 2;

    public static String PREFIX_PARKOUR(int level) {
        return colorFormat("PARKOUR", PARKOUR_COLOR(level)) + SUFFIX;
    }

    public static ChatColor PARKOUR_COLOR(int level) {
        return switch (level) {
            case 2 -> ChatColor.GREEN;
            case 3 -> ChatColor.GOLD;
            case 4 -> ChatColor.BLUE;
            case 5 -> ChatColor.LIGHT_PURPLE;
            default -> ChatColor.WHITE;
        };
    }

    public static String colorFormat(String toFormat, ChatColor color) {
        toFormat = ChatColor.stripColor(toFormat);
        int firstPhase = 0, secondPhase = 0;

        if (toFormat.length() % 3 == 0) firstPhase = secondPhase = toFormat.length() / 3;

        if (toFormat.length() % 4 == 0) {
            firstPhase = toFormat.length() / 4;
            secondPhase = toFormat.length() / 2;
        }

        if (toFormat.length() % 5 == 0) {
            firstPhase = (int) (toFormat.length() / 2.5);
            secondPhase = toFormat.length() / 5;
        }

        if (toFormat.length() % 7 == 0) {
            firstPhase = (int) (toFormat.length() / 3.5);
            secondPhase = (int) (toFormat.length() / (2 + 1 / 3.0));
        }

        if (firstPhase == 0) return ChatColor.GRAY + "§l" + toFormat;

        return ChatColor.WHITE + "§l" + toFormat.substring(0, firstPhase) + color + "§l" + toFormat.substring(firstPhase, firstPhase + secondPhase) + ChatColor.WHITE + "§l" + toFormat.substring(firstPhase + secondPhase);
    }

    public static int upTimeSeconds() {
        return (int) (ManagementFactory.getRuntimeMXBean().getUptime() / 1000);
    }
}
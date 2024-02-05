package io.github.rookietec9.enderplugin.configs.esg;

import io.github.rookietec9.enderplugin.configs.Config;
import io.github.rookietec9.enderplugin.configs.associates.Associate;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

/**
 * @author Jeremi
 * @version 23.4.6
 * @since 7.1.5
 */
public class ESGKit extends Associate {

    public ESGKit(Kits kit) {
        super(new Config(false, "ESG", "kit-" + kit.configName() + ".yml", EnderPlugin.getInstance()));
    }

    public boolean isFree() {
        return getBoolean("free", false);
    }

    public ChatColor getColor() {
        return ChatColor.getByChar(getChar("color", 'f'));
    }

    public String getLoreLine() {
        return ChatColor.WHITE + getString("item.lore", "");
    }

    public Material getMaterial() {
        return Material.getMaterial(getString("item.type", "barrier").toUpperCase());
    }

    public Color getItemColor() {
        return Color.fromRGB(getInteger("item.color.r", 0), getInteger("item.color.g", 0), getInteger("item.color.b", 0));
    }

    public byte getDataByte() {
        return getByte("item.byte", (byte) 0);
    }

    public enum Kits {

        ARCHER("Archer", -60, -123),
        ARMORER("Armor", -62, -125),
        ASSASSIN("Assassin", -64, -123),
        //BERSERKER("Berserker"), //never came to be????
        CACTUS_MAN("Cactus", -68, -127),
        ENDERMAN("Enderman", -66, -123),
        FISHERMAN("Fish", -58, -127),
        FURY_SLIME("Lava", -66, -125),
        GECKO("Gecko", 60, -127),
        HORSE_TAMER("Horse", -58, -125),
        KNIGHT("Knight", -60, -125),
        NEGROMANCER("Negro", -64, -127),
        AQUA_NINJA("Ninja", -64, -125),
        RABBIT("Rabbit", -58, -123),
        //RUNAWAY("run"), //never came to be????
        SCOUT("Scout", -62, -127),
        SNOWMAN("Snow", -62, -123), //toS
        WITCH("Witch", -68, -123),
        WOLF_TAMER("Wolf", -68, -125); //toS

        final String configName;
        final int x;
        final int z;

        Kits(String configName, int chestX, int chestZ) {
            this.configName = configName;
            x = chestX;
            z = chestZ;
        }

        public static Kits from(String s) {
            if (ChatColor.stripColor(s).equalsIgnoreCase("Wolf Tamer")) return WOLF_TAMER;
            if (ChatColor.stripColor(s).equalsIgnoreCase("Fury Slime")) return FURY_SLIME;
            if (ChatColor.stripColor(s).equalsIgnoreCase("Horse Tamer")) return HORSE_TAMER;

            for (Kits kit : Kits.values()) {
                if (kit.toString().toLowerCase().contains(ChatColor.stripColor(s.toLowerCase()))) return kit;
                if (kit.configName().contains(ChatColor.stripColor(s.toLowerCase()))) return kit;
            }
            return null;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        public String configName() {
            return configName;
        }
    }
}
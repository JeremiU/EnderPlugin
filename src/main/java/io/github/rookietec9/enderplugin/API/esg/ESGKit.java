package io.github.rookietec9.enderplugin.API.esg;

import io.github.rookietec9.enderplugin.API.configs.Config;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

/**
 * The hottest provider of loops.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 7.1.5
 */
public enum ESGKit {

    ARCHER("Archer"),
    ARMORER("Armor"),
    ASSASSIN("Assassin"),
    BERSERKER("Berserker"),
    CACTUS_MAN("Cactus"),
    ENDERMAN("Enderman"),
    FISHERMAN("Fish"),
    FURY_SLIME("Lava"),
    GECKO("Gecko"),
    HORSE_TAMER("Horse"),
    KNIGHT("Knight"),
    NEGROMANCER("Negro"),
    AQUA_NINJA("Ninja"),
    RABBIT("Rabbit"),
    RUNAWAY("Run"),
    SCOUT("Scout"),
    SNOWMAN("Snow"),
    WITCH("Witch"),
    WOLF_TAMER("Wolf"),
    SHELL("config");

    final String configName;
    final Config config;

    ESGKit(String configName) {
        this.configName = configName;
        config = new Config(false, "ESG", "kit-" + configName() + ".yml", EnderPlugin.getInstance());
    }

    public int toUpgrade(int to) {
        for (int j = 2; j <= 10; j++) {
            if (to == j)
                return new Config(false,"ESG","kit-"+ configName() +".yml", EnderPlugin.getInstance()).getYaml().getInt("upgrade" + j);
        }
        return 0;
    }

    /**
     * Gets a kit from a String.
     *
     * @param s String to get the kit from
     * @return ESGKit or null if not found
     */
    public static ESGKit from(String s) {
        if (ChatColor.stripColor(s).equalsIgnoreCase("Wolf Tamer")) {
            return WOLF_TAMER;
        }
        if (ChatColor.stripColor(s).equalsIgnoreCase("Fury Slime")) {
            return FURY_SLIME;
        }
        if (ChatColor.stripColor(s).equalsIgnoreCase("Horse Tamer")) {
            return HORSE_TAMER;
        }

        for (ESGKit esgKit : ESGKit.values()) {
            if (esgKit.toString().toLowerCase().contains(ChatColor.stripColor(s.toLowerCase()))) return esgKit;
        }
        return null;
    }

    public String configName() {
        return configName;
    }

    public boolean getFree() {
        return config.getYaml().getBoolean("free");
    }

    public ChatColor getColor() {
        return ChatColor.getByChar(config.getYaml().getString("color"));
    }

    public int getPrice() {
        return config.getYaml().getInt("price");
    }

    public String getLoreLine() {
        return config.getYaml().getString("item.lore");
    }

    public Material getType() {
        return Material.getMaterial(config.getYaml().getString("item.type").toUpperCase());
    }

    public boolean hideFlags() {
        return config.getYaml().getBoolean("item.hide");
    }

    public boolean getGlow() {
        return config.getYaml().getBoolean("item.glow");
    }

    public Color getItemColor() {
        return Color.fromRGB(config.getYaml().getInt("item.color.r"), config.getYaml().getInt("item.color.g"), config.getYaml().getInt("item.color.b"));
    }

    public byte getDataByte() {
        return (byte) config.getYaml().getInt("item.byte");
    }
}
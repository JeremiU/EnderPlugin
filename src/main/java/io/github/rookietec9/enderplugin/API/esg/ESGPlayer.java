package io.github.rookietec9.enderplugin.API.esg;

import io.github.rookietec9.enderplugin.API.configs.Config;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.entity.Player;

/**
 * @author Jeremi
 * @version 13.4.4
 * @since 7.0.2
 */
public class ESGPlayer {

    private final Player p;
    private final Config config;

    public ESGPlayer(Player p) {
        this.p = p;
        config = new Config(false, "ESG", p.getUniqueId().toString() + ".yml", EnderPlugin.getInstance());
        for (ESGKit e : ESGKit.values()) {
            if (e == ESGKit.SHELL) continue;
            if (config.getYaml().get("kit." + e.configName()) == null) {
                config.getYaml().createSection("kit." + e.configName());
                if (e.getFree()) {
                    config.getYaml().set("kit." + e.configName(), 1);
                } else {
                    config.getYaml().set("kit." + e.configName(), 0);
                }
            }
            config.modifyYaml();
        }
    }

    public int getCoins() {
        check("coins");
        return config.getYaml().getInt("coins");
    }

    public void setCoins(int i) {
        set("coins", i);
    }

    public int getDeaths() {
        check("deaths");
        return config.getYaml().getInt("deaths");
    }

    public void setDeaths(int i) {
        set("deaths", i);
    }

    public int getKills() {
        check("kills");
        return config.getYaml().getInt("kills");
    }

    public void setKills(int i) {
        set("kills", i);
    }

    public int getKitLevel(ESGKit kit) {
        check(kit);
        config.modifyYaml();
        return config.getYaml().getInt("kit." + kit.configName());
    }

    public boolean getKitUnlocked(ESGKit kit) {
        check(kit);
        config.modifyYaml();
        return getKitLevel(kit) != 0;
    }

    public int getKitCount() {
        int cnt = 0;
        for (ESGKit e : ESGKit.values()) {
            if (getKitUnlocked(e)) {
                cnt++;
            }
        }
        return cnt;
    }

    public int getHighestLVL() {
        int high = 1;
        config.modifyYaml();
        for (ESGKit e : ESGKit.values()) {
            if (getKitLevel(e) > high) {
                high = getKitLevel(e);
            }
        }
        return high;
    }

    public int getLowestLVL() {
        int low = 10;
        config.modifyYaml();
        for (ESGKit e : ESGKit.values()) {
            if (getKitLevel(e) < low) {
                low = getKitLevel(e);
            }
        }
        return low;
    }

    private void check(ESGKit kit) {
        if (config.getYaml().get("kit." + kit.configName) == null) {
            if (kit.getFree()) {
                config.getYaml().createSection("kit." + kit.configName);
                config.getYaml().set("kit." + kit.configName(), 1);
            } else {
                config.getYaml().createSection("kit." + kit.configName);
                config.getYaml().set("kit." + kit.configName(), 0);
            }
        }
    }

    private void check(String check) {
        if (config.getYaml().get(check) == null) {
            config.getYaml().createSection(check);
            config.getYaml().set(check, 0);
            config.modifyYaml();
        }
    }

    public Player getBase() {
        return p;
    }

    public void setESGKitLeve(int i, ESGKit esgKit) {
        set("kit." + esgKit.configName(), i);
    }

    private void set(String path, Object o) {
        config.modifyYaml();
        config.getYaml().set(path, o);
        config.modifyYaml();
    }
}
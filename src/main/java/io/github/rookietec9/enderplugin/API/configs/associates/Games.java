package io.github.rookietec9.enderplugin.API.configs.associates;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Config;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @version 16.2.1
 */
public class Games {
    private final Config config;

    public Games() {
        this.config = new Config(true, "", "games.yml", EnderPlugin.getInstance());
        config.modifyYaml();
    }

    public Config getConfig() {
        return config;
    }

    public String getUniversalIP() {
        return (String) get(getPath("bukkitIP", "104.162.164.13"));
    }

    private void set(String path, Object o) {

        config.modifyYaml();
        config.getYaml().set(path, o);
        config.modifyYaml();
    }

    private Object get(Object object) {
        config.modifyYaml();
        return object;
    }

    private Object getPath(String path, Object defaultObj) {
        config.modifyYaml();
        if (config.getYaml().get(path) == null) {
            set(path, defaultObj);
        }
        return config.getYaml().get(path);
    }

    public class BootyInfo {
        public String type() {
            return (String) getPath("booty.type", "plain");
        }

        public int deaths(Player player) {
            return (int) get(getPath("booty.deaths." + player.getUniqueId().toString(), 0));
        }

        public int kills(Player player) {
            return (int) get(getPath("booty.kills." + player.getUniqueId().toString(), 0));
        }

        public void setDeaths(Player player, int deaths) {
            set("booty.deaths." + player.getUniqueId().toString(), deaths);
        }

        public void setKills(Player player, int kills) {
            set("booty.kills." + player.getUniqueId().toString(), kills);
        }

        public void setType(String type) {
            set("booty.type", type);
        }
    }

    public class WizardsInfo {
        public int deaths(Player player) {
            return (int) get(getPath("wizard.deaths." + player.getUniqueId().toString(), 0));
        }

        public int kills(Player player) {
            return (int) get(getPath("wizard.kills." + player.getUniqueId().toString(), 0));
        }

        public void setDeaths(OfflinePlayer player, int deaths) {
            set("wizard.deaths." + player.getUniqueId().toString(), deaths);
        }

        public void setKills(OfflinePlayer player, int kills) {
            set("wizard.kills." + player.getUniqueId().toString(), kills);
        }
    }

    public class SpleefInfo {
        public int wins(Player player) {
            return (int) get(getPath("spleef.wins." + player.getUniqueId().toString(), 0));
        }

        public int blocksBroken(Player player) {
            return (int) get(getPath("spleef.blocks." + player.getUniqueId().toString(), 0));
        }

        public int losses(Player player) {
            return (int) get(getPath("spleef.losses." + player.getUniqueId().toString(), 0));
        }

        public void setWins(Player player, int wins) {
            set("spleef.wins." + player.getUniqueId().toString(), wins);
        }

        public void setBlocks(Player player, int blocks) {
            set("spleef.blocks." + player.getUniqueId().toString(), blocks);
        }

        public void setLosses(Player player, int losses) {
            set("spleef.losses." + player.getUniqueId().toString(), losses);
        }
    }

    public class ParkourInfo {
        public int getBlock(int level, Location location) {
            boolean b = true;

            for (int i = -3; b; i++) {
                if (config.getYaml().get("parkour.blockLoc." + level + "." + i + ".x") != null) {
                    if (location.getBlockX() == (int) config.getYaml().get("parkour.blockLoc." + level + "." + i + ".x"))
                        if (location.getBlockY() == (int) config.getYaml().get("parkour.blockLoc." + level + "." + i + ".y"))
                            if (location.getBlockZ() == (int) config.getYaml().get("parkour.blockLoc." + level + "." + i + ".z")) {
                                return i;
                            }
                } else if (i > 1) b = false;
            }
            return 0;
        }

        public Location blockLoc(int level, int block) {
            return new Location(Bukkit.getWorld(Utils.Reference.Worlds.PARKOUR),
                    (int) get(config.getYaml().get("parkour.blockLoc." + level + "." + block + ".x")),
                    (int) get(config.getYaml().get("parkour.blockLoc." + level + "." + block + ".y")),
                    (int) get(config.getYaml().get("parkour.blockLoc." + level + "." + block + ".z"))
            );
        }

        public Location returnLoc(int level) {
            return new Location(Bukkit.getWorld(Utils.Reference.Worlds.PARKOUR),
                    (int) get(getPath("parkour.returnLoc." + level + ".x", 0)),
                    (int) get(getPath("parkour.returnLoc." + level + ".y", 0)),
                    (int) get(getPath("parkour.returnLoc." + level + ".z", 0)),
                    Float.valueOf(String.valueOf(get(getPath("parkour.returnLoc." + level + ".yaw", 0)))),
                    Float.valueOf(String.valueOf(get(getPath("parkour.returnLoc." + level + ".pitch", 0))))
            );
        }
    }
}
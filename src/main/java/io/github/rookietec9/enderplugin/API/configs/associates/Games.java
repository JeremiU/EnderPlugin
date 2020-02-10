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
public class Games extends Associate {

    public Games() {
        super(new Config(true, "", "games.yml", EnderPlugin.getInstance()));
    }

    public String getUniversalIP() {
        return (String) getPath("bukkitIP", "104.162.164.13");
    }

    public class BootyInfo {
        public String type() {
            return (String) getPath("booty.type", "plain");
        }

        public int deaths(Player player) {
            return (int) getPath("booty.deaths." + player.getUniqueId().toString(), 0);
        }

        public int kills(Player player) {
            return (int) getPath("booty.kills." + player.getUniqueId().toString(), 0);
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
            return (int) getPath("wizard.deaths." + player.getUniqueId().toString(), 0);
        }

        public int kills(Player player) {
            return (int) getPath("wizard.kills." + player.getUniqueId().toString(), 0);
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
            return (int) getPath("spleef.wins." + player.getUniqueId().toString(), 0);
        }

        public int blocksBroken(Player player) {
            return (int) getPath("spleef.blocks." + player.getUniqueId().toString(), 0);
        }

        public int losses(Player player) {
            return (int) getPath("spleef.losses." + player.getUniqueId().toString(), 0);
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

        public Location blockLoc(int level, int block) {
            return new Location(Bukkit.getWorld(Utils.Reference.Worlds.PARKOUR),
                    (int) getPath("parkour.blockLoc." + level + "." + block + ".x", 0),
                    (int) getPath("parkour.blockLoc." + level + "." + block + ".y", 0),
                    (int) getPath("parkour.blockLoc." + level + "." + block + ".z", 0)
            );
        }

        public Location returnLoc(int level) {
            return new Location(Bukkit.getWorld(Utils.Reference.Worlds.PARKOUR),
                    (int) getPath("parkour.returnLoc." + level + ".x", 0),
                    (int) getPath("parkour.returnLoc." + level + ".y", 0),
                    (int) getPath("parkour.returnLoc." + level + ".z", 0),
                    Float.valueOf(String.valueOf(getPath("parkour.returnLoc." + level + ".yaw", 0))),
                    Float.valueOf(String.valueOf(getPath("parkour.returnLoc." + level + ".pitch", 0)))
            );
        }
    }
}
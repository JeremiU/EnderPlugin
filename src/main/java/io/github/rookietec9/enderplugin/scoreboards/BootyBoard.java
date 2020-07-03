package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.DataType;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Jeremi
 * @version 22.2.9
 * @since 13.1.7
 */
public class BootyBoard extends Board {

    public BootyBoard(Player player) {
        super(player, Worlds.BOOTY, BoardNames.BOOTY, ChatColor.DARK_AQUA);
        putBreaks(14, 11, 7);
        putHeader("Map: ", true, 13);
        putHeader(Java.capFirst(DataPlayer.bootyType()), false, 12);
        putHeader("THIS ROUND: ", false, 10);
        putHeader("OVERALL: ", false, 6);

        putData("Kills§b§b", DataPlayer.get(player).tempBootyKills + "", 9);
        putData("Kills§a§b", DataPlayer.get(player).getInt(DataType.BOOTYKILLS) + "", 5);
        putData("Deaths§b§b", DataPlayer.get(player).tempBootyDeaths + "", 8);
        putData("Deaths§a§b", DataPlayer.get(player).getInt(DataType.BOOTYDEATHS) + "", 4);
    }

    public void updateTempKills(int kills) {
        DataPlayer.get(player).tempBootyKills = kills;
        updateData("Kills§b§b", kills + "");
    }

    public void updateTempDeaths(int deaths) {
        DataPlayer.get(player).tempBootyDeaths = deaths;
        updateData("Deaths§b§b", deaths + "");
    }

    public void reloadKillsAndDeaths() {
        updateData("Kills§a§b", DataPlayer.get(player).getInt(DataType.BOOTYKILLS) + "");
        updateData("Deaths§a§b", DataPlayer.get(player).getInt(DataType.BOOTYDEATHS) + "");
    }

    public void updateMap(String map) {
        DataPlayer.setBootyType(map);
        updateHeader(DataPlayer.bootyType(), false, "fancy", "insane", "buffed", "open", "slimy", "spooky", "glassy", "classic", "easy", "plain");
    }
}
package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.configs.associates.User;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.configs.DataType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 13.1.7
 */
public class BootyBoard extends Board {

    public BootyBoard(Player player) {
        super(player, BOOTY, colorFormat(DataPlayer.getUser(player).isOG() ? TITLE_BOOTY : TITLE_ALT_BOOTY, ChatColor.DARK_AQUA), ChatColor.DARK_AQUA);
        putBreaks(11, 9, 7);

        putData("Map", Java.capFirst(DataPlayer.bootyType()), 10);

        putData("Kill Streak", DataPlayer.get(player).tempBootyStreak + "", 8);

        putData("Kills", DataPlayer.get(player).getInt(DataType.BOOTYKILLS) + "", 6);
        putData("Deaths", DataPlayer.get(player).getInt(DataType.BOOTYDEATHS) + "", 5);
        putData("K/D", String.format("%.2f", (DataPlayer.get(player).getInt(DataType.BOOTYDEATHS) > 0 ? DataPlayer.get(player).getInt(DataType.BOOTYKILLS) / (double) DataPlayer.get(player).getInt(DataType.BOOTYDEATHS) : DataPlayer.get(player).getInt(DataType.BOOTYKILLS))) + "", 4);
    }

    public void reloadKillsAndDeaths() {
        updateData("Kills", DataPlayer.get(player).getInt(DataType.BOOTYKILLS) + "");
        updateData("Deaths", DataPlayer.get(player).getInt(DataType.BOOTYDEATHS) + "");
        updateData("Kill Streak", DataPlayer.get(player).tempBootyStreak + "");
        updateData("K/D",String.format("%.2f", (DataPlayer.get(player).getInt(DataType.BOOTYDEATHS) > 0 ? DataPlayer.get(player).getInt(DataType.BOOTYKILLS) / (double) DataPlayer.get(player).getInt(DataType.BOOTYDEATHS) : DataPlayer.get(player).getInt(DataType.BOOTYKILLS))) + "");
    }

    public void updateMap(String map) {
        DataPlayer.setBootyType(map);
        updateData("Map", DataPlayer.bootyType());
    }
}
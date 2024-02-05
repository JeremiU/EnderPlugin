package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.PARKOUR;
import static io.github.rookietec9.enderplugin.Reference.PARKOUR_COLOR;

/**
 * @author Jeremi
 * @version 25.2.6
 * @since 13.4.4
 */
public class ParkourBoard extends Board {

    public ParkourBoard(Player player) {
        super(player, PARKOUR, PARKOUR_COLOR(DataPlayer.get(player).tempParkourLevel) + "§lPARKOUR", ChatColor.WHITE);
        putBreaks(5);
        putData("Level", DataPlayer.get(player).tempParkourLevel + "", 4);
    }

    public void updateLevel() {
        updateData("Level", DataPlayer.get(player).tempParkourLevel + "");
        changeTitle(PARKOUR_COLOR(DataPlayer.get(player).tempParkourLevel) + "§lPARKOUR");
    }
}
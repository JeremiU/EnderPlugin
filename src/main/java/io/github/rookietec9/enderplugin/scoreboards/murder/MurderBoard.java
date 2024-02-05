package io.github.rookietec9.enderplugin.scoreboards.murder;

import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.*;


/***
 * @author Jeremi
 * @version 25.2.0
 * @since ?.?.?
 */
public class MurderBoard extends Board {

    public MurderBoard(Player player) {
        super(player, null, colorFormat(TITLE_MURDERER, ChatColor.DARK_RED), ChatColor.DARK_RED);
        putBreaks(9, 6);
        putData("Phase", DataPlayer.murderMode, 8);
        putData("Time", formatTime(DataPlayer.prisonHidingSec), 7);
        putData("Role", "None", 5);
        putData("Innocents Left", 0 + "", 4);
    }

    public void updateTicks(int ticks) {
        updateData("Time", formatTime(ticks));
    }

    public void updatePeople(int people) {
        updateData("Innocents Left", people + "");
    }

    public void updateRole(boolean isInnocent) {
        updateData("Role", isInnocent ? "Innocent" : "Murderer");
    }

    public void updateMode(boolean isWaiting, boolean gameOn) {
        if (!gameOn) {
            DataPlayer.murderMode = "Finished";
            updateData("Role", "None");
        } else DataPlayer.murderMode = isWaiting ? "Waiting" : "Escaping";
        updateData("Phase", DataPlayer.murderMode);
    }
}
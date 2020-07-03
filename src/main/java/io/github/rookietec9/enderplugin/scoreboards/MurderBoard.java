package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


/***
 * @author Jeremi
 * @version 21.3.3
 * @since ?.?.?
 */
public class MurderBoard extends Board {

    public MurderBoard(Player player) {
        super(player, Worlds.MURDER, BoardNames.MURDER, ChatColor.DARK_RED);
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
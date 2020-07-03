package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Jeremi
 * @version 19.0.8
 * @since 13.4.4
 */
public class HideBoard extends Board {

    public HideBoard(Player player) {
        super(player, Worlds.HIDENSEEK, BoardNames.HIDE, ChatColor.GOLD);
        putBreaks(8, 6);
        putData("Seeker","none", 7);
        putData("Phase", "FINISHED", 6);
        putData("Time", formatTime(DataPlayer.hoodHidingSec >= 0 ? DataPlayer.hoodHidingSec : DataPlayer.hoodFindingSec), 5);
        putData("People Left", 0 + "", 4);
    }

    public void updateTicks(int ticks) {
        updateData("Time", Board.formatTime(ticks, ChatColor.GOLD, ChatColor.WHITE));
    }

    public void updatePeople(int people) {
        updateData("People Left", people + "");
    }

    public void updateSeeker(String seekerName) {
        updateData("Seeker", seekerName);
    }

    public void updateMode(boolean isHiding, boolean gameOn) {
        String mode;
        if (!gameOn) mode = "FINISHED";
        else mode = isHiding ? "HIDING" : "SEEKING";
        updateData("Phase", mode);
    }
}
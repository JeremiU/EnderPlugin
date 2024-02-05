package io.github.rookietec9.enderplugin.scoreboards.murder;

import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.*;


/**
 * @author Jeremi
 * @version 25.2.0
 * @since 13.4.4
 */
public class HideBoard extends Board {

    boolean gameOn;

    public HideBoard(Player player) {
        super(player, null, colorFormat(TITLE_HNS, ChatColor.GOLD), ChatColor.GOLD);
        putBreaks(8, 6);
        putData("Seeker", "none", 7);
        putData("Phase", "FINISHED", 6);
        putData("Time", formatTime(DataPlayer.hoodHidingSec >= 0 ? DataPlayer.hoodHidingSec : DataPlayer.hoodFindingSec), 5);
        putData("People Left", 0 + "", 4);
    }

    public void updateTicks() {
        updateData("Time", gameOn ? formatTime(DataPlayer.hoodHidingSec >= 0 ? DataPlayer.hoodHidingSec : DataPlayer.hoodFindingSec) : formatTime(0));
    }

    public void updatePeople(int people) {
        updateData("People Left", people + "");
    }

    public void updateSeeker(String seekerName) {
        updateData("Seeker", seekerName);
    }

    public void updateMode(boolean isHiding, boolean gameOn) {
        this.gameOn = gameOn;
        String mode;
        if (!gameOn) mode = "FINISHED";
        else mode = isHiding ? "HIDING" : "SEEKING";
        updateData("Phase", mode);
    }
}
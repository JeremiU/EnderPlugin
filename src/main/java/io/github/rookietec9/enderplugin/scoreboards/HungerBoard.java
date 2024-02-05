package io.github.rookietec9.enderplugin.scoreboards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 19.3.7
 */
public class HungerBoard extends Board {

    public HungerBoard(Player player) {
        super(player, HUNGER, colorFormat(TITLE_HUNGER, ChatColor.GOLD), ChatColor.GOLD);
        putBreaks(9, 6);
        putData("Phase", "DONE", 8);
        putData("Time", formatTime(0), 7);

        putData("Spots Tilled", 0 + "", 5);
        putData("Coins", 0 + "", 4);
    }

    public void updateTicks(int ticks) {
        updateData("Time", Board.formatTime(ticks, ChatColor.GOLD, ChatColor.WHITE));
    }

    public void updateMode(int phase, boolean gameOn) {
        if (!gameOn) {
            updateData("Phase", "DONE");
        } else {
            updateData("Phase", phase + "");
        }
    }

    public void updateSpots(int spots) {
        updateData("Spots Tilled", spots + "");
    }

    public void updateCoins(int coins) {
        updateData("Coins", coins + "");
    }
}
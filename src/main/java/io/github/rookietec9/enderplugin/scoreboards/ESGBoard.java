package io.github.rookietec9.enderplugin.scoreboards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @since 25.2.0
 * @version 19.3.6
 */
public class ESGBoard extends Board {

    public ESGBoard(Player player) {
        super(player, ESG_FIGHT, colorFormat(TITLE_ESG, ChatColor.GOLD), ChatColor.GOLD);
        putBreaks(12, 7);
        putData("Phase", "DONE", 11);
        putData("Time Left", formatTime(0), 10);
        putData("Players Left", 0 + "", 9);
        putData("Unlooted Chests", 0 + "", 8);

        putData("Chests Looted", 0 + "", 6);
        putData("Kit", "NONE", 5);
        putData("Kills", 0 + "", 4);
    }

    public void updateKills(int kills) {
        updateData("Kills", kills + "");
    }

    public void updateKit(String kit) {
        updateData("Kit", kit.toUpperCase());
    }

    public void updateChestsLooted(int looted) {
        updateData("Chests Looted", looted + "");
    }

    public void updateChestsNotLooted(int unlooted) {
        updateData("Unlooted Chests", unlooted + "");
    }

    public void updatePeople(int left) {
        updateData("Players left", left + "");
    }

    public void updateTicks(int ticks) {
        updateData("Time left", formatTime(ticks));
    }

    public void updateMode(boolean isDeathMatch, boolean gameOn) {
        if (!gameOn) updateData("Phase","DONE");
        else updateData("Phase", isDeathMatch ? "DEATHMATCH" : "WARMUP");
    }
}
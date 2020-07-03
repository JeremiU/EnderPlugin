package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Jeremi
 * @since 19.3.7
 * @version 19.3.7
 */
public class SumoBoard extends Board {

    public SumoBoard(Player player) {
        super(player, Worlds.SUMO, BoardNames.SUMO, ChatColor.AQUA);
        putBreaks(14, 11, 8);
        putData("Time Elapsed", formatTime(0), 13);
        putData("Players Left", 0 + "", 12);
        putHeader("THIS ROUND:", false, 10);
        putData("Players Killed§a§b", roundInt(0), 9);
        putHeader("OVERALL:", false, 7);
        putData("Players Killed§b§b", roundInt(0), 6);
        putData("Rounds Won§a§b", roundInt(0), 5);
        putData("Rounds Lost§a§b", roundInt(0), 4);
    }

    public void updateTicks() {
        updateData("Time Elapsed", formatTime(0) + "");
    }

    public void updatePlayers(int players) {
        updateData("Players Left", players + "");
    }

    public void updateTempKills(int kills) {
        updateData("Players Killed§a§b", kills + "");
    }

    public void reloadStats() {
        updateData("Players Killed§b§b", roundInt(0));
        updateData("Rounds Won§a§b", roundInt(0));
        updateData("Rounds Lost§a§b", roundInt(0));
    }
}
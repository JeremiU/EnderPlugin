package io.github.rookietec9.enderplugin.scoreboards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @since 25.2.0
 * @version 19.3.7
 */
public class ObstacleCourseBoard extends Board {

    public ObstacleCourseBoard(Player player) {
        super(player, OBSTACLE, colorFormat(TITLE_OBSTCLE, ChatColor.DARK_AQUA), ChatColor.DARK_AQUA);
        putBreaks(8, 5);
        putData("Level", "N/A", 7);
        putData("Block", "N/A", 6);
        putData("Attempts", "N/A", 4);
    }

    public void updateLevel(String level) {
        updateData("Level", level);
    }

    public void updateBlock(String block) {
        updateData("Block", block);
    }

}
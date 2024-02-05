package io.github.rookietec9.enderplugin.scoreboards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @since 25.2.0
 * @version 19.3.7
 */
public class RabbitBoard extends Board {

    public RabbitBoard(Player player) {
        super(player, TNT_RUN, colorFormat(TITLE_RABBIT, ChatColor.GREEN), ChatColor.GREEN);
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
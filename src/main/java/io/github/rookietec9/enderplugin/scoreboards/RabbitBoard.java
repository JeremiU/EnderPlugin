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
public class RabbitBoard extends Board {

    public RabbitBoard(Player player) {
        super(player, Worlds.TNT_RUN, BoardNames.RABBIT, ChatColor.GREEN);
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
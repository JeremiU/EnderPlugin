package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Jeremi
 * @version 18.5.8
 * @since 13.4.4
 */
public class ParkourBoard extends Board {

    public ParkourBoard(Player player) {
        super(player, Worlds.PARKOUR, BoardNames.PARKOUR, ChatColor.DARK_GRAY);
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
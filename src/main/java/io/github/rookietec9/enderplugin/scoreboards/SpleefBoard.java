package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.DataType;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * @author Jeremi
 * @version 21.3.4
 * @since 14.8.3
 */
public class SpleefBoard extends Board {

    public SpleefBoard(Player player) {
        super(player, Worlds.SPLEEF, BoardNames.SPLEEF, ChatColor.BLUE, GameMode.SURVIVAL);
        putBreaks(14, 11, 8);
        putData("Time Elapsed", formatTime(DataPlayer.spleefSec), 13);
        putData("Players Left", DataPlayer.spleefLeft.size() + "", 12);
        putHeader("THIS ROUND:", false, 10);
        putData("Blocks Broken§b§b", roundInt(DataPlayer.get(player).tempSpleefBlocks), 9);
        putHeader("OVERALL:", false, 7);
        putData("Blocks Broken§a§b", roundInt(DataPlayer.get(player).getInt(DataType.SPLEEFBLOCKS)), 6);
        putData("Rounds Won§a§b", DataPlayer.get(player).getInt(DataType.SPLEEFWINS) + "", 5);
        putData("Rounds Lost§a§b", DataPlayer.get(player).getInt(DataType.SPLEEFLOSSES) + "", 4);
    }

    public void updateTicks() {
        updateData("Time Elapsed", formatTime(DataPlayer.spleefSec) + "");
    }

    public void updatePlayers(int players) {
        updateData("Players Left", players + "");
    }

    public void updateTempBlocks(int blocks) {
        DataPlayer.get(player).tempSpleefBlocks = blocks;
        updateData("Blocks Broken§b§b", blocks + "");
    }

    public void reloadStats() {
        updateData("Blocks Broken§a§b", roundInt(DataPlayer.get(player).getInt(DataType.SPLEEFBLOCKS)));
        updateData("Rounds Won§a§b", DataPlayer.get(player).getInt(DataType.SPLEEFWINS) + "");
        updateData("Rounds Lost§a§b", DataPlayer.get(player).getInt(DataType.SPLEEFLOSSES) + "");
    }
}
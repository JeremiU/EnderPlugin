package io.github.rookietec9.enderplugin.scoreboards.murder;

import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.configs.DataType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 23.1.4
 */
public class MMGLobbyBoard extends Board {

    public MMGLobbyBoard(Player player) {
        super(player, MURDER, colorFormat(TITLE_MMG, ChatColor.GRAY), ChatColor.GRAY);
        putBreaks(11, 7);
        putHeader("JAIL MINIMAP", true, 10);
        putData("§fWins", DataPlayer.get(player).getInt(DataType.MURDERWINS) + "", 9);
        putData("§fLosses", DataPlayer.get(player).getInt(DataType.MURDERLOSSES) + "", 8);
        putHeader("NEIGHBORHOOD MINIMAP", true, 6);
        putData("§rWins", DataPlayer.get(player).getInt(DataType.HOODWINS) + "", 5);
        putData("§rLosses", DataPlayer.get(player).getInt(DataType.HOODLOSSES) + "", 4);
    }

    public void updateScores() {
        updateData("§fWins", DataPlayer.get(player).getInt(DataType.MURDERWINS) + "");
        updateData("§fLosses", DataPlayer.get(player).getInt(DataType.MURDERLOSSES) + "");
        updateData("§rWins", DataPlayer.get(player).getInt(DataType.HOODWINS) + "");
        updateData("§rLosses", DataPlayer.get(player).getInt(DataType.HOODLOSSES) + "");
    }
}
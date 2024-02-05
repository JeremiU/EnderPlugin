package io.github.rookietec9.enderplugin.scoreboards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Jeremi
 * @version 19.3.7
 * @since 13.1.7
 */
public class ArchivedBoard extends Board {

    protected ArchivedBoard(Player player, String worldName, String name, String desc) {
        super(player, worldName, name, ChatColor.WHITE);
        putHeader(desc + "!", true, 6);
        putBreaks(5);
        putData("NOTE", "These are abandoned", 4);
    }

}
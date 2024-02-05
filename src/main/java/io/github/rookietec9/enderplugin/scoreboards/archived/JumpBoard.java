package io.github.rookietec9.enderplugin.scoreboards.archived;

import io.github.rookietec9.enderplugin.scoreboards.ArchivedBoard;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.OLD_OBSTACLE;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 17.5.1
 */
public class JumpBoard extends ArchivedBoard {

    public JumpBoard(Player player) {
        super(player, OLD_OBSTACLE, "§7§l§mJUMP", "Parkour");
    }
}
package io.github.rookietec9.enderplugin.scoreboards.archived;

import io.github.rookietec9.enderplugin.scoreboards.ArchivedBoard;
import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.entity.Player;



/**
 * @author Jeremi
 * @version 18.5.9
 * @since 17.5.1
 */
public class JumpBoard extends ArchivedBoard {

    public JumpBoard(Player player) {
        super(player, Worlds.OLD_OBSTACLE, BoardNames.JUMP, "Parkour");
    }
}
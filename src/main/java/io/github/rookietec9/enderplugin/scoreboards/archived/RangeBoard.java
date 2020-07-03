package io.github.rookietec9.enderplugin.scoreboards.archived;

import io.github.rookietec9.enderplugin.scoreboards.ArchivedBoard;
import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.entity.Player;



/**
 * @author Jeremi
 * @version 18.5.8
 * @since 17.5.1
 */
public class RangeBoard extends ArchivedBoard {

    public RangeBoard(Player player) {
        super(player, Worlds.OLD_ARROWS, BoardNames.RANGE, "Shooting Range Mini-game");
    }
}

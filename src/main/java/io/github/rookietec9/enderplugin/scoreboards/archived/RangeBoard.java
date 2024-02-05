package io.github.rookietec9.enderplugin.scoreboards.archived;

import io.github.rookietec9.enderplugin.scoreboards.ArchivedBoard;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.OLD_ARROWS;


/**
 * @author Jeremi
 * @version 25.2.0
 * @since 17.5.1
 */
public class RangeBoard extends ArchivedBoard {

    public RangeBoard(Player player) {
        super(player, OLD_ARROWS, "§7§l§mRUSH", "Shooting Range Mini-game");
    }
}

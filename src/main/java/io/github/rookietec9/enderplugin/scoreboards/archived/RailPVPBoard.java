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
public class RailPVPBoard extends ArchivedBoard {

    public RailPVPBoard(Player player) {
        super(player, Worlds.OLD_RAIL_PVP, BoardNames.RAIL_PVP, "Fight on moving mine carts using bows");
    }
}

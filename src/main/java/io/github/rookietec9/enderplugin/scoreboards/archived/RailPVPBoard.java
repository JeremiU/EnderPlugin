package io.github.rookietec9.enderplugin.scoreboards.archived;

import io.github.rookietec9.enderplugin.scoreboards.ArchivedBoard;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.OLD_RAIL_PVP;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 17.5.1
 */
public class RailPVPBoard extends ArchivedBoard {

    public RailPVPBoard(Player player) {
        super(player, OLD_RAIL_PVP, "§7§l§mRAIL PVP", "Fight on moving mine carts using bows");
    }
}
